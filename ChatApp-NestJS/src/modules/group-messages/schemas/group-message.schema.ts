import { Prop, Schema, SchemaFactory } from "@nestjs/mongoose";
import mongoose from "mongoose";
import { Group } from "src/modules/groups/schemas/group.schema";
import { User } from "src/modules/users/schemas/user.schema";

@Schema({ timestamps: true })
export class GroupMessage {
    @Prop({ type: mongoose.Schema.Types.ObjectId, ref: 'Group', required: true })
    group: Group;

    @Prop({ type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true })
    sender: User;

    @Prop()
    content: string;

    @Prop()
    imageId: string;

    @Prop()
    imageUrl: string;
}

export const GroupMessageSchema = SchemaFactory.createForClass(GroupMessage);
