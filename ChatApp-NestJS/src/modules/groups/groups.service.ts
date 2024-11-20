import { BadRequestException, Body, Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { GroupMembersService } from '../group-members/group-members.service';
import { UsersService } from '../users/users.service';
import { GroupDto } from './dto/group.dto';
import { Group } from './schemas/group.schema';

@Injectable()
export class GroupsService {
  constructor(
    @InjectModel(Group.name)
    private readonly groupModel: Model<Group>,
    private readonly userService: UsersService,
    private readonly groupMemberService: GroupMembersService,
  ) { }

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
    const group = await this.groupModel.findOne({
      _id: groupId, isAvailable: true
    });
    if (!group) throw new NotFoundException("Không tìm thấy nhóm.");
    return group;
  }

  async addMember(req, groupId: string, memberId: string) {
    const user: any = await this.userService.findOneById(req.user.userId);
    const group = await this.findGroupById(groupId);
    const member: any = await this.userService.findOneById(memberId);
    await this.groupMemberService.addMember(user._id, member._id, group);
  }

  async getListMembersGroup(req, groupId: string) {
    const user: any = await this.userService.findOneById(req.user.userId);
    const group = await this.findGroupById(groupId);

    return await this.groupMemberService.getListMembersGroup(user._id, group);
  }

  async getListGroupsByUser(req) {
    const user: any = await this.userService.findOneById(req.user.userId);
    return await this.groupMemberService.getListGroupsByUser(user._id);
  }

  async removeMember(req, groupId: string, memberId: string) {
    const user: any = await this.userService.findOneById(req.user.userId);
    const group: any = await this.findGroupById(groupId);
    if (group.admin.toString() !== user._id.toString())
      throw new BadRequestException("Bạn không có quyền xóa thành viên khỏi nhóm.");

    const member: any = await this.userService.findOneById(memberId);

    return await this.groupMemberService.removeMember(group, member);
  }

  async quitGroup(req, groupId: string) {
    const user: any = await this.userService.findOneById(req.user.userId);
    const group: any = await this.findGroupById(groupId);

    if (group.admin.toString() === user._id.toString()) {
      await this.groupModel.findByIdAndUpdate(
        { _id: group._id },
        { isAvailable: false },
        { new: true }
      );
      return "Đã giải tán nhóm.";
    } else {
      await this.groupMemberService.removeMember(group._id, user._id);
      return "Đã thoát khỏi nhóm.";
    }
  }
}
