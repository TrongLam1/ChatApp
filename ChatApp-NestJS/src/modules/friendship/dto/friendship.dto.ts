import { IsNotEmpty } from "class-validator";

export class FriendshipDto {
    @IsNotEmpty()
    friendId: string;
}
