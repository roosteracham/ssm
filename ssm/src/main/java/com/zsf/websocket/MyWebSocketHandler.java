package com.zsf.websocket;

import org.springframework.web.socket.*;

import java.io.IOException;


/**
 * websocket处理器：功能实现的核心代码编写类
 */
public class MyWebSocketHandler implements WebSocketHandler {

    //定义一个全局的初始化值count=0
    private static int i = 0;

    private Thread thread;

    //建立连接后的操作
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        System.out.println("已建立连接..");

    }

    //消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
    public void handleMessage(WebSocketSession session,
                              WebSocketMessage<?> message) throws Exception {
        String receivedMessage = message.getPayload().toString();

        System.out.println(receivedMessage);
        sendMessages(session, receivedMessage);
        thread.start();
    }

    private void sendMessages(WebSocketSession session, String receivedMessage) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                String[] strings = receivedMessage.split(",");
                while (!"close".equals(receivedMessage)) {

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

                    try {
                    session.sendMessage(new TextMessage(stringBuilder.toString()));
                    Thread.sleep(1000);
                    }catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //消息传输错误处理
    public void handleTransportError(WebSocketSession session,
                                     Throwable exception) throws Exception {
        System.out.println("消息传输错误..");
        thread.stop();
    }

    //连接关闭后的操作
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus closeStatus) throws Exception {
        System.out.println("连接关闭后的操作..");
        thread.stop();
    }

    public boolean supportsPartialMessages() {
        return false;
    }
}
