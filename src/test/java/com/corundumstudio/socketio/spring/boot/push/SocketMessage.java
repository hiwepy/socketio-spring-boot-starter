package com.corundumstudio.socketio.spring.boot.push;


import lombok.Data;

@Data
public class SocketMessage<T> {
    private String room;
    private String namespace;
    private String fromUserId;
    private String toUserId;
    private String eventName;
    private T message;
}
