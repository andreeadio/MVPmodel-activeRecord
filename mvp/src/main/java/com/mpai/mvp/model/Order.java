package com.mpai.mvp.model;


import com.mpai.mvp.util.NotificationUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Order {

  private static String storagePath = "./data/orders.csv";

  private Long id;
  private String clientName;
  private String city;
  private OrderStatus status;
  private String itemsSummary;

  public Order() {}

  public Order(Long id, String clientName, String city, OrderStatus status, String itemsSummary) {
    this.id = id;
    this.clientName = clientName;
    this.city = city;
    this.status = status;
    this.itemsSummary = itemsSummary;
  }

  // ===== Active Record API =====

  public static void configureStoragePath(String path) {
    if (path != null && !path.isBlank()) storagePath = path;
  }

  public static List<Order> loadAll() {
    ensureFileExists();

    try {
      List<String> lines = Files.readAllLines(Path.of(storagePath));
      return lines.stream()
          .filter(l -> !l.isBlank())
          .skip(1) // header
          .map(Order::fromCsv)
          .flatMap(Optional::stream)
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new IllegalStateException("Cannot read storage file: " + storagePath, e);
    }
  }

  public static Optional<Order> findById(long id) {
    return loadAll().stream().filter(o -> o.getId() == id).findFirst();
  }

  public static List<Order> search(String query, OrderStatus statusFilter) {
    String q = query == null ? "" : query.trim().toLowerCase();

    return loadAll().stream()
        .filter(o -> q.isEmpty()
            || String.valueOf(o.getId()).contains(q)
            || o.getClientName().toLowerCase().contains(q)
            || o.getCity().toLowerCase().contains(q))
        .filter(o -> statusFilter == null || o.getStatus() == statusFilter)
        .collect(Collectors.toList());
  }

  public static void saveAll(List<Order> orders) {
    ensureFileExists();

    Path path = Path.of(storagePath);
    try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {
      writer.write("id,clientName,city,status,itemsSummary");
      writer.newLine();
      for (Order o : orders) {
        writer.write(o.toCsv());
        writer.newLine();
      }
    } catch (IOException e) {
      throw new IllegalStateException("Cannot write storage file: " + storagePath, e);
    }
  }

  public void updateStatus(OrderStatus newStatus) {
    if (newStatus == null) throw new IllegalArgumentException("Status cannot be null");
    if (!this.status.canTransitionTo(newStatus)) {
      throw new IllegalArgumentException("Invalid transition: " + this.status + " -> " + newStatus);
    }

    OrderStatus old = this.status;
    this.status = newStatus;

    // persist: read -> modify -> write
    List<Order> all = loadAll();
    boolean updated = false;
    for (int i = 0; i < all.size(); i++) {
      if (Objects.equals(all.get(i).id, this.id)) {
        all.set(i, this);
        updated = true;
        break;
      }
    }
    if (!updated) {
      throw new IllegalStateException("Order not found for update: " + id);
    }
    saveAll(all);

    NotificationUtil.notifyClient(this, old, newStatus);
  }

  // ===== CSV helpers =====

  private static Optional<Order> fromCsv(String line) {
    // naive CSV (ok pentru examen). dacă ai virgule în text, le eviți.
    String[] parts = line.split(",", -1);
    if (parts.length < 5) return Optional.empty();

    try {
      Long id = Long.parseLong(parts[0].trim());
      String client = parts[1].trim();
      String city = parts[2].trim();
      OrderStatus st = OrderStatus.valueOf(parts[3].trim());
      String items = parts[4].trim();
      return Optional.of(new Order(id, client, city, st, items));
    } catch (Exception ex) {
      return Optional.empty();
    }
  }

  private String toCsv() {
    return String.format("%d,%s,%s,%s,%s",
        id,
        safe(clientName),
        safe(city),
        status.name(),
        safe(itemsSummary)
    );
  }

  private static String safe(String s) {
    return s == null ? "" : s.replace(",", " ");
  }

  private static void ensureFileExists() {
    try {
      Path path = Path.of(storagePath);
      Path parent = path.getParent();
      if (parent != null) Files.createDirectories(parent);

      if (!Files.exists(path)) {
        Files.writeString(path, "id,clientName,city,status,itemsSummary\n",
            StandardOpenOption.CREATE, StandardOpenOption.WRITE);
      }
    } catch (IOException e) {
      throw new IllegalStateException("Cannot initialize storage file: " + storagePath, e);
    }
  }

  // ===== getters/setters =====

  public Long getId() { return id; }
  public String getClientName() { return clientName; }
  public String getCity() { return city; }
  public OrderStatus getStatus() { return status; }
  public String getItemsSummary() { return itemsSummary; }

  public void setId(Long id) { this.id = id; }
  public void setClientName(String clientName) { this.clientName = clientName; }
  public void setCity(String city) { this.city = city; }
  public void setStatus(OrderStatus status) { this.status = status; }
  public void setItemsSummary(String itemsSummary) { this.itemsSummary = itemsSummary; }
}
