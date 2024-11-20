import { Body, Controller, Delete, Get, Param, Post, Put, Req, UseGuards } from '@nestjs/common';
import { JwtAuthGuard } from 'src/auth/guard/jwt-auth.guard';
import { FriendshipDto } from './dto/friendship.dto';
import { FriendshipService } from './friendship.service';

@Controller('friendship')
export class FriendshipController {
  constructor(private readonly friendshipService: FriendshipService) { }

  @Post('request')
  @UseGuards(JwtAuthGuard)
  async requestFriend(@Req() req, @Body() friendshipDto: FriendshipDto) {
    return this.friendshipService.requestFriend(req, friendshipDto.friendId);
  }

  @Put('accept')
  @UseGuards(JwtAuthGuard)
  async acceptFriend(@Req() req, @Body() friendshipDto: FriendshipDto) {
    return this.friendshipService.acceptFriend(req, friendshipDto.friendId);
  }

  @Delete('cancel')
  @UseGuards(JwtAuthGuard)
  async cancelFriendship(@Req() req, @Body() friendshipDto: FriendshipDto) {
    return this.friendshipService.cancelFriendship(req, friendshipDto.friendId);
  }

  @Get('find-friend/:friendId')
  @UseGuards(JwtAuthGuard)
  async findFriend(@Req() req, @Param('friendId') friendId: string) {
    return this.friendshipService.findFriend(req, friendId);
  }

  @Get('list-friends')
  @UseGuards(JwtAuthGuard)
  async getListFriends(@Req() req) {
    return this.friendshipService.getListFriends(req);
  }

  @Get('list-request-friends')
  @UseGuards(JwtAuthGuard)
  async getListRequestFriends(@Req() req) {
    return this.friendshipService.getListRequestFriends(req);
  }

  @Get('count-request-friends')
  @UseGuards(JwtAuthGuard)
  async countRequestFriends(@Req() req) {
    return this.friendshipService.countRequestFriends(req);
  }

  @Get('find-by-name/:name')
  @UseGuards(JwtAuthGuard)
  async findUserByName(@Req() req, @Param('name') name: string) {
    return await this.friendshipService.findUsersByName(req, name);
  }
}
