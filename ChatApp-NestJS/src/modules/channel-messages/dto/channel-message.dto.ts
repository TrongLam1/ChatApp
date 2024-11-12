import { IsNotEmpty, IsOptional } from "class-validator";

export class ChannelMessageDto {
    @IsNotEmpty({ message: "FriendId is not empty." })
    friendId: string;

    @IsOptional()
    content: string;
}
