package com.zsf.websocket;

import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * websocket处理器：功能实现的核心代码编写类
 */
public class MyWebSocketHandler implements WebSocketHandler {

    private Thread thread;

    private static final ScheduledExecutorService executorService =
            Executors.newSingleThreadScheduledExecutor();

    private String receivedMessage;


    //建立连接后的操作
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        System.out.println("已建立连接..");

        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {

                String[] strings = receivedMessage.split(",");

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
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    //消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
    public void handleMessage(WebSocketSession session,
                              WebSocketMessage<?> message) throws Exception {
        receivedMessage = message.getPayload().toString();
        //System.out.println(receivedMessage);
        //sendMessages(session, receivedMessage);
    }

    private void sendMessages(WebSocketSession session, String receivedMessage) {

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

    public static void main(String[] args) {

        int[] a = new int[2];
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < a.length; i++) {
                    System.out.print(a[i] + " ");
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
        try {
            Thread.sleep(3000);
        }catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        a[1] = 1;

        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println("next");
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
}
