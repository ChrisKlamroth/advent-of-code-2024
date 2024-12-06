package day06;

import java.util.EnumMap;
import java.util.Map.Entry;

public enum Direction {
  UP,
  DOWN,
  RIGHT,
  LEFT;

  private static final EnumMap<Direction, String> SYMBOLS = new EnumMap<>(Direction.class);
  static {
    SYMBOLS.put(UP, "^");
    SYMBOLS.put(DOWN, "v");
    SYMBOLS.put(RIGHT, ">");
    SYMBOLS.put(LEFT, "<");
  }

  public static Direction fromSymbol(String symbol) {
    for (final Entry<Direction, String> entry : SYMBOLS.entrySet()) {
      if (symbol.equals(entry.getValue())) {
        return entry.getKey();
      }
    }
    throw new IllegalArgumentException("Symbol is not a direction symbol");
  }

  public static boolean isDirectionSymbol(String symbol) {
    return SYMBOLS.containsValue(symbol);
  }

  public String getSymbol() {
    return SYMBOLS.get(this);
  }

  @Override
  public String toString() {
    return this.getSymbol();
  }
}