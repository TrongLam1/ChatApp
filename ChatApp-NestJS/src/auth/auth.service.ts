import { Injectable } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { comparePasswordHelper } from 'src/helpers/utils';
import { UsersService } from 'src/modules/users/users.service';

@Injectable()
export class AuthService {
  constructor(
    private readonly userService: UsersService,
    private readonly jwtService: JwtService
  ) { }

  async validateUser(email: string, password: string) {
    const user = await this.userService.findOneByEmail(email);
    if (!user) return null;

    const validPassword = await comparePasswordHelper(password, user.password);
    if (!validPassword) return null;

    return user;
  }

  // async validateGoogleUser(googleUser: CreateUserDto) {
  //   const user = await this.userService.findOneByEmail(googleUser.email);
  //   if (user) return user;
  //   return await this.userService.register(googleUser);
  // }

  async signIn(user): Promise<any> {
    const payload = {
      id: user._id.toString(), email: user.email, username: user.name, avatar: user.imageUrl
    };
    const refreshToken = this.jwtService.sign(payload,
      {
        secret: process.env.REFRESH_JWT_SECRET_KEY,
        expiresIn: '30d'
      })
    await this.userService.saveRefreshToken(user, refreshToken);

    return {
      user: {
        id: user._id.toString(),
        email: user.email,
        username: user.name,
        phone: user.phone,
        avatar: user.imageUrl
      },
      access_token: this.jwtService.sign(payload, { expiresIn: '1d' }),
      refreshToken
    };
  }
}
