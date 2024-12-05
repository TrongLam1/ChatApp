import { IsNotEmpty, IsOptional } from "class-validator";

export class ChannelMessageDto {
    @IsNotEmpty({ message: "ChannelId is not empty." })
    id: string;

    @IsOptional()
    content: string;
}
