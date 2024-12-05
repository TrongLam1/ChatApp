import { IsNotEmpty, IsOptional } from "class-validator";

export class GroupMessageDto {
    @IsNotEmpty({ message: "Group's id is not empty." })
    id: string;

    @IsOptional()
    content: string;
}
