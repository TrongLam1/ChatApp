import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { AuthModule } from './auth/auth.module';
import { MongooseModule } from '@nestjs/mongoose';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { UsersModule } from './modules/users/users.module';
import { APP_GUARD, APP_INTERCEPTOR } from '@nestjs/core';
import { JwtAuthGuard } from './auth/guard/jwt-auth.guard';
import { CloudinaryModule } from './cloudinary/cloudinary.module';
import { FriendshipModule } from './modules/friendship/friendship.module';
import { ChannelsModule } from './modules/channels/channels.module';
import { ChannelMessagesModule } from './modules/channel-messages/channel-messages.module';
import { GroupsModule } from './modules/groups/groups.module';
import { GroupMembersModule } from './modules/group-members/group-members.module';
import { GroupMessagesModule } from './modules/group-messages/group-messages.module';
import { TransformInterceptor } from './interceptor/response';

@Module({
  imports: [
    AuthModule,
    UsersModule,
    FriendshipModule,
    ChannelsModule,
    ChannelMessagesModule,
    GroupsModule,
    GroupMembersModule,
    GroupMessagesModule,
    CloudinaryModule,
    ConfigModule.forRoot({ isGlobal: true }),
    MongooseModule.forRootAsync({
      imports: [ConfigModule],
      useFactory: async (configService: ConfigService) => ({
        uri: configService.get<string>('MONGODB_URI'),
      }),
      inject: [ConfigService],
    }),
  ],
  controllers: [AppController],
  providers: [
    AppService,
    {
      provide: APP_GUARD,
      useClass: JwtAuthGuard,
    },
    {
      provide: APP_INTERCEPTOR,
      useClass: TransformInterceptor,
    },
  ],
})
export class AppModule { }
