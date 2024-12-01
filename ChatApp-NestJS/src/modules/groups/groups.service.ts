import { BadRequestException, Body, Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { GroupMembersService } from '../group-members/group-members.service';
import { UsersService } from '../users/users.service';
import { GroupDto } from './dto/group.dto';
import { Group } from './schemas/group.schema';
import { AddMembersDto } from './dto/add-members.dto';

@Injectable()
export class GroupsService {
  constructor(
    @InjectModel(Group.name)
    private readonly groupModel: Model<Group>,
    private readonly userService: UsersService,
    private readonly groupMemberService: GroupMembersService,
  ) { }

  private async findById(groupId: string) {
    const group = await this.groupModel
      .findOne({
        _id: groupId, isAvailable: true
      });
    if (!group) throw new NotFoundException("Không tìm thấy nhóm.");

    return group;
  }

  async createGroup(req, @Body() groupDto: GroupDto) {
    const { groupName, memberIds } = groupDto;
    const admin = await this.userService.findOneById(req.user.userId);

    const group = await this.groupModel.create({
      groupName: groupName, admin: admin._id
    });

    await this.groupMemberService.createGroupMember(admin, group);

    memberIds.map(async (memberId) => {
      const member = await this.userService.findOneById(memberId);
      await this.groupMemberService.createGroupMember(member, group);
    });

    return group;
  }

  async findGroupById(groupId: string) {
    const group: any = await this.findById(groupId);

    const countMembers = await this.groupMemberService.countMembersInGroup(groupId);

    return {
      group: {
        id: group._id,
        groupName: group.groupName,
        admin: group.admin
      },
      members: countMembers
    };
  }

  async addMember(req, @Body() addMembersDto: AddMembersDto) {
    const { groupId, memberIds } = addMembersDto;
    const user: any = await this.userService.findOneById(req.user.userId);

    const group = await this.findById(groupId);

    const members: any = await this.userService.findByIds(memberIds);
    await this.groupMemberService.addMember(user._id, members, group);
  }

  async getListMembersGroup(req, groupId: string) {
    const user: any = await this.userService.findOneById(req.user.userId);
    const group = await this.findById(groupId);

    return await this.groupMemberService.getListMembersGroup(user, group);
  }

  async getListFriendsInvite(req, groupId: string) {
    const user: any = await this.userService.findOneById(req.user.userId);
    const group = await this.findById(groupId);

    return await this.groupMemberService.getListFriendsInvite(req, user, group);
  }

  async getListGroupsByUser(req) {
    const user: any = await this.userService.findOneById(req.user.userId);
    return await this.groupMemberService.getListGroupsByUser(user._id);
  }

  async removeMember(req, groupId: string, memberId: string) {
    const user: any = await this.userService.findOneById(req.user.userId);

    const group = await this.groupModel.findOne({
      _id: groupId, isAvailable: true
    });
    if (!group) throw new NotFoundException("Không tìm thấy nhóm.");

    if (group.admin.toString() !== user._id.toString())
      throw new BadRequestException("Bạn không có quyền xóa thành viên khỏi nhóm.");

    const member: any = await this.userService.findOneById(memberId);

    return await this.groupMemberService.removeMember(group, member);
  }

  async quitGroup(req, groupId: string) {
    const user: any = await this.userService.findOneById(req.user.userId);
    const group: any = await this.findGroupById(groupId);

    if (group.group.admin.toString() === user._id.toString()) {
      await this.groupModel.findByIdAndUpdate(
        { _id: group.group.id },
        { isAvailable: false },
        { new: true }
      );
      return "Đã giải tán nhóm.";
    } else {
      await this.groupMemberService.removeMember(group.group, user._id);
      return "Đã thoát khỏi nhóm.";
    }
  }
}
