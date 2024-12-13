import { ApiProperty } from "@nestjs/swagger";
import { IsNotEmpty, IsOptional } from "class-validator";

export class GroupMessageDto {
    @IsNotEmpty({ message: "Group's id is not empty." })
    @ApiProperty({ description: 'Group Id' })
    id: string;

    @IsOptional()
    @ApiProperty({ description: 'Content message.' })
    content: string;
}
