package day05;

import java.util.Arrays;
import java.util.List;

public class PageOrderingRule {
  private final Integer pageNumberX;
  private final Integer pageNumberY;

  public static PageOrderingRule fromFileLine(String line) {
    final List<Integer> pageNumbers = Arrays.asList(line.split("\\|")).stream()
        .map(Integer::valueOf)
        .toList();
    return new PageOrderingRule(pageNumbers.get(0), pageNumbers.get(1));
  }

  private PageOrderingRule(Integer pageNumberX, Integer pageNumberY) {
    this.pageNumberX = pageNumberX;
    this.pageNumberY = pageNumberY;
  }

  public Integer getPageNumberX() {
    return this.pageNumberX;
  }

  public Integer getPageNumberY() {
    return this.pageNumberY;
  }
}
