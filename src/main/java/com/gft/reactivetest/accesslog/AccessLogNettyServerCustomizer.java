package com.gft.reactivetest.accesslog;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import reactor.netty.http.server.HttpServer;

public class AccessLogNettyServerCustomizer implements NettyServerCustomizer {

    public static final String HANDLER_NAME = "accessLogHandler";
    public static final String BASE_HANDLER_NAME = "reactor.left.httpCodec";


    @Override
    public HttpServer apply(HttpServer httpServer) {
        return httpServer
                .tcpConfiguration(tcpServer ->
                        tcpServer.bootstrap(bootstrap -> bootstrap.childHandler(new AccessLogChannelInitializer())));
    }

    private class AccessLogChannelInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
//            channel
//                    .pipeline()
//                    .addAfter(BASE_HANDLER_NAME, HANDLER_NAME, new AccessLogHandler());
        }
    }
}