import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { GroupMembersController } from './group-members.controller';
import { GroupMembersService } from './group-members.service';
import { GroupMember, GroupMemberSchema } from './schemas/group-member.schema';
import { FriendshipModule } from '../friendship/friendship.module';

@Module({
  imports: [
    MongooseModule.forFeature([{ name: GroupMember.name, schema: GroupMemberSchema }]),
    FriendshipModule,
  ],
  controllers: [GroupMembersController],
  providers: [GroupMembersService],
  exports: [GroupMembersService]
})
export class GroupMembersModule { }
