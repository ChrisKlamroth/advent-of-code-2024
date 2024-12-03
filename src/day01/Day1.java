import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 {
  private static final Path LOCATION_IDS_FILE_PATH = Paths.get("./src/day01/location-ids.txt");
  private static final String LOCATION_ID_SEPARATOR = "   ";

  public static void main(String[] args) throws IOException {
    final List<Integer> locationIdsLeft = new ArrayList<>();
    final List<Integer> locationIdsRight = new ArrayList<>();

    Files.lines(LOCATION_IDS_FILE_PATH)
        .map(line -> parseLocationIds(line))
        .forEach(locationIds -> {
          locationIdsLeft.add(locationIds.get(0));
          locationIdsRight.add(locationIds.get(1));
        });

    final int totalDistance = calculateTotalDistance(
        sort(locationIdsLeft),
        sort(locationIdsRight));

    System.out.printf("The total distance is %d%n", totalDistance);
  }

  private static List<Integer> parseLocationIds(String line) {
    return Arrays
        .asList(line.split(LOCATION_ID_SEPARATOR))
        .stream()
        .map(Integer::parseInt)
        .toList();
  }

  private static List<Integer> sort(List<Integer> locationIds) {
    return locationIds
        .stream()
        .sorted()
        .collect(Collectors.toList());
  }

  private static int calculateTotalDistance(List<Integer> locationIdsLeft, List<Integer> locationIdsRight) {
    if (locationIdsLeft.size() != locationIdsRight.size()) {
      throw new IllegalArgumentException("Both location-ID lists must have the same length");
    }

    int totalDistance = 0;

    for (int index = 0; index < locationIdsLeft.size(); index++) {
      int distance = Math.abs(locationIdsLeft.get(index) - locationIdsRight.get(index));
      totalDistance = totalDistance + distance;
    }

    return totalDistance;
  }
}
