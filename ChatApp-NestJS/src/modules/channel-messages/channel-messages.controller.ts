import { Body, Controller, Get, Param, Post, Req, UploadedFile, UseGuards, UseInterceptors } from '@nestjs/common';
import { FileInterceptor } from '@nestjs/platform-express';
import { ApiOperation } from '@nestjs/swagger';
import { JwtAuthGuard } from 'src/auth/guard/jwt-auth.guard';
import { ChannelMessagesService } from './channel-messages.service';
import { ChannelMessageDto } from './dto/channel-message.dto';

@Controller('channel-messages')
export class ChannelMessagesController {
  constructor(private readonly channelMessagesService: ChannelMessagesService) { }

  @Post('post-message')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Send message to channel.' })
  async postMessage(@Req() req, @Body() channelMessageDto: ChannelMessageDto) {
    return await this.channelMessagesService.postMessage(req, channelMessageDto);
  }

  @Post('post-image')
  @UseGuards(JwtAuthGuard)
  @UseInterceptors(FileInterceptor('file'))
  @ApiOperation({ summary: 'Send image to channel.' })
  async postImage(
    @Req() req,
    @Body() channelMessageDto: ChannelMessageDto,
    @UploadedFile() file: Express.Multer.File) {
    return await this.channelMessagesService.postImage(req, channelMessageDto, file);
  }

  @Get('get-messages/:channelId')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Get list messages of channel.' })
  async getMessages(@Param('channelId') channelId: string) {
    return await this.channelMessagesService.getMessages(channelId);
  }
}
