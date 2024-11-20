import { BadRequestException, Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { GroupMember } from './schemas/group-member.schema';

@Injectable()
export class GroupMembersService {
    constructor(
        @InjectModel(GroupMember.name)
        private readonly groupMemberModel: Model<GroupMember>
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

        if (!isExisted) throw new BadRequestException("Bạn không ở trong nhóm này.");

        return true;
    }

    async createGroupMember(member: any, group: any) {
        await this.existedMemberInGroup(member._id, group);

        return await this.groupMemberModel.create({
            user: member._id, group: group._id
        });
    }

    async addMember(userId: string, memberId: string, group: any) {
        await this.checkedUserInGroup(userId, group);

        await this.createGroupMember(memberId, group);
    }

    async getListMembersGroup(userId: string, group: any) {
        await this.checkedUserInGroup(userId, group);

        return await this.groupMemberModel
            .find({ group: group._id })
            .populate({
                path: 'user',
                select: '_id name email'
            })
            .select('_id');
    }

    async getListGroupsByUser(userId: string) {
        return await this.groupMemberModel
            .find({ user: userId })
            .populate({
                path: 'group',
                select: '_id groupName'
            })
            .select('_id createdAt');
    }

    async removeMember(group: any, member: any) {
        await this.groupMemberModel.findOneAndDelete({
            user: member._id, group: group._id
        });

        return "Xóa thành viên thành công.";
    }
}
