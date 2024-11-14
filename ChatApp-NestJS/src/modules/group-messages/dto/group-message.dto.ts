import { IsNotEmpty, IsOptional } from "class-validator";

export class GroupMessageDto {
    @IsNotEmpty({ message: "Group's id is not empty." })
    groupId: string;

    @IsNotEmpty({ message: "Sender's id is not empty." })
    memberId: string;

    @IsOptional()
    content: string;
}
