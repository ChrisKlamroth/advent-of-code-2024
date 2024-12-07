package day07;

import java.util.ArrayList;
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

  public boolean isValid(boolean useConcatenation) {
    return this.isValid(this.result, this.operands.size() - 1, useConcatenation);
  }

  private boolean isValid(final long result, final int index, boolean useConcatenation) {
    if (index == 0) {
      return result == this.operands.get(index);
    }

    final double division = ((double) result) / this.operands.get(index);
    if (this.isInteger(division) && this.isValid((long) division, index - 1, useConcatenation)) {
      return true;
    }

    final long substraction = result - this.operands.get(index);
    if (this.isValid(substraction, index - 1, useConcatenation)) {
      return true;
    }

    if (useConcatenation && this.isConcatenation(result, this.operands.get(index))) {
      final long splitted = this.splitConcatenation(result, this.operands.get(index));
      return this.isValid(splitted, index - 1, useConcatenation);
    }

    return false;
  }

  private long splitConcatenation(long a, long b) {
    final String stringA = Long.toString(a);
    final String stringB = Long.toString(b);
    final String result = stringA.substring(0, stringA.length() - stringB.length());

    return Long.parseLong(result);
  }

  private boolean isInteger(double value) {
    return value % 1 == 0;
  }

  private boolean isConcatenation(long a, long b) {
    final String stringA = Long.toString(a);
    final String stringB = Long.toString(b);

    return a > 0
        && stringA.length() > 1
        && stringA.length() > stringB.length()
        && stringA.endsWith(stringB);
  }

  @Override
  public String toString() {
    return String.format("Equation(%n  result=%d,%n  operands=%s,%n)",
        this.result,
        this.operands);
  }
}
