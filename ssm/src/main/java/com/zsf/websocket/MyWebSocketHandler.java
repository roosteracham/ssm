package com.zsf.websocket;

import org.apache.log4j.Logger;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * websocket处理器：功能实现的核心代码编写类
 */
public class MyWebSocketHandler implements WebSocketHandler {

    private Logger logger = Logger.getLogger(MyWebSocketHandler.class);

    private static final ScheduledExecutorService executorService =
            Executors.newSingleThreadScheduledExecutor();

    // 可对300个测点值更新
    private List<String> points = new ArrayList<>(300);


    //建立连接后的操作
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        logger.info("已建立连接..");

        executorService.scheduleWithFixedDelay(() -> {

            StringBuilder stringBuilder = new StringBuilder("{");

            synchronized (points) {
                for (int j = 0; j < points.size(); j++) {
                    stringBuilder.append("\"")
                            .append(points.get(j))
                            .append("\":\"")
                            .append((int) (Math.random() * 50));
                    if (j == points.size() - 1) {
                        stringBuilder.append("\"");
                    } else {
                        stringBuilder.append("\",");
                    }
                }
            }
            stringBuilder.append("}");


            try {
                session.sendMessage(new TextMessage(stringBuilder.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    //消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
    public void handleMessage(WebSocketSession session,
                              WebSocketMessage<?> message) throws Exception {
        String receivedMessage = message.getPayload().toString();
        String[] newPoints = receivedMessage.split(",");
        for (int i = 0; i < newPoints.length; i++) {

            synchronized (points) {
                if (points.size() > 300) {
                    points.remove(0);
                }
                if (!points.contains(newPoints[i]) && newPoints[i] != "")
                    points.add(newPoints[i]);
            }
        }
        logger.info("receivedMessage : " + receivedMessage);
    }

    //消息传输错误处理
    public void handleTransportError(WebSocketSession session,
                                     Throwable exception) throws Exception {
        logger.error("消息传输错误..");
    }

    //连接关闭后的操作
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus closeStatus) throws Exception {
        logger.info("连接关闭后的操作..");
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
        } catch (InterruptedException e) {
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
