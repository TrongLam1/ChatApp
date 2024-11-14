import { Controller } from '@nestjs/common';
import { GroupMessagesService } from './group-messages.service';

@Controller('group-messages')
export class GroupMessagesController {
  constructor(private readonly groupMessagesService: GroupMessagesService) { }
}
