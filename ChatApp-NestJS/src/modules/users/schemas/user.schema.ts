import { Prop, Schema, SchemaFactory } from "@nestjs/mongoose";

@Schema({ timestamps: true })
export class User {
    @Prop({ required: true })
    name: string;

    @Prop({ required: true })
    email: string;

    @Prop({ required: true })
    password: string;

    @Prop({ maxlength: 10, minlength: 10 })
    phone: string;

    @Prop()
    imageUrl: string;

    @Prop()
    imageId: string;

    @Prop({ default: "LOCAL" })
    accountType: string;

    @Prop()
    refreshToken: string;
}

export const UserSchema = SchemaFactory.createForClass(User);
