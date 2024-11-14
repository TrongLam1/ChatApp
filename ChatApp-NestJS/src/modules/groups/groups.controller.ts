import { Body, Controller, Delete, Get, Param, Post, Query, Req, UseGuards } from '@nestjs/common';
import { GroupsService } from './groups.service';
import { GroupDto } from './dto/group.dto';
import { JwtAuthGuard } from 'src/auth/guard/jwt-auth.guard';

@Controller('groups')
export class GroupsController {
  constructor(private readonly groupsService: GroupsService) { }

  @Post('create')
  @UseGuards(JwtAuthGuard)
  async createGroup(@Req() req, @Body() groupDto: GroupDto) {
    return await this.groupsService.createGroup(req, groupDto);
  }

  @Post('add-member')
  @UseGuards(JwtAuthGuard)
  async addMember(
    @Req() req,
    @Query('groupId') groupId: string,
    @Query('memberId') memberId: string,
  ) {
    return await this.groupsService.addMember(req, groupId, memberId);
  }

  @Get('members/:groupId')
  @UseGuards(JwtAuthGuard)
  async getListMembersGroup(@Req() req, @Param('groupId') groupId: string) {
    return await this.groupsService.getListMembersGroup(req, groupId);
  }

  @Get('list-groups-by-user')
  @UseGuards(JwtAuthGuard)
  async getListGroupsByUser(@Req() req) {
    return await this.groupsService.getListGroupsByUser(req);
  }

  @Delete('remove-member')
  @UseGuards(JwtAuthGuard)
  async removeMember(
    @Req() req,
    @Query('groupId') groupId: string,
    @Query('memberId') memberId: string,
  ) {
    return await this.groupsService.removeMember(req, groupId, memberId);
  }

  @Delete('quit/:groupId')
  @UseGuards(JwtAuthGuard)
  async quitGroup(@Req() req, @Param('groupId') groupId: string) {
    return await this.groupsService.quitGroup(req, groupId);
  }
}
