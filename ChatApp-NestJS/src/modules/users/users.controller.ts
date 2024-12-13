import { Body, Controller, Get, Param, Post, Put, Req, UploadedFile, UseGuards, UseInterceptors } from '@nestjs/common';
import { FileInterceptor } from '@nestjs/platform-express';
import { JwtAuthGuard } from 'src/auth/guard/jwt-auth.guard';
import { Public } from 'src/decorator/decorator';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
import { UsersService } from './users.service';
import { ApiOperation, ApiResponse } from '@nestjs/swagger';

@Controller('users')
export class UsersController {
  constructor(private readonly usersService: UsersService) { }

  @Post()
  @Public()
  @ApiOperation({ summary: 'Create new user' })
  @ApiResponse({ status: 201, description: 'Create new user successfully.' })
  async create(@Body() createUserDto: CreateUserDto) {
    return await this.usersService.register(createUserDto);
  }

  @Put('update-profile')
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Update profile user' })
  @ApiResponse({ status: 200, description: 'Update profile user successfully.' })
  async updateProfile(@Req() req, @Body() updateUserDto: UpdateUserDto) {
    return await this.usersService.updateUser(req, updateUserDto);
  }

  @Put('change-avatar')
  @UseGuards(JwtAuthGuard)
  @UseInterceptors(FileInterceptor('file'))
  @ApiOperation({ summary: 'Update avatar user' })
  @ApiResponse({ status: 200, description: 'Update avatar user successfully.' })
  async changeAvatar(@Req() req, @UploadedFile() file) {
    return await this.usersService.changeAvatar(req, file);
  }

  @Get()
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Get information user' })
  @ApiResponse({ status: 200, description: 'Information user.' })
  async getProfile(@Req() req) {
    return await this.usersService.getProfile(req);
  }

  @Get('name/:name')
  @Public()
  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Find user by name' })
  @ApiResponse({ status: 200, description: 'List of users.' })
  async findName(@Param('name') name: string) {
    return await this.usersService.findUsersByName(name);
  }
}
