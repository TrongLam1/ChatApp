import { Body, Controller, Get, Param, Post, Put, Req, UploadedFile, UseGuards, UseInterceptors } from '@nestjs/common';
import { FileInterceptor } from '@nestjs/platform-express';
import { JwtAuthGuard } from 'src/auth/guard/jwt-auth.guard';
import { Public } from 'src/decorator/decorator';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
import { UsersService } from './users.service';

@Controller('users')
export class UsersController {
  constructor(private readonly usersService: UsersService) { }

  @Post()
  @Public()
  async create(@Body() createUserDto: CreateUserDto) {
    return await this.usersService.register(createUserDto);
  }

  @Put('update-profile')
  @UseGuards(JwtAuthGuard)
  async updateProfile(@Req() req, @Body() updateUserDto: UpdateUserDto) {
    return await this.usersService.updateUser(req, updateUserDto);
  }

  @Put('change-avatar')
  @UseGuards(JwtAuthGuard)
  @UseInterceptors(FileInterceptor('file'))
  async changeAvatar(@Req() req, @UploadedFile() file: Express.Multer.File) {
    return await this.usersService.changeAvatar(req, file);
  }

  @Get()
  @UseGuards(JwtAuthGuard)
  async getProfile(@Req() req) {
    return await this.usersService.getProfile(req);
  }

  @Get('name/:name')
  @Public()
  @UseGuards(JwtAuthGuard)
  async findName(@Param('name') name: string) {
    return await this.usersService.findUsersByName(name);
  }
}
