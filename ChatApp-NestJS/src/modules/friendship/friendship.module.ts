import { Module } from '@nestjs/common';
import { FriendshipService } from './friendship.service';
import { FriendshipController } from './friendship.controller';
import { MongooseModule } from '@nestjs/mongoose';
import { Friendship, FriendshipSchema } from './schemas/friendship.schema';
import { UsersModule } from '../users/users.module';

@Module({
  imports: [
    MongooseModule.forFeature([{ name: Friendship.name, schema: FriendshipSchema }]),
    UsersModule
  ],
  controllers: [FriendshipController],
  providers: [FriendshipService],
  exports: [FriendshipService]
})
export class FriendshipModule { }