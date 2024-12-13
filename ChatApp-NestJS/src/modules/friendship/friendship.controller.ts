import { Body, Controller, Delete, Get, Param, Post, Put, Req, UseGuards } from '@nestjs/common';
import { JwtAuthGuard } from 'src/auth/guard/jwt-auth.guard';
import { FriendshipDto } from './dto/friendship.dto';
import { FriendshipService } from './friendship.service';
import { ApiOperation, ApiResponse } from '@nestjs/swagger';

@Controller('friendship')
export class FriendshipController {
  constructor(private readonly friendshipService: FriendshipService) { }

  @Post('request')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Send request friend.' })
  @ApiResponse({ status: 201, description: 'Create new friendship with status WAITING.' })
  async requestFriend(@Req() req, @Body() friendshipDto: FriendshipDto) {
    return this.friendshipService.requestFriend(req, friendshipDto.friendId);
  }

  @Put('accept')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Accept friend.' })
  @ApiResponse({ status: 200, description: 'Accept friendship and status is FRIEND.' })
  async acceptFriend(@Req() req, @Body() friendshipDto: FriendshipDto) {
    return this.friendshipService.acceptFriend(req, friendshipDto.friendId);
  }

  @Delete('cancel')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Cancel friend.' })
  @ApiResponse({ status: 200, description: 'Delete friendship.' })
  async cancelFriendship(@Req() req, @Body() friendshipDto: FriendshipDto) {
    return this.friendshipService.cancelFriendship(req, friendshipDto.friendId);
  }

  @Get('find-friend/:friendId')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Find friend by friend id' })
  @ApiResponse({ status: 200, description: 'Info friend.' })
  async findFriend(@Req() req, @Param('friendId') friendId: string) {
    return this.friendshipService.findFriend(req, friendId);
  }

  @Get('list-friends')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Get list friends.' })
  @ApiResponse({ status: 200, description: 'List of friends have status FRIEND.' })
  async getListFriends(@Req() req) {
    return this.friendshipService.getListFriends(req);
  }

  @Get('list-request-friends')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Get list requests friend.' })
  @ApiResponse({ status: 200, description: 'List of friends have status PENDING.' })
  async getListRequestFriends(@Req() req) {
    return this.friendshipService.getListRequestFriends(req);
  }

  @Get('count-request-friends')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Count total requests friend.' })
  async countRequestFriends(@Req() req) {
    return this.friendshipService.countRequestFriends(req);
  }

  @Get('find-by-name/:name')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Find friend by name.' })
  async findUserByName(@Req() req, @Param('name') name: string) {
    return await this.friendshipService.findUsersByName(req, name);
  }
}
