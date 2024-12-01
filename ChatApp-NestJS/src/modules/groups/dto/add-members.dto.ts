import { IsNotEmpty } from "class-validator";

export class AddMembersDto {
    @IsNotEmpty({ message: "Group's id is mandatory." })
    groupId: string;

    @IsNotEmpty({ message: "Member's id is mandatory." })
    memberIds: string[];
}
