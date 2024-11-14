import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { GroupMembersController } from './group-members.controller';
import { GroupMembersService } from './group-members.service';
import { GroupMember, GroupMemberSchema } from './schemas/group-member.schema';

@Module({
  imports: [
    MongooseModule.forFeature([{ name: GroupMember.name, schema: GroupMemberSchema }])
  ],
  controllers: [GroupMembersController],
  providers: [GroupMembersService],
  exports: [GroupMembersService]
})
export class GroupMembersModule { }
