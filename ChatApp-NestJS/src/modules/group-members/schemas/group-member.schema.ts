import { Prop, Schema, SchemaFactory } from "@nestjs/mongoose";
import mongoose from "mongoose";
import { Group } from "src/modules/groups/schemas/group.schema";
import { User } from "src/modules/users/schemas/user.schema";

@Schema({ timestamps: true })
export class GroupMember {
    @Prop({ type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true })
    user: User;

    @Prop({ type: mongoose.Schema.Types.ObjectId, ref: 'Group', required: true })
    group: Group;
}

export const GroupMemberSchema = SchemaFactory.createForClass(GroupMember);
