import { Injectable } from '@nestjs/common';
import { CreateChannelMessageDto } from './dto/create-channel-message.dto';
import { UpdateChannelMessageDto } from './dto/update-channel-message.dto';

@Injectable()
export class ChannelMessagesService {
  create(createChannelMessageDto: CreateChannelMessageDto) {
    return 'This action adds a new channelMessage';
  }

  findAll() {
    return `This action returns all channelMessages`;
  }

  findOne(id: number) {
    return `This action returns a #${id} channelMessage`;
  }

  update(id: number, updateChannelMessageDto: UpdateChannelMessageDto) {
    return `This action updates a #${id} channelMessage`;
  }

  remove(id: number) {
    return `This action removes a #${id} channelMessage`;
  }
}
