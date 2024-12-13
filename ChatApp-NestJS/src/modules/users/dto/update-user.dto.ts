import { ApiProperty } from "@nestjs/swagger";
import { IsOptional } from "class-validator";

export class UpdateUserDto {
    @IsOptional()
    @ApiProperty({ description: 'The name of the user. Not require.', example: 'John' })
    username: string;

    @IsOptional()
    @ApiProperty({ description: 'The password of the user. Not require.', example: '12345678' })
    password: string;

    @IsOptional()
    @ApiProperty({ description: 'The phone of the user. Not require.', example: '1234567890' })
    phone: string;
}
