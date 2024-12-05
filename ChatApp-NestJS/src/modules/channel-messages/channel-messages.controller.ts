import { Body, Controller, Get, Param, Post, Req, UploadedFile, UseGuards, UseInterceptors } from '@nestjs/common';
import { JwtAuthGuard } from 'src/auth/guard/jwt-auth.guard';
import { ChannelMessagesService } from './channel-messages.service';
import { ChannelMessageDto } from './dto/channel-message.dto';
import { FileInterceptor } from '@nestjs/platform-express';

@Controller('channel-messages')
export class ChannelMessagesController {
  constructor(private readonly channelMessagesService: ChannelMessagesService) { }

  @Post('post-message')
  @UseGuards(JwtAuthGuard)
  async postMessage(@Req() req, @Body() channelMessageDto: ChannelMessageDto) {
    return await this.channelMessagesService.postMessage(req, channelMessageDto);
  }

  @Post('post-image')
  @UseGuards(JwtAuthGuard)
  @UseInterceptors(FileInterceptor('file'))
  async postImage(
    @Req() req,
    @Body() channelMessageDto: ChannelMessageDto,
    @UploadedFile() file: Express.Multer.File) {
    return await this.channelMessagesService.postImage(req, channelMessageDto, file);
  }

  @Get('get-messages/:channelId')
  @UseGuards(JwtAuthGuard)
  async getMessages(@Param('channelId') channelId: string) {
    return await this.channelMessagesService.getMessages(channelId);
  }
}
