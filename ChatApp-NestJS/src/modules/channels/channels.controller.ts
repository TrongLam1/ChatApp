import { Controller, Get, Param, Req, UseGuards } from '@nestjs/common';
import { JwtAuthGuard } from 'src/auth/guard/jwt-auth.guard';
import { ChannelsService } from './channels.service';
import { ApiOperation } from '@nestjs/swagger';

@Controller('channels')
export class ChannelsController {
  constructor(private readonly channelsService: ChannelsService) { }

  @Get('find-channel/:friendId')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Find channel by id.' })
  async findChannel(@Req() req, @Param('friendId') friendId: string) {
    return await this.channelsService.findChannel(req, friendId);
  }
}
