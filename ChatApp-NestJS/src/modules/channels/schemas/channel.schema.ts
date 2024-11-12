import { Prop, Schema, SchemaFactory } from "@nestjs/mongoose";
import mongoose from "mongoose";
import { User } from "src/modules/users/schemas/user.schema";

@Schema({ timestamps: true })
export class Channel {
    @Prop({ type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true })
    userId: User;

    @Prop({ type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true })
    friendId: User;
}

export const ChannelSchema = SchemaFactory.createForClass(Channel);
