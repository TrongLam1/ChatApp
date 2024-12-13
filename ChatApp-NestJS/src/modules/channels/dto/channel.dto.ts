import { ApiProperty } from "@nestjs/swagger";
import { IsNotEmpty } from "class-validator";

export class ChannelDto {
    @IsNotEmpty({ message: "FriendId is mandatory." })
    @ApiProperty({ description: 'Friend Id chat with' })
    friendId: string;
}
