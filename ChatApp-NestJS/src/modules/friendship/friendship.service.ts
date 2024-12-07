import { BadRequestException, Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { UsersService } from '../users/users.service';
import { Friendship, StatusFriendship } from './schemas/friendship.schema';
import { RealTimeGateway } from '../real-time/real-time.gateway';

@Injectable()
export class FriendshipService {
  constructor(
    @InjectModel(Friendship.name)
    private readonly friendshipModel: Model<Friendship>,
    private readonly userService: UsersService,
    private readonly realTimeGateway: RealTimeGateway,
  ) { }

  private async existedFriendship(user, friend) {
    const existedFriendship = await this.friendshipModel.exists({
      userId: user._id.toString(),
      friendId: friend._id.toString()
    });
    if (existedFriendship) throw new BadRequestException("Bạn đã là bạn bè.");

    return;
  }

  async requestFriend(req, userId: string) {
    const user = await this.userService.findOneById(req.user._id);
    const friend = await this.userService.findOneById(userId);

    await this.existedFriendship(user, friend);

    const friendshipMe = await this.friendshipModel.create({
      userId: user._id.toString(),
      friendId: friend._id.toString(),
      status: StatusFriendship.WAITING
    });

    await this.friendshipModel.create({
      friendId: user._id.toString(),
      userId: friend._id.toString(),
      status: StatusFriendship.PENDING
    });

    this.realTimeGateway.handleSendNotification({
      type: 'Request friend',
      messageFrom: req.user.username
    }, friend._id.toString());

    return friendshipMe;
  }

  async acceptFriend(req, userId: string) {
    const user = await this.userService.findOneById(req.user._id);
    const friend = await this.userService.findOneById(userId);

    const friendshipMe = await this.friendshipModel
      .findOneAndUpdate(
        {
          userId: user._id,
          friendId: friend._id,
          status: StatusFriendship.PENDING
        },
        { status: StatusFriendship.FRIEND },
        { new: true }
      );

    if (!friendshipMe) throw new BadRequestException("Bạn không thể đồng ý kết bạn.");

    await this.friendshipModel
      .findOneAndUpdate(
        {
          friendId: user._id,
          userId: friend._id,
          status: StatusFriendship.WAITING
        },
        { status: StatusFriendship.FRIEND },
        { new: true }
      );

    const countRequest = await this.countRequestFriends(req);

    return {
      friend: friendshipMe, countRequest
    };
  }

  async cancelFriendship(req, _userId: string) {
    const user = await this.userService.findOneById(req.user._id);
    const friend = await this.userService.findOneById(_userId);

    await this.friendshipModel
      .findOneAndDelete(
        {
          userId: user._id,
          friendId: friend._id
        }
      );

    await this.friendshipModel
      .findOneAndDelete(
        {
          friendId: user._id,
          userId: friend._id
        }
      );

    return await this.countRequestFriends(req);
  }

  async isFriend(req, friendId: string) {
    const user = await this.userService.findOneById(req.user._id);
    const friend = await this.userService.findOneById(friendId);

    const friendship = await this.friendshipModel.findOne({
      userId: user._id,
      friendId: friend._id
    });

    if (friendship.status === StatusFriendship.FRIEND) return true;

    return false;
  }

  async findFriend(req, friendId: string) {
    const user = await this.userService.findOneById(req.user._id);
    return await this.friendshipModel
      .findOne({
        userId: user._id,
        friendId: friendId,
        status: StatusFriendship.FRIEND,
      })
      .select('userId status')
      .populate({
        path: 'userId',
        select: 'name email imageUrl',
      })
      .lean(); // Chuyển kết quả sang object JSON thuần
  }

  async getListFriends(req) {
    const user = await this.userService.findOneById(req.user._id);
    return await this.friendshipModel
      .find({
        friendId: user._id,
        status: StatusFriendship.FRIEND,
      })
      .select('userId status')
      .populate({
        path: 'userId',
        select: 'name email imageUrl',
      })
      .lean();
  }

  async getListRequestFriends(req) {
    const user = await this.userService.findOneById(req.user._id);
    return await this.friendshipModel
      .find({
        userId: user._id,
        status: StatusFriendship.PENDING,
      })
      .select('status')
      .populate({
        path: 'friendId',
        select: 'name email imageUrl',
      })
      .lean();
  }

  async countRequestFriends(req) {
    const user = await this.userService.findOneById(req.user._id);
    return await this.friendshipModel
      .countDocuments({
        userId: user._id,
        status: StatusFriendship.PENDING,
      });
  }

  async findUsersByName(req, name: string) {
    const me = await this.userService.findOneById(req.user._id);
    const users = await this.userService.findUsersByName(name);

    const usersMap = new Map(
      users.flatMap(user => [
        [user._id.toString(), user]
      ])
    );

    const userIds: any = users
      .filter(user => user._id.toString() !== req.user._id)
      .map(user => user._id);

    return await this.findFriendshipsByUserIds(me, userIds, usersMap);
  }

  async findFriendshipsByUserIds(user, userIds, usersMap) {
    const friendships = await this.friendshipModel
      .find({
        userId: user._id,
        friendId: { $in: userIds }
      })
      .populate({ path: 'friendId', select: 'name email imageUrl' })
      .select('friendId status')
      .exec();

    const userIdsInResults = new Set(
      friendships.flatMap((friendship: any) => [
        friendship.friendId._id.toString()
      ])
    );

    userIds = userIds.map(userId => userId.toString());

    const missingFriendships = userIds
      .filter(userId => !userIdsInResults.has(userId))
      .map(userId => {
        if (usersMap.get(userId)._id.toString() !== user._id.toString()) {
          return {
            friendId: usersMap.get(userId),
            status: null
          }
        } else {
          return {
            friendId: usersMap.get(userId),
            status: 'friend'
          }
        }
      });

    return [...friendships, ...missingFriendships];
  }
}
