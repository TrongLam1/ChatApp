import { Controller, Post, Req, UseGuards } from '@nestjs/common';
import { Public, ResponseMessage } from 'src/decorator/decorator';
import { AuthService } from './auth.service';
import { LocalAuthGuard } from './guard/local-auth.guard';

@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) { }

  @Post('login')
  @UseGuards(LocalAuthGuard)
  @Public()
  @ResponseMessage("Fetch login")
  async login(@Req() req) {
    return await this.authService.signIn(req.user);
  }
}
