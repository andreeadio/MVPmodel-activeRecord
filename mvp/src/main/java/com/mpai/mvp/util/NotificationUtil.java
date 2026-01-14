package com.mpai.mvp.util;


import com.mpai.mvp.model.Order;
import com.mpai.mvp.model.OrderStatus;

public class NotificationUtil {

  private NotificationUtil() {}

  public static void notifyClient(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
    // Pentru examen: log + mesaj în UI (îl trimitem prin redirect param în Presenter)
    System.out.printf("NOTIFY client=%s | orderId=%d | %s -> %s%n",
        order.getClientName(), order.getId(), oldStatus, newStatus);
  }
}
