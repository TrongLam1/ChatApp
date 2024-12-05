import { Body, Controller, Get, Param, Post, Req, UploadedFile, UseGuards, UseInterceptors } from '@nestjs/common';
import { GroupMessagesService } from './group-messages.service';
import { JwtAuthGuard } from 'src/auth/guard/jwt-auth.guard';
import { FileInterceptor } from '@nestjs/platform-express';
import { GroupMessageDto } from './dto/group-message.dto';

@Controller('group-messages')
export class GroupMessagesController {
  constructor(private readonly groupMessagesService: GroupMessagesService) { }

  @Post('post-message')
  @UseGuards(JwtAuthGuard)
  async postMessage(@Req() req, @Body() groupMessageDto: GroupMessageDto) {
    return await this.groupMessagesService.postMessage(req, groupMessageDto);
  }

  @Post('post-image')
  @UseGuards(JwtAuthGuard)
  @UseInterceptors(FileInterceptor('file'))
  async postImage(
    @Req() req,
    @Body() groupMessageDto: GroupMessageDto,
    @UploadedFile() file: Express.Multer.File) {
    return await this.groupMessagesService.postImage(req, groupMessageDto, file);
  }

  @Get('get-messages/:groupId')
  @UseGuards(JwtAuthGuard)
  async getMessages(@Param('groupId') groupId: string) {
    return await this.groupMessagesService.getMessages(groupId);
  }
}
