package com.mengzhidu.netty.demo.demo11111;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 服务端入口类
 */
public class Server {
    public static void main(String[] args) throws Exception{
        final ServerHandler handler = new ServerHandler();

        // 创建事件循环组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    // 指定所使用的nio传输channel
                    .channel(NioServerSocketChannel.class)
                    // 指定本地监听的地址
                    .localAddress(new InetSocketAddress(8080))
                    // 添加一个handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(handler);
                        }
                    });

            // 异步的绑定服务器，我们调用sync()方法来执行同步，直到绑定完成
            ChannelFuture future = bootstrap.bind().sync();
            // 获取该Channel的CloseFuture，并且阻塞当前线程直到它完成
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
