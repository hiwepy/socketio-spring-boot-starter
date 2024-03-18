package com.corundumstudio.socketio.spring.boot;

import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import com.corundumstudio.socketio.spring.boot.push.SocketMessage;
import com.corundumstudio.socketio.store.pubsub.DispatchMessage;
import com.corundumstudio.socketio.store.pubsub.PubSubStore;
import com.corundumstudio.socketio.store.pubsub.PubSubType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@Slf4j
public class SocketPushController {

    @Autowired
    private PubSubStore pubSubStore;

    @PostMapping("/push")
    public String push(@RequestBody SocketMessage<String> socketMessage) {
        Packet packet = new Packet(PacketType.MESSAGE);
        packet.setSubType(PacketType.EVENT);
        packet.setName(socketMessage.getEventName());
        packet.setData(Collections.singletonList(socketMessage.getMessage()));
        DispatchMessage dispatchMessage = new DispatchMessage(socketMessage.getRoom(), packet, socketMessage.getNamespace());
        pubSubStore.publish(PubSubType.DISPATCH, dispatchMessage);
        return "ok";
    }

}

