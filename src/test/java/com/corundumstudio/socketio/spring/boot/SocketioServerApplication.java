package com.corundumstudio.socketio.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocketioServerApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SocketioServerApplication.class, args);
    }

}
