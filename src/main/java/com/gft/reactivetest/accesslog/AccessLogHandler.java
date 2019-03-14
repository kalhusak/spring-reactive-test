package com.gft.reactivetest.accesslog;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import org.springframework.http.HttpStatus;

import java.net.URI;

/**
 * Access logger executed in netty channel pipeline.
 */
public class AccessLogHandler extends ChannelDuplexHandler {

    private AccessLog accessLog;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            final HttpRequest request = (HttpRequest) msg;
            accessLog = new AccessLog(System.currentTimeMillis())
                    .remoteAddress(((SocketChannel) ctx.channel()).remoteAddress())
                    .method(request.method().name())
                    .uri(URI.create(request.uri()));
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        if (msg instanceof HttpResponse) {
            final HttpResponse response = (HttpResponse) msg;
            HttpResponseStatus status = response.status();
            accessLog.status(HttpStatus.valueOf(status.code()));
            if (status.equals(HttpResponseStatus.CONTINUE)) {
                ctx.write(msg, promise);
                return;
            }
        }

        if (msg instanceof LastHttpContent) {
            ctx.write(msg, promise).addListener(future -> {
                if (future.isSuccess()) {
                    accessLog.log();
                }
            });
            return;
        }

        ctx.write(msg, promise);
    }

}