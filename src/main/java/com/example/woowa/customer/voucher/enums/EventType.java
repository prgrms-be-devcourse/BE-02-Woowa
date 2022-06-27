package com.example.woowa.customer.voucher.enums;

import java.util.Arrays;
import java.util.Optional;

public enum EventType {
  MONTH("month"),
  SPECIAL("special");

  private final String type;

  EventType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return type;
  }

  public static EventType of(String value) throws Exception {
    return Optional.ofNullable(find(value)).orElseThrow(() -> new RuntimeException("not valid event type"));
  }

  private static EventType find(String value) {
    return Arrays.stream(values()).filter(type -> type.toString().equals(value)).findFirst().orElse(null);
  }
}
