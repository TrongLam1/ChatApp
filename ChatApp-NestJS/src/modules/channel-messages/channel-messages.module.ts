import { Module } from '@nestjs/common';
import { ChannelMessagesService } from './channel-messages.service';
import { ChannelMessagesController } from './channel-messages.controller';
import { ChannelsModule } from '../channels/channels.module';
import { MongooseModule } from '@nestjs/mongoose';
import { ChannelMessage, ChannelMessageSchema } from './schemas/channel-message.schema';
import { CloudinaryModule } from 'src/cloudinary/cloudinary.module';

@Module({
  imports: [
    MongooseModule.forFeature([{ name: ChannelMessage.name, schema: ChannelMessageSchema }]),
    ChannelsModule,
    CloudinaryModule
  ],
  controllers: [ChannelMessagesController],
  providers: [ChannelMessagesService],
  exports: [ChannelMessagesService],
})
export class ChannelMessagesModule { }
