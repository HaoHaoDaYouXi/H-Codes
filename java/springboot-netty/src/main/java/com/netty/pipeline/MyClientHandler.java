package com.netty.pipeline;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * MyClientHandler
 *
 * @author TONE
 * @date 2024/6/11
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MyClientHandler extends SimpleChannelInboundHandler<Object> {
    /**
     * 建立连接时
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.fireChannelActive();
        log.info("MyClientHandler ==== 建立连接时：{} 连接的客户端地址:{}", new Date(), ctx.channel().remoteAddress());
    }

    /**
     * 接受消息
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param obj the message to handle
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj) {
        Channel incoming = ctx.channel();
        log.info("Server -> Client remoteAddress:{} obj:{}", incoming.remoteAddress(), obj.toString());
//        ctx.writeAndFlush(obj);
    }
}
