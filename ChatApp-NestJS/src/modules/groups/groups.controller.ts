import { Body, Controller, Delete, Get, Param, Post, Query, Req, UseGuards } from '@nestjs/common';
import { GroupsService } from './groups.service';
import { GroupDto } from './dto/group.dto';
import { JwtAuthGuard } from 'src/auth/guard/jwt-auth.guard';
import { AddMembersDto } from './dto/add-members.dto';
import { ApiOperation } from '@nestjs/swagger';

@Controller('groups')
export class GroupsController {
  constructor(private readonly groupsService: GroupsService) { }

  @Post('create')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Create new group.' })
  async createGroup(@Req() req, @Body() groupDto: GroupDto) {
    return await this.groupsService.createGroup(req, groupDto);
  }

  @Post('add-member')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Add members to group.' })
  async addMember(
    @Req() req,
    @Body() addMembersDto: AddMembersDto
  ) {
    return await this.groupsService.addMember(req, addMembersDto);
  }

  @Get('members/:groupId')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Get list members of group.' })
  async getListMembersGroup(@Req() req, @Param('groupId') groupId: string) {
    return await this.groupsService.getListMembersGroup(req, groupId);
  }

  @Get('friends-invite/:groupId')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Get list friends without of group.' })
  async getListFriendsInvite(@Req() req, @Param('groupId') groupId: string) {
    return await this.groupsService.getListFriendsInvite(req, groupId);
  }

  @Get('list-groups-by-user')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Get list groups of user.' })
  async getListGroupsByUser(@Req() req) {
    return await this.groupsService.getListGroupsByUser(req);
  }

  @Get('find-group/:groupId')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Find group by id.' })
  async findGroupById(@Param('groupId') groupId: string) {
    return await this.groupsService.findGroupById(groupId);
  }

  @Delete('remove-member')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Remove members from group.' })
  async removeMember(
    @Req() req,
    @Body() removeMembers: any
  ) {
    return await this.groupsService.removeMember(req, removeMembers.groupId, removeMembers.memberId);
  }

  @Delete('quit/:groupId')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Quit out group.' })
  async quitGroup(@Req() req, @Param('groupId') groupId: string) {
    return await this.groupsService.quitGroup(req, groupId);
  }
}
