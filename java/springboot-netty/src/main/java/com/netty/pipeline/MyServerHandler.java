package com.netty.pipeline;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * MyServerHandler
 *
 * @author TONE
 * @date 2024/6/11
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MyServerHandler extends SimpleChannelInboundHandler<Object> {
    /**
     * 不活跃次数
     */
    private int lossConnectCount = 0;

    /**
     * 建立连接时
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.fireChannelActive();
        log.info("MyServerHandler ==== 建立连接时：{} 连接的客户端地址:{}", new Date(), ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object obj) {
        Channel incoming = ctx.channel();
        log.info("Client -> Server remoteAddress:{} obj:{}", incoming.remoteAddress(), obj.toString());
        // todo 业务处理，和消息的过滤
        ctx.writeAndFlush(obj);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        log.info("{} -> [已经有 5 秒中没有接收到客户端的消息了]", this.getClass().getName());
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                lossConnectCount++;
                if (lossConnectCount > 2) {
                    log.info("{} -> [释放不活跃通道] channelId:{}", this.getClass().getName(), ctx.channel().id());
                    ctx.channel().close();
                }
            }
        } else {
            ctx.fireUserEventTriggered(evt);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel incoming = ctx.channel();
        // 当出现异常就关闭连接
        log.info("通道异常,关闭连接:{}", incoming.read(), cause);
        ctx.close();
    }

}
