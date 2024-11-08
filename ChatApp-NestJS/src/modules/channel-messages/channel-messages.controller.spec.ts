import { Test, TestingModule } from '@nestjs/testing';
import { ChannelMessagesController } from './channel-messages.controller';
import { ChannelMessagesService } from './channel-messages.service';

describe('ChannelMessagesController', () => {
  let controller: ChannelMessagesController;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [ChannelMessagesController],
      providers: [ChannelMessagesService],
    }).compile();

    controller = module.get<ChannelMessagesController>(ChannelMessagesController);
  });

  it('should be defined', () => {
    expect(controller).toBeDefined();
  });
});
