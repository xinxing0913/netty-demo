package com.mengzhidu.netty.demo.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * Created by xinguimeng on 2018/4/10
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            // 向其他组里的所有Channel发一份数据
            group.writeAndFlush(new TextWebSocketFrame("clent " + ctx.channel() + "joined"));
            // 把当前Channel加入到ChannelGroup中
            group.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx,evt);
        }
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        System.out.println("收到客户端的消息:" + textWebSocketFrame.retain().text());
        group.writeAndFlush(new TextWebSocketFrame("服务端收到:" + textWebSocketFrame.retain().text()));
    }
}
