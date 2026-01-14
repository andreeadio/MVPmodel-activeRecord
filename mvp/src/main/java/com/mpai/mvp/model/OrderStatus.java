package com.mpai.mvp.model;

public enum OrderStatus {
  PRELUATA,
  PREGATITA,
  PLATITA,
  ONORATA;

  public boolean canTransitionTo(OrderStatus next) {
    if (next == null) return false;
    return switch (this) {
      case PRELUATA -> next == PREGATITA;
      case PREGATITA -> next == PLATITA;
      case PLATITA -> next == ONORATA;
      case ONORATA -> false;
    };
  }
}
