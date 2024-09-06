package com.netty;

import com.netty.pipeline.NettyClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * MyClient
 * 模拟设备发送数据
 *
 * @author TONE
 * @date 2024/6/11
 */
@Slf4j
@Component
public class MyClient {
    private static final EventLoopGroup WORKER_GROUP = new NioEventLoopGroup();
    // todo 使用的是Server的地址和端口，可以自行把参数改成配置
    private String host = "192.168.0.109";
    private int port = 9101;

    public void run() throws InterruptedException {
        try {
            Bootstrap b = new Bootstrap();
            b.group(WORKER_GROUP)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new NettyClientInitializer());
//                    .handler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel ch) {
//                            ch.pipeline().addLast(new StringDecoder());
//                            ch.pipeline().addLast(new StringEncoder());
//                            ch.pipeline().addLast(new MyClientHandler());
//                        }
//                    });
            // 启动客户端
            ChannelFuture f = b.connect(host, port).sync();
            int i = 0;
            while (true) {
                i++;
                String msg = "你好啊，这是我们第" + i + "次见面";
                f.channel().writeAndFlush(msg);
                log.info("第 {} 次发送数据", i);
                // 间隔一下继续发送数据
                Thread.sleep(5000);
            }
        } finally {
            WORKER_GROUP.shutdownGracefully();
        }
    }

    /**
     * 关闭客户端方法
     */
    @PreDestroy
    public void close() {
        log.info("关闭客户端....");
        WORKER_GROUP.shutdownGracefully();
    }

}
