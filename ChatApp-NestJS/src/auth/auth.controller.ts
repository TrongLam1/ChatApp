import { Body, Controller, Get, Post, Req, Res, UseGuards } from '@nestjs/common';
import { Public, ResponseMessage } from 'src/decorator/decorator';
import { AuthService } from './auth.service';
import { LocalAuthGuard } from './guard/local-auth.guard';
import { CreateUserDto } from 'src/modules/users/dto/create-user.dto';
import { UsersService } from 'src/modules/users/users.service';
import { JwtAuthGuard } from './guard/jwt-auth.guard';
import { ApiOperation, ApiResponse } from '@nestjs/swagger';
import { GoogleAuthGuard } from './guard/google-auth.guard';

@Controller('auth')
export class AuthController {
  constructor(
    private readonly authService: AuthService,
    private readonly userService: UsersService
  ) { }

  @Post('register')
  @ResponseMessage("Register account")
  @Public()
  @ApiOperation({ summary: 'Register account' })
  @ApiResponse({ status: 201, description: 'Register account successfully.' })
  async register(@Body() registerDTO: CreateUserDto) {
    return await this.userService.register(registerDTO);
  }

  @Post('login')
  @UseGuards(LocalAuthGuard)
  @Public()
  @ResponseMessage("Fetch login")
  @ApiOperation({ summary: 'Login account' })
  @ApiResponse({ status: 200, description: 'Login account.' })
  async login(@Req() req) {
    return await this.authService.signIn(req.user);
  }

  @Get('logout')
  @UseGuards(JwtAuthGuard)
  @ResponseMessage("Logout")
  @ApiOperation({ summary: 'Logout account' })
  @ApiResponse({ status: 200, description: 'Logout account.' })
  async logout(@Req() req) {
    return await this.userService.logout(req.user);
  }

  @Get('google/login')
  @Public()
  @UseGuards(GoogleAuthGuard)
  googleLogin() { }

  @Get('google/callback')
  @Public()
  @UseGuards(GoogleAuthGuard)
  async googleCallBack(@Req() req, @Res() res) {
    const response = await this.authService.signIn(req.user);
    const string = JSON.stringify(response);
    res.redirect(`http://localhost:3000/login?response=${string}`);
  }
}
