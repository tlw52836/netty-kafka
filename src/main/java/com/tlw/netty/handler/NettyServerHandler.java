package com.tlw.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PostConstruct;

/**
 * @author Gjing
 *
 * netty服务端处理器
 **/
@Slf4j
@Component
public class NettyServerHandler extends SimpleChannelInboundHandler {

    @Autowired
    private KafkaTemplate<Integer, Object> kafkaTemplate;
    public static NettyServerHandler nettyServerHandler;


    /**
     * 直接用Autowired注入kafkaTemplate会报空指针的错误，需要添加以下步骤才不报错
     */
    @PostConstruct
    public void init() {
        nettyServerHandler = this;
        nettyServerHandler.kafkaTemplate = this.kafkaTemplate;
    }


    /**
     * 客户端连接成功后触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Channel active......");
    }

    /**
     * 每次接收到客户端传来的数据后触发
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("服务器收到消息: {}", msg.toString());
        nettyServerHandler.kafkaTemplate.send("topic1",msg.toString()).addCallback(new ListenableFutureCallback<SendResult<Integer, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("发送消息失败：" + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<Integer, Object> result) {
                log.info("发送消息成功：" + result.getRecordMetadata().topic() + "-"
                        + result.getRecordMetadata().partition() + "-" + result.getRecordMetadata().offset());
            }
        });

        //analysis(msg.toString());
        ctx.write("68265416");
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Channel close......");
    }

    /**
     * 发生异常后触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public void analysis(String s){
        log.info("帧头：" + s.substring(0,2));
        log.info("帧长度：" + s.substring(2,6));
        log.info("协议版本：" + s.substring(6,12));
        log.info("地址：" + s.substring(12,28));
        log.info("报文生成时间：" + s.substring(28,44));
        log.info("控制码：" + s.substring(44,46));
        log.info("帧序号：" + s.substring(46,50));
        log.info("备用字段：" + s.substring(50,54));
        log.info("命令码：" + s.substring(54,56));
        log.info("用户数据长度：" + s.substring(56,60));
        log.info("用户数据：" + s.substring(60,60+Integer.valueOf(s.substring(56,60),16)*2));
        log.info("校验和：" + s.substring(s.length()-4,s.length()-2));
        log.info("帧尾：" + s.substring(s.length()-2));
    }
}
