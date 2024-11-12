import { Body, Injectable } from '@nestjs/common';
import { ChannelMessage } from './schemas/channel-message.schema';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { ChannelsService } from '../channels/channels.service';
import { ChannelMessageDto } from './dto/channel-message.dto';
import { CloudinaryService } from 'src/cloudinary/cloudinary.service';

@Injectable()
export class ChannelMessagesService {
  constructor(
    @InjectModel(ChannelMessage.name)
    private readonly channelMessageModel: Model<ChannelMessage>,
    private readonly channelService: ChannelsService,
    private readonly cloudinaryService: CloudinaryService,
  ) { }

  async postMessage(req, @Body() channelMessageDto: ChannelMessageDto) {
    const { friendId, content } = channelMessageDto;
    const channel: any = await this.channelService.findChannel(req, friendId);

    const message: any = await this.channelMessageModel.create({
      channelId: channel._id,
      sender: req.user.userId,
      content: content
    });

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
    const { friendId } = channelMessageDto;
    const channel: any = await this.channelService.findChannel(req, friendId);

    const files = await this.cloudinaryService.uploadFile(file);

    const message: any = await this.channelMessageModel.create({
      channelId: channel._id,
      sender: req.user.userId,
      imageId: files.public_id,
      imageUrl: files.url,
    });

    return {
      sender: req.user.username,
      content: message.imageUrl,
      createdAt: message.createdAt,
    };
  }
}
