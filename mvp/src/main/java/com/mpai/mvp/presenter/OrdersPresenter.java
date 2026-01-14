package com.mpai.mvp.presenter;


import com.mpai.mvp.model.Order;
import com.mpai.mvp.model.OrderStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrdersPresenter {

  public OrdersPresenter(@Value("${app.storage.ordersFile}") String filePath) {
    // configurează storage (Active Record)
    Order.configureStoragePath(filePath);
  }

  @GetMapping
  public String list(
      @RequestParam(required = false) String q,
      @RequestParam(required = false) OrderStatus status,
      @RequestParam(required = false) String msg,
      Model model
  ) {
    List<Order> orders = Order.search(q, status);

    model.addAttribute("orders", orders);
    model.addAttribute("q", q == null ? "" : q);
    model.addAttribute("status", status);
    model.addAttribute("allStatuses", OrderStatus.values());
    model.addAttribute("msg", msg);

    return "orders";
  }

  @GetMapping("/{id}")
  public String details(@PathVariable long id, Model model) {
    Order order = Order.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));

    model.addAttribute("order", order);
    model.addAttribute("allStatuses", OrderStatus.values());
    return "order-details";
  }

  @PostMapping("/{id}/status")
  public String updateStatus(@PathVariable long id, @RequestParam OrderStatus newStatus) {
    Order order = Order.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));

    order.updateStatus(newStatus);

    return "redirect:/orders?msg=Status+updated+for+order+" + id;
  }
}
