package day05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PageRuleset implements Comparable<PageRuleset> {
  private final Integer pageNumber;
  private final List<Integer> predecessors;
  private final List<Integer> successors;

  public PageRuleset(Integer pageNumber) {
    this.pageNumber = pageNumber;
    this.predecessors = new ArrayList<>();
    this.successors = new ArrayList<>();
  }

  public boolean isForPageNumber(Integer pageNumber) {
    return this.pageNumber.equals(pageNumber);
  }

  public void addPredecessor(Integer pageNumber) {
    this.predecessors.add(pageNumber);
    Collections.sort(this.predecessors);
  }

  public void addSuccessor(Integer pageNumber) {
    this.successors.add(pageNumber);
    Collections.sort(this.successors);
  }

  public boolean isPredecessor(Integer pageNumber) {
    return this.predecessors.contains(pageNumber);
  }

  public boolean isSuccessor(Integer pageNumber) {
    return this.successors.contains(pageNumber);
  }

  @Override
  public int compareTo(PageRuleset other) {
    return this.pageNumber.compareTo(other.pageNumber);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (this.getClass() != object.getClass()) {
      return false;
    }
    PageRuleset other = (PageRuleset) object;
    return this.pageNumber.equals(other.pageNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.pageNumber);
  }

  @Override
  public String toString() {
    return String.format("PageRuleset(%n  pageNumber=%d,%n  predecessors=%s,%n  successors=%s,%n)",
        this.pageNumber,
        this.predecessors,
        this.successors);
  }
}