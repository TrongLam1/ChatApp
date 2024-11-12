import { IsNotEmpty } from "class-validator";

export class ChannelDto {
    @IsNotEmpty({ message: "FriendId is mandatory." })
    friendId: string;
}
