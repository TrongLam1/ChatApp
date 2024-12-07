import { Body, Injectable } from '@nestjs/common';
import { GroupMessage } from './schemas/group-message.schema';
import { GroupsService } from '../groups/groups.service';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { CloudinaryService } from 'src/cloudinary/cloudinary.service';
import { GroupMessageDto } from './dto/group-message.dto';
import { GroupMembersService } from '../group-members/group-members.service';
import { UsersService } from '../users/users.service';
import { RealTimeGateway } from '../real-time/real-time.gateway';

@Injectable()
export class GroupMessagesService {
    constructor(
        @InjectModel(GroupMessage.name)
        private readonly groupMessageModel: Model<GroupMessage>,
        private readonly userService: UsersService,
        private readonly groupService: GroupsService,
        private readonly groupMemberService: GroupMembersService,
        private readonly cloudinaryService: CloudinaryService,
        private readonly realTimeGateway: RealTimeGateway,
    ) { }

    async postMessage(req, @Body() groupMessageDto: GroupMessageDto) {
        const { id, content } = groupMessageDto;
        const group: any = await this.groupService.findById(id);

        await this.groupMemberService.checkedUserInGroup(req.user._id, group);

        const message: any = await this.groupMessageModel.create({
            group: group._id,
            sender: req.user._id,
            content: content
        });

        const members = await this.groupMemberService.getListMembersGroup(req.user, group);

        this.realTimeGateway.handleSendMessage({
            _id: message._id,
            sender: {
                _id: req.user._id,
                name: req.user.username,
                imageUrl: req.user.avatar
            },
            subscribeId: group._id,
            content: message.content,
            createdAt: message.createdAt,
        }, id);

        members
            .filter(member => member.friendId._id.toString() !== req.user._id)
            .map(member => {
                this.realTimeGateway.handleSendNotification({
                    type: 'New message',
                    subscribe: group._id.toString(),
                    messageFrom: `Nhóm '${group.groupName}'`
                }, member.friendId._id.toString());
            })

        return {
            sender: {
                _id: req.user._id,
                name: req.user.username,
                imageUrl: req.user.avatar
            },
            content: message.content,
            createdAt: message.createdAt,
        };
    }

    async postImage(
        req,
        @Body() groupMessageDto: GroupMessageDto,
        file: Express.Multer.File) {
        const { id } = groupMessageDto;
        const group: any = await this.groupService.findById(id);
        const sender = await this.userService.findOneById(req.user._id);

        await this.groupMemberService.checkedUserInGroup(req.user._id, group);

        const files = await this.cloudinaryService.uploadFile(file);

        const message: any = await this.groupMessageModel.create({
            group: group._id,
            sender: req.user._id,
            imageId: files.public_id,
            imageUrl: files.url,
        });

        return {
            sender: {
                name: sender.name,
                avatar: sender.imageUrl
            },
            content: message.imageUrl,
            createdAt: message.createdAt,
        };
    }

    async getMessages(groupId: string) {
        const group = await this.groupService.findById(groupId);

        return await this.groupMessageModel
            .find({ group: group._id })
            .populate({
                path: 'sender',
                select: 'name imageUrl'
            })
            .select('content imageUrl createdAt');
    }
}
