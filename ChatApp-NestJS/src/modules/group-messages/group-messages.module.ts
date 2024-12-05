import { Module } from '@nestjs/common';
import { GroupMessagesService } from './group-messages.service';
import { GroupMessagesController } from './group-messages.controller';
import { GroupsModule } from '../groups/groups.module';
import { GroupMessage, GroupMessageSchema } from './schemas/group-message.schema';
import { MongooseModule } from '@nestjs/mongoose';
import { CloudinaryModule } from 'src/cloudinary/cloudinary.module';
import { GroupMembersModule } from '../group-members/group-members.module';
import { UsersModule } from '../users/users.module';
import { RealTimeModule } from '../real-time/real-time.module';

@Module({
  imports: [
    MongooseModule.forFeature([{ name: GroupMessage.name, schema: GroupMessageSchema }]),
    UsersModule,
    GroupsModule,
    GroupMembersModule,
    CloudinaryModule,
    RealTimeModule
  ],
  controllers: [GroupMessagesController],
  providers: [GroupMessagesService],
  exports: [GroupMessagesService],
})
export class GroupMessagesModule { }
