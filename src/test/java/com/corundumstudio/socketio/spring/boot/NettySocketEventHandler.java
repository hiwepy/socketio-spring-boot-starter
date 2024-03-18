package com.corundumstudio.socketio.spring.boot;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.corundumstudio.socketio.spring.boot.handler.AbstractSocketEventHandler;
import org.springframework.stereotype.Component;

@Component
public class NettySocketEventHandler extends AbstractSocketEventHandler {


    // 消息接收入口，当接收到消息后，查找发送目标客户端，并且向该客户端发送消息，且给自己发送消息
    // 暂未使用
    @OnEvent("message")
    public void onEvent(SocketIOClient client, AckRequest ackRequest, String data) {
        System.out.println("接收到消息：" + data);
        client.sendEvent("message", "你好，客户端");
    }

}
