import { ApiProperty } from "@nestjs/swagger";
import { IsNotEmpty, IsOptional } from "class-validator";

export class ChannelMessageDto {
    @IsNotEmpty({ message: "ChannelId is not empty." })
    @ApiProperty({ description: 'Channel Id' })
    id: string;

    @IsOptional()
    @ApiProperty({ description: 'Content message.' })
    content: string;
}
