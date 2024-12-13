import { ApiProperty } from "@nestjs/swagger";
import { IsNotEmpty } from "class-validator";

export class AddMembersDto {
    @IsNotEmpty({ message: "Group's id is mandatory." })
    @ApiProperty({ description: 'Id group.' })
    groupId: string;

    @IsNotEmpty({ message: "Member's id is mandatory." })
    @ApiProperty({ description: 'List ids of members.' })
    memberIds: string[];
}
