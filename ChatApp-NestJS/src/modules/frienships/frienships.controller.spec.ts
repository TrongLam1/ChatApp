import { Test, TestingModule } from '@nestjs/testing';
import { FrienshipsController } from './frienships.controller';
import { FrienshipsService } from './frienships.service';

describe('FrienshipsController', () => {
  let controller: FrienshipsController;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [FrienshipsController],
      providers: [FrienshipsService],
    }).compile();

    controller = module.get<FrienshipsController>(FrienshipsController);
  });

  it('should be defined', () => {
    expect(controller).toBeDefined();
  });
});
