import { Prop, Schema, SchemaFactory } from "@nestjs/mongoose";
import mongoose from "mongoose";
import { User } from "src/modules/users/schemas/user.schema";

@Schema({ timestamps: true })
export class Group {
    @Prop({ required: true })
    groupName: string;

    @Prop({ type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true })
    admin: User;

    @Prop({ default: true })
    isAvailable: boolean;
}

export const GroupSchema = SchemaFactory.createForClass(Group);