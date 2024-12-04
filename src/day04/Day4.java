package day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
  private static final String SEARCH_REGEX = "(?=XMAS|SAMX)";

  public static void main(String[] args) throws IOException {
    final List<List<String>> wordSearch = readWordSearch();

    final List<List<String>> rows = getRows(wordSearch);
    final List<List<String>> columns = getColumns(wordSearch);
    final List<List<String>> rightDiagonals = getRightDiagonals(wordSearch);
    final List<List<String>> leftDiagonals = getLeftDiagonals(wordSearch);

    final int rowsCount = countMatches(rows);
    final int columnsCount = countMatches(columns);
    final int rightDiagonalsCount = countMatches(rightDiagonals);
    final int leftDiagonalsCount = countMatches(leftDiagonals);
    final int totalCount = rowsCount + columnsCount + rightDiagonalsCount + leftDiagonalsCount;

    System.out.printf(
        "--- %d Matches ---%n- Rows: %d%n- Columns: %d%n- Right Diagonals: %d%n- Left Diagonals: %d%n",
        totalCount,
        rowsCount,
        columnsCount,
        rightDiagonalsCount,
        leftDiagonalsCount);
  }

  private static List<List<String>> readWordSearch() throws IOException {
    return Files
        .lines(WORD_SEARCH_FILE_PATH)
        .map(line -> Arrays.asList(line.split("")))
        .toList();
  }

  private static int countMatches(List<List<String>> matrixLines) {
    final Pattern pattern = Pattern.compile(SEARCH_REGEX);
    int matches = 0;

    for (final List<String> line : matrixLines) {
      final String lineString = String.join("", line);
      final Matcher matcher = pattern.matcher(lineString);
      while (matcher.find()) {
        matches++;
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
  private static List<List<String>> getRows(List<List<String>> matrix) {
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
  private static List<List<String>> getColumns(List<List<String>> matrix) {
    final List<List<String>> columns = new ArrayList<>();

    for (int columnIndex = 0; columnIndex < matrix.size(); columnIndex++) {
      final List<String> column = new ArrayList<>();
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
  private static List<List<String>> getRightDiagonals(List<List<String>> matrix) {
    final List<List<String>> diagonals = new ArrayList<>();

    /**
     * Diagonals:     Indices:
     * / / / /        1 2 3 4
     * / / / .        1 2 3 .
     * / / . .        1 2 . .
     * / . . .        1 . . .
     */
    for (int rowIndex = 0; rowIndex < matrix.size(); rowIndex++) {
      final List<String> diagonal = new ArrayList<>();
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
      final List<String> diagonal = new ArrayList<>();
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
  private static List<List<String>> getLeftDiagonals(List<List<String>> matrix) {
    final List<List<String>> diagonals = new ArrayList<>();

    /**
     * Diagonals:     Indices:
     * \ . . .        1 . . .
     * \ \ . .        1 2 . .
     * \ \ \ .        1 2 3 .
     * \ \ \ \        1 2 3 4
     */
    for (int rowIndex = matrix.size() - 1; rowIndex >= 0; rowIndex--) {
      final List<String> diagonal = new ArrayList<>();
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
      final List<String> diagonal = new ArrayList<>();
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
}
