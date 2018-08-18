package com.zsf.util.websocket;
/**
* 握手（handshake）接口
* */
import com.zsf.dao.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.Cookie;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    @Autowired
    private RedisDao redisDao;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        System.out.println("Before Handshake");
        /*HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        //String token = request.getHeader("token");
        if (token != null) {
            String value = redisDao.getValue(token);
            if (value != null) {
                redisDao.setExpired(token, value, 60, TimeUnit.MINUTES);
                return super.beforeHandshake(request, response, wsHandler, attributes);
            }
        }
        return false;*/

        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        System.out.println("After Handshake");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
