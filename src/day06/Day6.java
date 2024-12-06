package day06;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Day6 {
  private static final Path EXAMPLE_FILE_PATH = Paths.get("./src/day06/example.txt");
  private static final Path MAP_FILE_PATH = Paths.get("./src/day06/map.txt");

  public static void main(String[] args) throws IOException {
    part1();
  }

  private static void part1() throws IOException {
    final Map map = Map.loadFromFile(MAP_FILE_PATH);
    map.predictGuardRoute();
    map.print();
    System.out.printf("The guard visited %d distinct positions%n", map.countVisitedSpaces());
  }
}
