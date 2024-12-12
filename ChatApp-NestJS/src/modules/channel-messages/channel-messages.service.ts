import { Body, Injectable } from '@nestjs/common';
import { ChannelMessage } from './schemas/channel-message.schema';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { ChannelsService } from '../channels/channels.service';
import { ChannelMessageDto } from './dto/channel-message.dto';
import { CloudinaryService } from 'src/cloudinary/cloudinary.service';
import { RealTimeGateway } from '../real-time/real-time.gateway';

@Injectable()
export class ChannelMessagesService {
  constructor(
    @InjectModel(ChannelMessage.name)
    private readonly channelMessageModel: Model<ChannelMessage>,
    private readonly channelService: ChannelsService,
    private readonly cloudinaryService: CloudinaryService,
    private readonly realTimeGateway: RealTimeGateway,
  ) { }

  async postMessage(req, @Body() channelMessageDto: ChannelMessageDto) {
    const { id, content } = channelMessageDto;
    const channel: any = await this.channelService.findChannelById(id);

    const message: any = await this.channelMessageModel.create({
      channelId: channel._id,
      sender: req.user._id,
      content: content
    });

    this.realTimeGateway.handleSendMessage({
      _id: message._id,
      sender: {
        _id: req.user._id,
        name: req.user.username,
        imageUrl: req.user.avatar
      },
      subscribeId: channel._id,
      content: message.content,
      createdAt: message.createdAt,
    }, channel._id.toString());

    const sendTo = channel.userId._id.toString() === req.user._id ?
      channel.friendId._id.toString() : channel.userId._id.toString();

    this.realTimeGateway.handleSendNotification({
      type: 'New message',
      subscribe: channel._id.toString(),
      messageFrom: req.user.username
    }, sendTo);

    return {
      sender: req.user.username,
      content: message.content,
      createdAt: message.createdAt,
    };
  }

  async postImage(
    req,
    @Body() channelMessageDto: ChannelMessageDto,
    file: Express.Multer.File) {
    const { id } = channelMessageDto;
    const channel: any = await this.channelService.findChannelById(id);

    const files = await this.cloudinaryService.uploadFile(file);

    const message: any = await this.channelMessageModel.create({
      channelId: channel._id,
      sender: req.user._id,
      imageId: files.public_id,
      imageUrl: files.url,
    });

    this.realTimeGateway.handleSendMessage({
      _id: message._id,
      sender: {
        _id: req.user._id,
        name: req.user.username,
        imageUrl: req.user.avatar
      },
      subscribeId: channel._id,
      imageUrl: message.imageUrl,
      createdAt: message.createdAt,
    }, channel._id.toString());

    const sendTo = channel.userId._id.toString() === req.user._id ?
      channel.friendId._id.toString() : channel.userId._id.toString();

    this.realTimeGateway.handleSendNotification({
      type: 'New message',
      subscribe: channel._id.toString(),
      messageFrom: req.user.username
    }, sendTo);

    return {
      sender: req.user.username,
      content: message.imageUrl,
      createdAt: message.createdAt,
    };
  }

  async getMessages(channelId: string) {
    const channel: any = await this.channelService.findChannelById(channelId);

    const messages = await this.channelMessageModel
      .find({ channelId: channel._id })
      .populate({
        path: 'sender',
        select: 'name imageUrl'
      })
      .select('content imageUrl createdAt');

    return messages;
  }
}
