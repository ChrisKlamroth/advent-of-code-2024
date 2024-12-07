package day07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Day7 {
  private static final Path EXAMPLE_FILE_PATH = Paths.get("./src/day07/example.txt");
  private static final Path CALIBRATIONS_FILE_PATH = Paths.get("./src/day07/calibrations.txt");

  public static void main(String[] args) throws IOException {
    part1();
    part2();
  }

  private static void part1() throws IOException {
    final List<Equation> equations = Files.lines(CALIBRATIONS_FILE_PATH)
        .map(Equation::fromFileLine)
        .toList();

    final long resultSum = equations.stream()
        .filter(equation -> equation.isValid(false))
        .map(equation -> equation.getResult())
        .reduce(0l, (sum, result) -> sum + result);

    System.out.printf("The total calibration result is %d%n", resultSum);
  }

  private static void part2() throws IOException {
    final List<Equation> equations = Files.lines(CALIBRATIONS_FILE_PATH)
        .map(Equation::fromFileLine)
        .toList();

    final long resultSum = equations.stream()
        .filter(equation -> equation.isValid(true))
        .map(equation -> equation.getResult())
        .reduce(0l, (sum, result) -> sum + result);

    System.out.printf("The total calibration result with concatenation is %d%n", resultSum);
  }
}
