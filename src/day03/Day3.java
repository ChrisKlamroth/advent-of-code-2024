package day03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {
  private static final Path MEMORY_FILE_PATH = Paths.get("./src/day03/memory.txt");

  public static void main(String[] args) throws IOException {
    final String memory = readMemory();
    Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
    Matcher matcher = pattern.matcher(memory);

    int result = 0;

    while (matcher.find()) {
      int x = Integer.parseInt(matcher.group(1));
      int y = Integer.parseInt(matcher.group(2));
      result = result + (x * y);
    }

    System.out.printf("The result of adding up all of the multiplications is %d%n", result);
  }

  private static String readMemory() throws IOException {
    final StringBuilder memoryBuilder = new StringBuilder();
    Files.lines(MEMORY_FILE_PATH).forEach(line -> memoryBuilder.append(line));

    return memoryBuilder.toString();
  }
}
