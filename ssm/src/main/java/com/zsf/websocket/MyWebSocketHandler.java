package com.zsf.websocket;

import org.springframework.web.socket.*;


/**
 * websocket处理器：功能实现的核心代码编写类
 */
public class MyWebSocketHandler implements WebSocketHandler {

    //定义一个全局的初始化值count=0
    private static int i = 0;

    private String receivedMessage;
    //建立连接后的操作
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        System.out.println("建立连接.. : " + receivedMessage);

        String[] strings = receivedMessage.split(",");
        while (i < 10) {

            StringBuilder stringBuilder = new StringBuilder("{");

            for (int j = 0; j < strings.length; j++) {
                stringBuilder.append("\"")
                        .append(strings[j])
                        .append("\":\"")
                        .append((int)(Math.random() * 50));
                if (j == strings.length - 1) {
                    stringBuilder.append("\"");
                } else {
                    stringBuilder.append("\",");
                }
            }
            stringBuilder.append("}");

            session.sendMessage(new TextMessage(stringBuilder.toString()));
            if (i == 9) {
                i = 0;
            }
            Thread.sleep(1000);
        }
    }

    //消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
    public void handleMessage(WebSocketSession session,
                              WebSocketMessage<?> message) throws Exception {
        receivedMessage = message.getPayload().toString();
        System.out.println(receivedMessage);
    }

    //消息传输错误处理
    public void handleTransportError(WebSocketSession session,
                                     Throwable exception) throws Exception {
        System.out.println("消息传输错误..");
    }

    //连接关闭后的操作
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus closeStatus) throws Exception {
        System.out.println("连接关闭后的操作..");
    }

    public boolean supportsPartialMessages() {
        return false;
    }

    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("'a' : ").append("'b'");
        System.out.println(stringBuilder.toString());
    }
}
