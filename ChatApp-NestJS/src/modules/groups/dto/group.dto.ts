import { ApiProperty } from "@nestjs/swagger";
import { IsNotEmpty } from "class-validator";

export class GroupDto {
    @IsNotEmpty({ message: "Group's name is mandatory." })
    @ApiProperty({ description: 'The name of the group.', example: 'Test Group' })
    groupName: string;

    @IsNotEmpty({ message: "Member's id is mandatory." })
    @ApiProperty({ description: 'List ids of members.' })
    memberIds: string[];
}
