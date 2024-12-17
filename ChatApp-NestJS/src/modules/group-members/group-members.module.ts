import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { GroupMembersService } from './group-members.service';
import { GroupMember, GroupMemberSchema } from './schemas/group-member.schema';
import { FriendshipModule } from '../friendship/friendship.module';

@Module({
  imports: [
    MongooseModule.forFeature([{ name: GroupMember.name, schema: GroupMemberSchema }]),
    FriendshipModule,
  ],
  providers: [GroupMembersService],
  exports: [GroupMembersService]
})
export class GroupMembersModule { }
