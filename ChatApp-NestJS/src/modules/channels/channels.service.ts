import { BadRequestException, Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Channel } from 'diagnostics_channel';
import { Model } from 'mongoose';
import { FriendshipService } from '../friendship/friendship.service';

@Injectable()
export class ChannelsService {
  constructor(
    @InjectModel(Channel.name)
    private readonly channelModel: Model<Channel>,
    private readonly friendshipService: FriendshipService
  ) { }

  async createNewChannel(req, friendId: string) {
    const isFriend = await this.friendshipService.isFriend(req, friendId);
    if (!isFriend) throw new BadRequestException("Chưa kết bạn, không thể nhắn tin.");

    const channel = await this.channelModel.create(
      { userId: req.user.userId, friendId: friendId });

    return channel;
  }

  async findChannel(req, friendId: string) {
    if (req.user.userId === friendId) throw new BadRequestException("Người nhận không hợp lệ.");

    let channel: any = await this.channelModel
      .findOne({
        userId: req.user.userId, friendId
      })
      .populate({
        path: 'friendId',
        select: 'name email imageUrl',
      });

    if (!channel) {
      channel = await this.channelModel
        .findOne({
          friendId: req.user.userId, userId: friendId
        })
        .populate({
          path: 'friendId',
          select: 'name email imageUrl',
        });

      if (!channel) {
        channel = await this.createNewChannel(req, friendId);
      }
    }

    return {
      channelId: channel._id
    };
  }

  async findChannelById(channelId: string) {
    return await this.channelModel.findOne({ _id: channelId });
  }
}
