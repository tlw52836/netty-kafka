package com.tlw.netty.server;

import com.tlw.netty.handler.NettyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author Gjing
 *
 * netty服务初始化器
 **/
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        //发送消息格式
        socketChannel.pipeline().addLast(new MyEncoder());
        //接受消息格式
        socketChannel.pipeline().addLast(new MyDecoder());

        socketChannel.pipeline().addLast(new NettyServerHandler());
    }
}
