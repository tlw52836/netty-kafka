package com.tlw;

import com.tlw.netty.server.TcpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;

@SpringBootApplication
public class NettyKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyKafkaApplication.class, args);
        TcpServer nettyServer = new TcpServer();
        nettyServer.start(new InetSocketAddress("127.0.0.1", 8090));
    }

}
