package day02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day2 {
  private static final Path REPORTS_FILE_PATH = Paths.get("./src/day02/reports.txt");
  private static final String LEVEL_SEPARATOR = " ";

  public static void main(String[] args) throws IOException {
    final List<List<Integer>> reports = Files.lines(REPORTS_FILE_PATH)
        .map(line -> parseReport(line))
        .filter(report -> isReportSafe(report, true))
        .toList();

    System.out.printf("%d reports are safe%n", reports.size());
  }

  private static List<Integer> parseReport(String line) {
    return Arrays
        .asList(line.split(LEVEL_SEPARATOR))
        .stream()
        .map(Integer::parseInt)
        .toList();
  }

  private static boolean isReportSafe(List<Integer> report, boolean withDampener) {
    if (withDampener) {
      return areIncreasingLevelsValidWithDampener(report) || areDecreasingLevelsValidWithDampener(report);
    } else {
      return areIncreasingLevelsValid(report) || areDecreasingLevelsValid(report);
    }
  }

  private static boolean areIncreasingLevelsValid(List<Integer> report) {
    for (int index = 0; index < report.size() - 1; index++) {
      // Levels are increasing.
      if (report.get(index + 1) <= report.get(index)) {
        return false;
      }
      // Level difference is at most 3.
      if (report.get(index + 1) - report.get(index) > 3) {
        return false;
      }
    }
    return true;
  }

  private static boolean areDecreasingLevelsValid(List<Integer> report) {
    for (int index = 0; index < report.size() - 1; index++) {
      // Levels are decreasing.
      if (report.get(index + 1) >= report.get(index)) {
        return false;
      }
      // Level difference is at most 3.
      if (report.get(index) - report.get(index + 1) > 3) {
        return false;
      }
    }
    return true;
  }

  private static boolean areIncreasingLevelsValidWithDampener(List<Integer> report) {
    for (int index = 0; index < report.size() - 1; index++) {
      boolean isIncreasing = report.get(index + 1) > report.get(index);
      boolean isDifferenceAtMostThree = report.get(index + 1) - report.get(index) <= 3;

      if (!isIncreasing || !isDifferenceAtMostThree) {
        // Problem dampener triggers and checks if the problematic level is the current
        // or the next one.
        return areIncreasingLevelsValid(getReportWithoutLevelAt(report, index))
            || areIncreasingLevelsValid(getReportWithoutLevelAt(report, index + 1));
      }
    }
    return true;
  }

  private static boolean areDecreasingLevelsValidWithDampener(List<Integer> report) {
    for (int index = 0; index < report.size() - 1; index++) {
      boolean isDecreasing = report.get(index + 1) < report.get(index);
      boolean isDifferenceAtMostThree = report.get(index) - report.get(index + 1) <= 3;

      if (!isDecreasing || !isDifferenceAtMostThree) {
        // Problem dampener triggers and checks if the problematic level is the current
        // or the next one.
        return areDecreasingLevelsValid(getReportWithoutLevelAt(report, index))
            || areDecreasingLevelsValid(getReportWithoutLevelAt(report, index + 1));
      }
    }
    return true;
  }

  /**
   * Returns a copy of the report without the level at the given index.
   */
  private static List<Integer> getReportWithoutLevelAt(List<Integer> report, int index) {
    List<Integer> reportCopy = new ArrayList<>(report);
    reportCopy.remove(index);
    return reportCopy;
  }
}
