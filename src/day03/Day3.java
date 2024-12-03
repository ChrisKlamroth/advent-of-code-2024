package day03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {
  private static final Path MEMORY_FILE_PATH = Paths.get("./src/day03/memory.txt");
  private static final String DISABLED_INSTRUCTION = "don't()";
  private static final String ENABLED_INSTRUCTION = "do()";

  public static void main(String[] args) throws IOException {
    final String memory = readMemory();
    final String multiplicationInstructionRegex = "mul\\((\\d{1,3}),(\\d{1,3})\\)";
    final String disabledInstructionRegex = "don't\\(\\)";
    final String enabledInstructionRegex = "do\\(\\)";

    final List<String> activeInstructions = List.of(
        multiplicationInstructionRegex,
        disabledInstructionRegex,
        enabledInstructionRegex);
    final Pattern pattern = Pattern.compile(String.join("|", activeInstructions));
    final Matcher matcher = pattern.matcher(memory);

    int result = 0;
    boolean isDisabled = false;

    while (matcher.find()) {
      String instruction = matcher.group(0);
      switch (instruction) {
        case DISABLED_INSTRUCTION:
          isDisabled = true;
          break;
        case ENABLED_INSTRUCTION:
          isDisabled = false;
          break;
        default:
          if (!isDisabled) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            result = result + (x * y);
          }
          break;
      }
    }

    System.out.printf("The result of adding up all of the multiplications is %d%n", result);
  }

  private static String readMemory() throws IOException {
    final StringBuilder memoryBuilder = new StringBuilder();
    Files.lines(MEMORY_FILE_PATH).forEach(line -> memoryBuilder.append(line));

    return memoryBuilder.toString();
  }
}
