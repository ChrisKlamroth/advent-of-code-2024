package day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 {
  private static final Path WORD_SEARCH_FILE_PATH = Paths.get("./src/day04/word-search.txt");

  // We use a lookahead to prevent the matches from being consumed
  // and instead find any overlapping matches.
  //
  // Ex. "XMASAMX" would match a total of two times, once for "XMAS" and once for "SAMX".
  //
  // Without a lookahead, the first match of "XMAS" would be consumed
  // and only leave "AMX" for future matches.
  private static final String XMAS_REGEX = "(?=XMAS|SAMX)";
  private static final String X_SHAPED_MAS_REGEX = "(?=MAS|SAM)";

  public static void main(String[] args) throws IOException {
    part1();
    System.out.println();
    part2();
  }

  private static void part1() throws IOException {
    final List<List<MatrixElement>> wordSearch = readWordSearch();

    final List<List<MatrixElement>> rows = getRows(wordSearch);
    final List<List<MatrixElement>> columns = getColumns(wordSearch);
    final List<List<MatrixElement>> rightDiagonals = getRightDiagonals(wordSearch);
    final List<List<MatrixElement>> leftDiagonals = getLeftDiagonals(wordSearch);

    final int rowsCount = countXmasMatches(rows);
    final int columnsCount = countXmasMatches(columns);
    final int rightDiagonalsCount = countXmasMatches(rightDiagonals);
    final int leftDiagonalsCount = countXmasMatches(leftDiagonals);
    final int totalCount = rowsCount + columnsCount + rightDiagonalsCount + leftDiagonalsCount;

    System.out.println("--- Part 1 ---");
    System.out.printf(
        "%d Matches:%n- Rows: %d%n- Columns: %d%n- Right Diagonals: %d%n- Left Diagonals: %d%n",
        totalCount,
        rowsCount,
        columnsCount,
        rightDiagonalsCount,
        leftDiagonalsCount);
  }

  private static void part2() throws IOException {
    final List<List<MatrixElement>> wordSearch = readWordSearch();
    final int xShapedMasCount = countXShapedMasMatches(wordSearch);

    System.out.println("--- Part 2 ---");
    System.out.printf("Two MAS in the shape of an X appear %d times%n", xShapedMasCount);
  }

  private static List<List<MatrixElement>> readWordSearch() throws IOException {
    final AtomicInteger rowIndex = new AtomicInteger();
    return Files
        .lines(WORD_SEARCH_FILE_PATH)
        .map(line -> parseMatrixLine(line, rowIndex))
        .toList();
  }

  private static List<MatrixElement> parseMatrixLine(String line, AtomicInteger rowIndex) {
    final AtomicInteger columnIndex = new AtomicInteger();
    final List<MatrixElement> elements = Arrays
        .asList(line.split(""))
        .stream()
        .map(value -> new MatrixElement(rowIndex.get(), columnIndex.getAndIncrement(), value))
        .toList();
    rowIndex.set(rowIndex.get() + 1);
    return elements;
  }

  private static int countXmasMatches(List<List<MatrixElement>> matrixLines) {
    final Pattern pattern = Pattern.compile(XMAS_REGEX);
    int matches = 0;

    for (final List<MatrixElement> line : matrixLines) {
      final String lineString = String.join("", line.stream().map(element -> element.getValue()).toList());
      final Matcher matcher = pattern.matcher(lineString);
      while (matcher.find()) {
        matches++;
      }
    }

    return matches;
  }

  private static int countXShapedMasMatches(List<List<MatrixElement>> wordSearch) {
    final List<List<MatrixElement>> leftDiagonals = getLeftDiagonals(wordSearch);
    final Pattern pattern = Pattern.compile(X_SHAPED_MAS_REGEX);
    int matches = 0;

    // Look for matches on the left diagonals.
    for (final List<MatrixElement> leftDiagonal : leftDiagonals) {
      final String diagionalString = String.join("", leftDiagonal.stream().map(element -> element.getValue()).toList());
      final Matcher matcher = pattern.matcher(diagionalString);

      // If a match has been found, it means the first axis of the "X", namely the "\", is complete.
      // Now we only have to compare the two element on the edges of the other axis, namely the element
      // on the top right and on the bottom left, to complete the other axis "/";
      while (matcher.find()) {
        final MatrixElement matchedElement = leftDiagonal.get(matcher.start());
        final MatrixElement topRightElement = wordSearch
            .get(matchedElement.getRowIndex())
            .get(matchedElement.getColumnIndex() + 2);
        final MatrixElement bottomLeftElement = wordSearch
            .get(matchedElement.getRowIndex() + 2)
            .get(matchedElement.getColumnIndex());

        if ((topRightElement.hasValue("S") && bottomLeftElement.hasValue("M"))
            || (topRightElement.hasValue("M") && bottomLeftElement.hasValue("S"))) {
          matches++;
        }
      }
    }

    return matches;
  }

  /**
   * Gets the rows of the matrix, from top to bottom.
   * (A little bit redundant, but rather for technical cohesiveness)
   * 
   * Rows:          Indices:
   * - - - -        1 2 3 4
   * - - - -        1 2 3 4
   * - - - -        1 2 3 4
   * - - - -        1 2 3 4
   */
  private static List<List<MatrixElement>> getRows(List<List<MatrixElement>> matrix) {
    return matrix;
  }

  /**
   * Gets the columns of the matrix, from left to right.
   * 
   * Columns:       Indices:
   * | | | |        1 1 1 1
   * | | | |        2 2 2 2
   * | | | |        3 3 3 3
   * | | | |        4 4 4 4
   */
  private static List<List<MatrixElement>> getColumns(List<List<MatrixElement>> matrix) {
    final List<List<MatrixElement>> columns = new ArrayList<>();

    for (int columnIndex = 0; columnIndex < matrix.size(); columnIndex++) {
      final List<MatrixElement> column = new ArrayList<>();
      for (int rowIndex = 0; rowIndex < matrix.size(); rowIndex++) {
        column.add(matrix.get(rowIndex).get(columnIndex));
      }
      columns.add(column);
    }

    return columns;
  }

  /**
   * Gets the right diagonals of the matrix, starting from the top left corner.
   * 
   * Diagonals:     Indices:
   * / / / /        1 2 3 4
   * / / / /        1 2 3 3
   * / / / /        1 2 2 2
   * / / / /        1 1 1 1
   */
  private static List<List<MatrixElement>> getRightDiagonals(List<List<MatrixElement>> matrix) {
    final List<List<MatrixElement>> diagonals = new ArrayList<>();

    /**
     * Diagonals:     Indices:
     * / / / /        1 2 3 4
     * / / / .        1 2 3 .
     * / / . .        1 2 . .
     * / . . .        1 . . .
     */
    for (int rowIndex = 0; rowIndex < matrix.size(); rowIndex++) {
      final List<MatrixElement> diagonal = new ArrayList<>();
      int diagonalRowIndex = rowIndex;
      int diagonalColumnIndex = 0;

      while (diagonalRowIndex >= 0 && diagonalColumnIndex <= rowIndex) {
        diagonal.add(matrix.get(diagonalRowIndex).get(diagonalColumnIndex));
        diagonalRowIndex--;
        diagonalColumnIndex++;
      }
      diagonals.add(diagonal);
    }

    /**
     * Diagonals:     Indices:
     * . . . .        . . . .
     * . . . /        . . . 3
     * . . / /        . . 2 2
     * . / / /        . 1 1 1
     */
    for (int columnIndex = 1; columnIndex < matrix.size(); columnIndex++) {
      final List<MatrixElement> diagonal = new ArrayList<>();
      int diagonalRowIndex = matrix.size() - 1;
      int diagonalColumnIndex = columnIndex;

      while (diagonalRowIndex >= columnIndex && diagonalColumnIndex < matrix.size()) {
        diagonal.add(matrix.get(diagonalRowIndex).get(diagonalColumnIndex));
        diagonalRowIndex--;
        diagonalColumnIndex++;
      }
      diagonals.add(diagonal);
    }

    return diagonals;
  }

  /**
   * Gets the left diagonals of the matrix, starting from the bottom left corner.
   * 
   * Diagonals:     Indices:
   * \ \ \ \        1 1 1 1
   * \ \ \ \        1 2 2 2
   * \ \ \ \        1 2 3 3
   * \ \ \ \        1 2 3 4
   */
  private static List<List<MatrixElement>> getLeftDiagonals(List<List<MatrixElement>> matrix) {
    final List<List<MatrixElement>> diagonals = new ArrayList<>();

    /**
     * Diagonals:     Indices:
     * \ . . .        1 . . .
     * \ \ . .        1 2 . .
     * \ \ \ .        1 2 3 .
     * \ \ \ \        1 2 3 4
     */
    for (int rowIndex = matrix.size() - 1; rowIndex >= 0; rowIndex--) {
      final List<MatrixElement> diagonal = new ArrayList<>();
      int diagonalRowIndex = rowIndex;
      int diagonalColumnIndex = 0;

      while (diagonalRowIndex < matrix.size() && diagonalColumnIndex < matrix.size() - rowIndex) {
        diagonal.add(matrix.get(diagonalRowIndex).get(diagonalColumnIndex));
        diagonalRowIndex++;
        diagonalColumnIndex++;
      }
      diagonals.add(diagonal);
    }

    /**
     * Diagonals:     Indices:
     * . \ \ \        . 1 1 1
     * . . \ \        . . 2 2
     * . . . \        . . . 3
     * . . . .        . . . .
     */
    for (int columnIndex = 1; columnIndex < matrix.size(); columnIndex++) {
      final List<MatrixElement> diagonal = new ArrayList<>();
      int diagonalRowIndex = 0;
      int diagonalColumnIndex = columnIndex;

      while (diagonalRowIndex < matrix.size() - columnIndex && diagonalColumnIndex < matrix.size()) {
        diagonal.add(matrix.get(diagonalRowIndex).get(diagonalColumnIndex));
        diagonalRowIndex++;
        diagonalColumnIndex++;
      }
      diagonals.add(diagonal);
    }

    return diagonals;
  }

  private static void printMatrix(List<List<MatrixElement>> matrix) {
    for (int index = 0; index < matrix.size(); index++) {
      final List<String> matrixRowString = matrix
          .get(index)
          .stream()
          .map(element -> element.getValue())
          .toList();
      System.out.printf("%d%s%n", index, matrixRowString);
    }
  }
}

class MatrixElement {
  private final int rowIndex;
  private final int columnIndex;
  private final String value;

  public MatrixElement(int rowIndex, int columnIndex, String value) {
    this.rowIndex = rowIndex;
    this.columnIndex = columnIndex;
    this.value = value;
  }

  public int getRowIndex() {
    return this.rowIndex;
  }

  public int getColumnIndex() {
    return this.columnIndex;
  }

  public String getValue() {
    return this.value;
  }

  public boolean hasValue(String value) {
    return this.value.equals(value);
  }

  @Override
  public String toString() {
    return String.format("MatrixElement(rowIndex=%d, columnIndex=%d, value=%s)",
        this.rowIndex,
        this.columnIndex,
        this.value);
  }
}