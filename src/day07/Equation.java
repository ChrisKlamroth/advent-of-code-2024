package day07;

import java.util.Arrays;
import java.util.List;

public class Equation {
  final private long result;
  final private List<Long> operands;

  public static Equation fromFileLine(String line) {
    final String[] parts = line.split(":");
    final long result = Long.parseLong(parts[0]);
    final List<Long> operands = Arrays.asList(parts[1].trim().split(" ")).stream()
        .map(Long::parseLong)
        .toList();

    return new Equation(result, operands);
  }

  private Equation(long result, List<Long> operands) {
    this.result = result;
    this.operands = operands;
  }

  public long getResult() {
    return this.result;
  }

  public boolean isValid() {
    return this.isValid(this.result, this.operands.size() - 1);
  }

  private boolean isValid(final long result, final int index) {
    if (index == 0) {
      return result == this.operands.get(index);
    }

    final double division = ((double) result) / this.operands.get(index);
    if (this.isInteger(division) && this.isValid((long) division, index - 1)) {
      return true;
    }

    final long substraction = result - this.operands.get(index);
    return this.isValid(substraction, index - 1);
  }

  private boolean isInteger(double value) {
    return value % 1 == 0;
  }

  @Override
  public String toString() {
    return String.format("Equation(%n  result=%d,%n  operands=%s,%n)",
        this.result,
        this.operands);
  }
}
