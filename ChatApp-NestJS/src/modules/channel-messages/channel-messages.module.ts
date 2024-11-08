import { Module } from '@nestjs/common';
import { ChannelMessagesService } from './channel-messages.service';
import { ChannelMessagesController } from './channel-messages.controller';

@Module({
  controllers: [ChannelMessagesController],
  providers: [ChannelMessagesService],
})
export class ChannelMessagesModule {}
