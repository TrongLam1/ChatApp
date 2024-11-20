import { Body, Injectable } from '@nestjs/common';
import { GroupMessage } from './schemas/group-message.schema';
import { GroupsService } from '../groups/groups.service';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { CloudinaryService } from 'src/cloudinary/cloudinary.service';
import { GroupMessageDto } from './dto/group-message.dto';
import { GroupMembersService } from '../group-members/group-members.service';

@Injectable()
export class GroupMessagesService {
    constructor(
        @InjectModel(GroupMessage.name)
        private readonly groupMessageModel: Model<GroupMessage>,
        private readonly groupService: GroupsService,
        private readonly groupMemberService: GroupMembersService,
        private readonly cloudinaryService: CloudinaryService,
    ) { }

    async postMessage(req, @Body() groupMessageDto: GroupMessageDto) {
        const { groupId, content } = groupMessageDto;
        const group: any = await this.groupService.findGroupById(groupId);

        await this.groupMemberService.checkedUserInGroup(req.user.userId, group);

        const message: any = await this.groupMessageModel.create({
            group: group._id,
            sender: req.user.userId,
            content: content
        });

        return {
            sender: req.user.username,
            content: message.content,
            createdAt: message.createdAt,
        };
    }

    async postImage(
        req,
        @Body() groupMessageDto: GroupMessageDto,
        file: Express.Multer.File) {
        const { groupId } = groupMessageDto;
        const group: any = await this.groupService.findGroupById(groupId);

        await this.groupMemberService.checkedUserInGroup(req.user.userId, group);

        const files = await this.cloudinaryService.uploadFile(file);

        const message: any = await this.groupMessageModel.create({
            group: group._id,
            sender: req.user.userId,
            imageId: files.public_id,
            imageUrl: files.url,
        });

        return {
            sender: req.user.username,
            content: message.imageUrl,
            createdAt: message.createdAt,
        };
    }
}
