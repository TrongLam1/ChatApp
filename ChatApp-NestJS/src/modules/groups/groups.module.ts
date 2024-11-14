import { Module } from '@nestjs/common';
import { GroupsService } from './groups.service';
import { GroupsController } from './groups.controller';
import { MongooseModule } from '@nestjs/mongoose';
import { Group, GroupSchema } from './schemas/group.schema';
import { FriendshipModule } from '../friendship/friendship.module';
import { UsersModule } from '../users/users.module';
import { GroupMembersModule } from '../group-members/group-members.module';

@Module({
  imports: [
    MongooseModule.forFeature([{ name: Group.name, schema: GroupSchema }]),
    FriendshipModule,
    UsersModule,
    GroupMembersModule
  ],
  controllers: [GroupsController],
  providers: [GroupsService],
})
export class GroupsModule { }
