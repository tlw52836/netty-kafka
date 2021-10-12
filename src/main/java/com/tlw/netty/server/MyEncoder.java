package com.tlw.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @ClassName NettyMessageEncoder
 * @Deacription TODO 自定义发送消息格式  发送16进制
 * @Author LiuDaGang
 * @Date 2021/3/11 19:19
 * @Version 1.0
 **/
public class MyEncoder  extends MessageToByteEncoder<String> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf byteBuf) throws Exception {
        //将16进制字符串转为数组
        byteBuf.writeBytes(hexString2Bytes(s));
    }

    /**
     * @Title:hexString2Bytes
     * @Description:16进制字符串转字节数组
     * @param src 16进制字符串
     * @return 字节数组
     */

    public static byte[] hexString2Bytes(String src) {

        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }
}