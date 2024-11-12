import { Prop, Schema, SchemaFactory } from "@nestjs/mongoose";
import mongoose from "mongoose";

@Schema({ timestamps: true })
export class ChannelMessage {
    @Prop({ type: mongoose.Schema.Types.ObjectId, ref: 'Channel', required: true })
    channelId: string;

    @Prop({ type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true })
    sender: string;

    @Prop()
    content: string;

    @Prop()
    imageId: string;

    @Prop()
    imageUrl: string;
}

export const ChannelMessageSchema = SchemaFactory.createForClass(ChannelMessage);
