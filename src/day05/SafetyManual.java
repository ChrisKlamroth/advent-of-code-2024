package day05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SafetyManual {
  private static final Path FILE_PATH = Paths.get("./src/day05/updates.txt");
  private static final String FILE_SECTION_SEPARATOR = "";

  private final List<PageRuleset> pageRulesets;
  private final List<List<Integer>> pagesToProduce;

  public SafetyManual() throws IOException {
    this.pageRulesets = new ArrayList<>();
    this.pagesToProduce = new ArrayList<>();
    this.loadUpdateFromFile();
  }

  public void part1() throws IOException {
    final List<List<Integer>> validPages = this.pagesToProduce.stream()
        .filter(pages -> this.arePagesValid(pages))
        .toList();
    final int sum = this.sumMiddlePageNumbers(validPages);
    System.out.printf("The sum of all middle page numbers of the valid pages is %d%n", sum);
  }

  public void part2() throws IOException {
    final List<List<Integer>> fixedPages = this.pagesToProduce.stream()
        .filter(pages -> !this.arePagesValid(pages))
        .map(invalidPages -> this.fixPages(invalidPages))
        .toList();
    final int sum = this.sumMiddlePageNumbers(fixedPages);
    System.out.printf("The sum of all middle page numbers of the fixed pages is %d%n", sum);
  }

  private void loadUpdateFromFile() throws IOException {
    final String[] fileLines = Files.lines(FILE_PATH).toArray(String[]::new);
    boolean isPageRuleSection = true;

    for (final String line : fileLines) {
      if (line.equals(FILE_SECTION_SEPARATOR)) {
        isPageRuleSection = false;
        continue;
      }
      if (isPageRuleSection) {
        this.parsePageRulesetFromFileLine(line);
      } else {
        this.parsePagesToProduceFromFileLine(line);
      }
    }
  }

  public int sumMiddlePageNumbers(List<List<Integer>> pagesToProduce) {
    return pagesToProduce.stream()
        .map(pages -> pages.get(pages.size() / 2))
        .reduce(0, (sum, middlePageNumber) -> sum + middlePageNumber);
  }

  private boolean arePagesValid(List<Integer> pages) {
    return this.arePagePredecessorsValid(pages) && this.arePageSuccessorsValid(pages);
  }

  private boolean arePagePredecessorsValid(List<Integer> pages) {
    for (int currentPageIndex = pages.size() - 1; currentPageIndex >= 0; currentPageIndex--) {
      for (int previousPageIndex = currentPageIndex - 1; previousPageIndex >= 0; previousPageIndex--) {
        final Optional<PageRuleset> pageRuleset = this.findRuleset(pages.get(currentPageIndex));
        final boolean isInvalid = pageRuleset.isPresent() && pageRuleset.get().isSuccessor(pages.get(previousPageIndex));
        if (isInvalid) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean arePageSuccessorsValid(List<Integer> pages) {
    for (int currentPageIndex = 0; currentPageIndex < pages.size(); currentPageIndex++) {
      for (int nextPageIndex = currentPageIndex + 1; nextPageIndex < pages.size(); nextPageIndex++) {
        final Optional<PageRuleset> pageRuleset = this.findRuleset(pages.get(currentPageIndex));
        final boolean isInvalid = pageRuleset.isPresent() && pageRuleset.get().isPredecessor(pages.get(nextPageIndex));
        if (isInvalid) {
          return false;
        }
      }
    }
    return true;
  }

  private List<Integer> fixPages(List<Integer> pages) {
    return this.fixSuccessors(this.fixPredecessors(pages));
  }

  private List<Integer> fixPredecessors(List<Integer> pages) {
    for (int currentPageIndex = pages.size() - 1; currentPageIndex >= 0; currentPageIndex--) {
      for (int previousPageIndex = currentPageIndex - 1; previousPageIndex >= 0; previousPageIndex--) {
        final Optional<PageRuleset> pageRuleset = this.findRuleset(pages.get(currentPageIndex));
        final boolean isInvalid = pageRuleset.isPresent() && pageRuleset.get().isSuccessor(pages.get(previousPageIndex));
        if (isInvalid) {
          final List<Integer> fixedPages = this.swapPages(pages, currentPageIndex, previousPageIndex);
          return this.fixPredecessors(fixedPages);
        }
      }
    }
    return pages;
  }

  private List<Integer> fixSuccessors(List<Integer> pages) {
    for (int currentPageIndex = 0; currentPageIndex < pages.size(); currentPageIndex++) {
      for (int nextPageIndex = currentPageIndex + 1; nextPageIndex < pages.size(); nextPageIndex++) {
        final Optional<PageRuleset> pageRuleset = this.findRuleset(pages.get(currentPageIndex));
        final boolean isInvalid = pageRuleset.isPresent() && pageRuleset.get().isPredecessor(pages.get(nextPageIndex));
        if (isInvalid) {
          final List<Integer> fixedPages = this.swapPages(pages, currentPageIndex, nextPageIndex);
          return this.fixPredecessors(fixedPages);
        }
      }
    }
    return pages;
  }

  private List<Integer> swapPages(List<Integer> pages, int i, int j) {
    final List<Integer> pagesCopy = new ArrayList<>(pages);
    Collections.swap(pagesCopy, i, j);
    return pagesCopy;
  }

  private Optional<PageRuleset> findRuleset(Integer pageNumber) {
    return this.pageRulesets.stream()
        .filter(ruleset -> ruleset.isForPageNumber(pageNumber))
        .findAny();
  }

  private void parsePageRulesetFromFileLine(String line) {
    final PageOrderingRule pageRule = PageOrderingRule.fromFileLine(line);

    this.pageRulesets.stream()
        .filter(pageRuleset -> pageRuleset.isForPageNumber(pageRule.getPageNumberX()))
        .findAny()
        .orElseGet(() -> this.getAndAddRuleset(pageRule.getPageNumberX()))
        .addSuccessor(pageRule.getPageNumberY());

    this.pageRulesets.stream()
        .filter(pageRuleset -> pageRuleset.isForPageNumber(pageRule.getPageNumberY()))
        .findAny()
        .orElseGet(() -> this.getAndAddRuleset(pageRule.getPageNumberY()))
        .addPredecessor(pageRule.getPageNumberX());

    Collections.sort(this.pageRulesets);
  }

  private void parsePagesToProduceFromFileLine(String line) {
    final List<Integer> pages = Arrays.asList(line.split(",")).stream()
        .map(Integer::valueOf)
        .toList();
    this.pagesToProduce.add(pages);
  }

  private PageRuleset getAndAddRuleset(Integer pageNumber) {
    final PageRuleset newRuleset = new PageRuleset(pageNumber);
    this.pageRulesets.add(newRuleset);
    return newRuleset;
  }
}
