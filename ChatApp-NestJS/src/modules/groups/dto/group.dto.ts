import { IsNotEmpty } from "class-validator";

export class GroupDto {
    @IsNotEmpty({ message: "Group's name is mandatory." })
    groupName: string;

    @IsNotEmpty({ message: "Member's id is mandatory." })
    memberIds: string[];
}
