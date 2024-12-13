import { ApiProperty } from "@nestjs/swagger";
import { IsEmail, IsNotEmpty, IsOptional, Length } from "class-validator";

export class CreateUserDto {
    @IsNotEmpty({ message: "Tên không được để trống." })
    @ApiProperty({ description: 'The name of the user', example: 'John Doe' })
    username: string;

    @IsNotEmpty({ message: "Email không được để trống." })
    @IsEmail({}, { message: "Email không hợp lệ." })
    @ApiProperty({ description: 'The email of the user', example: 'doe@gmail.com' })
    email: string;

    @IsNotEmpty({ message: "Mật khẩu không được để trống." })
    @Length(7)
    @ApiProperty({ description: 'The password of the user', example: '12345678' })
    password: string;

    @IsOptional()
    provider: string = null;
}
