import { ApiProperty } from "@nestjs/swagger";
import { IsNotEmpty } from "class-validator";

export class FriendshipDto {
    @IsNotEmpty()
    @ApiProperty({ description: 'Friend Id' })
    friendId: string;
}
