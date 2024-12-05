import { ConnectedSocket, MessageBody, SubscribeMessage, WebSocketGateway, WebSocketServer } from '@nestjs/websockets';
import { Server, Socket } from 'socket.io';

@WebSocketGateway(3001, { cors: "*" })
export class RealTimeGateway {

    @WebSocketServer()
    server: Server;

    @SubscribeMessage('joinChat')
    handleJoinChannel(
        @ConnectedSocket() client: Socket,
        @MessageBody() subscribeId: string
    ) {
        client.join(subscribeId);
    }

    handleSendMessage(newMessage: any, subscribeId: string) {
        this.server.to(subscribeId).emit('newMessage', newMessage);
    }
}
