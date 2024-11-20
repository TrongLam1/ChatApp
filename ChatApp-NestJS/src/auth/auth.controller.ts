import { Body, Controller, Get, Post, Req, UseGuards } from '@nestjs/common';
import { Public, ResponseMessage } from 'src/decorator/decorator';
import { AuthService } from './auth.service';
import { LocalAuthGuard } from './guard/local-auth.guard';
import { CreateUserDto } from 'src/modules/users/dto/create-user.dto';
import { UsersService } from 'src/modules/users/users.service';
import { JwtAuthGuard } from './guard/jwt-auth.guard';

@Controller('auth')
export class AuthController {
  constructor(
    private readonly authService: AuthService,
    private readonly userService: UsersService
  ) { }

  @Post('register')
  @ResponseMessage("Register account")
  @Public()
  async register(@Body() registerDTO: CreateUserDto) {
    return await this.userService.register(registerDTO);
  }

  @Post('login')
  @UseGuards(LocalAuthGuard)
  @Public()
  @ResponseMessage("Fetch login")
  async login(@Req() req) {
    return await this.authService.signIn(req.user);
  }

  @Get('logout')
  @UseGuards(JwtAuthGuard)
  @ResponseMessage("Logout")
  async logout(@Req() req) {
    return await this.userService.logout(req.user);
  }
}
