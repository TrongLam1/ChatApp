import { BadRequestException, Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import mongoose, { Model } from 'mongoose';
import { Friendship, StatusFriendship } from './schemas/friendship.schema';
import { UsersService } from '../users/users.service';

@Injectable()
export class FriendshipService {
  constructor(
    @InjectModel(Friendship.name)
    private readonly friendshipModel: Model<Friendship>,
    private readonly userService: UsersService
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
    const user = await this.userService.findOneById(req.user.userId);
    const friend = await this.userService.findOneById(userId);

    await this.existedFriendship(user, friend);

    const friendshipMe = await this.friendshipModel.create({
      userId: user._id.toString(),
      friendId: friend._id.toString(),
      status: StatusFriendship.WAITING
    });

    const friendshipFriend = await this.friendshipModel.create({
      friendId: user._id.toString(),
      userId: friend._id.toString(),
      status: StatusFriendship.PENDING
    });

    return friendshipMe;
  }

  async acceptFriend(req, userId: string) {
    const user = await this.userService.findOneById(req.user.userId);
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

    const friendshipFriend = await this.friendshipModel
      .findOneAndUpdate(
        {
          friendId: user._id,
          userId: friend._id,
          status: StatusFriendship.WAITING
        },
        { status: StatusFriendship.FRIEND },
        { new: true }
      );

    return friendshipMe;
  }

  async cancelFriendship(req, _userId: string) {
    const user = await this.userService.findOneById(req.user.userId);
    const friend = await this.userService.findOneById(_userId);

    const friendshipMe = await this.friendshipModel
      .findOneAndDelete(
        {
          userId: user._id,
          friendId: friend._id
        }
      );

    const friendshipFriend = await this.friendshipModel
      .findOneAndDelete(
        {
          friendId: user._id,
          userId: friend._id
        }
      );

    return "Hủy kết bạn thành công.";
  }

  async getListFriends(req) {
    const user = await this.userService.findOneById(req.user.userId);
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
      .lean(); // Chuyển kết quả sang object JSON thuần
  }

  async getListRequestFriends(req) {
    const user = await this.userService.findOneById(req.user.userId);
    return await this.friendshipModel
      .find({
        friendId: user._id,
        status: StatusFriendship.PENDING,
      })
      .select('userId status')
      .populate({
        path: 'userId',
        select: 'name email imageUrl',
      })
      .lean();
  }

  async countRequestFriends(req) {
    const user = await this.userService.findOneById(req.user.userId);
    return await this.friendshipModel
      .countDocuments({
        friendId: user._id,
        status: StatusFriendship.PENDING,
      });
  }
}
