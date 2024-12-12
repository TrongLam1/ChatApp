import { BadRequestException, Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { GroupMember } from './schemas/group-member.schema';
import { FriendshipService } from '../friendship/friendship.service';

@Injectable()
export class GroupMembersService {
    constructor(
        @InjectModel(GroupMember.name)
        private readonly groupMemberModel: Model<GroupMember>,
        private readonly friendshipService: FriendshipService,
    ) { }

    private async existedMemberInGroup(memberId: string, group: any) {
        const isExisted = await this.groupMemberModel.exists({
            user: memberId, group: group._id
        });

        if (isExisted) throw new BadRequestException("Thành viên đã có trong nhóm.");

        return false;
    }

    async checkedUserInGroup(memberId: string, group: any) {
        const isExisted = await this.groupMemberModel.exists({
            user: memberId, group: group._id
        });

        if (isExisted === null) throw new BadRequestException("Bạn không ở trong nhóm này.");

        return true;
    }

    async createGroupMember(member: any, group: any) {
        await this.existedMemberInGroup(member._id, group);

        return await this.groupMemberModel.create({
            user: member._id, group: group._id
        });
    }

    async addMember(userId: string, members: any, group: any) {
        await this.checkedUserInGroup(userId, group);

        members.map(async (member: any) => {
            await this.createGroupMember(member._id, group);
        });
    }

    async getListMembersGroup(user, group: any) {
        await this.checkedUserInGroup(user._id, group);

        const members = await this.groupMemberModel
            .find({ group: group._id })
            .populate({
                path: 'user',
                select: '_id name email imageUrl'
            })
            .select('_id');

        const membersMap = new Map(
            members.flatMap((member: any) => [
                [member.user._id.toString(), member.user]
            ])
        );

        const memberIds: any = members
            .map((member: any) => member.user._id);

        return await this.friendshipService
            .findFriendshipsByUserIds(user, memberIds, membersMap);
    }

    async getListFriendsInvite(req, user, group: any) {
        await this.checkedUserInGroup(user._id, group);

        const members = await this.groupMemberModel
            .find({ group: group._id })
            .populate({
                path: 'user',
                select: '_id'
            })
            .select('_id');

        const memberIds = new Set(members.map((member: any) => member.user._id.toString()));

        const friends = await this.friendshipService.getListFriends(req);

        const filteredFriends = friends.filter(
            (friend: any) => !memberIds.has(friend.userId._id.toString())
        );

        return filteredFriends;
    }

    async getListGroupsByUser(userId: string) {
        const groups = await this.groupMemberModel
            .find({ user: userId })
            .populate({
                path: 'group',
                match: { isAvailable: true },
                select: '_id groupName'
            })
            .select('_id createdAt');

        return groups.filter(group => group.group !== null);
    }

    async countMembersInGroup(groupId: string) {
        return await this.groupMemberModel.countDocuments({ group: groupId });
    }

    async removeMember(group: any, memberId: any) {
        await this.groupMemberModel.findOneAndDelete({
            user: memberId, group: group.id
        });

        return "Xóa thành viên thành công.";
    }
}
