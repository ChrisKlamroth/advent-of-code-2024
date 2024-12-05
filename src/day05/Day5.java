package day05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Day5 {
  private static final Path UPDATES_FILE_PATH = Paths.get("./src/day05/updates.txt");

  public static void main(String[] args) throws IOException {
    final Map<Integer, List<Integer>> beforeRules = new HashMap<>();
    final Map<Integer, List<Integer>> afterRules = new HashMap<>();
    final List<List<Integer>> pageUpdates = new ArrayList<>();

    final AtomicBoolean isRule = new AtomicBoolean(true);

    for (final String line : Files.lines(UPDATES_FILE_PATH).toArray(String[]::new)) {
      if (line.equals("")) {
        isRule.set(false);
        continue;
      }
      if (isRule.get()) {
        final String[] pages = line.split("\\|");
        final Integer x = Integer.valueOf(pages[0]);
        final Integer y = Integer.valueOf(pages[1]);

        if (!beforeRules.containsKey(x)) {
          beforeRules.put(x, new ArrayList<>());
        }
        beforeRules.get(x).add(y);

        if (!afterRules.containsKey(y)) {
          afterRules.put(y, new ArrayList<>());
        }
        afterRules.get(y).add(x);
      } else {
        final List<Integer> pageUpdate = Arrays.asList(line.split(","))
            .stream()
            .map(Integer::valueOf)
            .toList();
        pageUpdates.add(pageUpdate);
      }
    }

    final List<List<Integer>> validPageUpdates = new ArrayList<>();
    for (final List<Integer> pageUpdate : pageUpdates) {
      if (isPageUpdateValid(pageUpdate, beforeRules, afterRules)) {
        validPageUpdates.add(pageUpdate);
      }
    }

    int middlePageSum = 0;
    for (final List<Integer> validPageUpdate : validPageUpdates) {
      final Integer middlePage = validPageUpdate.get((validPageUpdate.size() / 2));
      middlePageSum = middlePageSum + middlePage;
    }

    System.out.printf("The result of adding up the middle page number from the correctly-ordered updates is %d%n",
        middlePageSum);
  }

  private static boolean isPageUpdateValid(List<Integer> pageUpdate, Map<Integer, List<Integer>> beforeRules,
      Map<Integer, List<Integer>> afterRules) {
    for (int i = 0; i < pageUpdate.size(); i++) {
      for (int j = i + 1; j < pageUpdate.size(); j++) {
        final Integer x = pageUpdate.get(i);
        final Integer y = pageUpdate.get(j);
        if (beforeRules.containsKey(y) && beforeRules.get(y).contains(x)) {
          return false;
        }
      }
    }

    for (int i = pageUpdate.size() - 1; i >= 0; i--) {
      for (int j = i - 1; j >= 0; j--) {
        final Integer x = pageUpdate.get(i);
        final Integer y = pageUpdate.get(j);
        if (afterRules.containsKey(y) && afterRules.get(y).contains(x)) {
          return false;
        }
      }
    }

    return true;
  }
}