import { Prop, Schema, SchemaFactory } from "@nestjs/mongoose";
import mongoose from "mongoose";
import { User } from "src/modules/users/schemas/user.schema";

export enum StatusFriendship {
    WAITING = 'waiting',
    PENDING = 'pending',
    FRIEND = 'friend',
    BLOCK = 'block',
    IS_BLOCKED = 'is_blocked',
}

@Schema({ timestamps: true })
export class Friendship {
    @Prop({ type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true })
    userId: User;

    @Prop({ type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true })
    friendId: User;

    @Prop({ required: true, enum: StatusFriendship, type: String })
    status: StatusFriendship;
}

export const FriendshipSchema = SchemaFactory.createForClass(Friendship);
