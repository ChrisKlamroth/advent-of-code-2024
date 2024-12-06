package day06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Map {
  private final List<List<MapSpace>> map;
  private final Guard guard;

  public static Map loadFromFile(Path path) throws IOException {
    final List<List<MapSpace>> map = new ArrayList<>();
    final String[] fileLines = Files.lines(path).toArray(String[]::new);
    Guard guard = null;

    for (int rowIndex = 0; rowIndex < fileLines.length; rowIndex++) {
      final String[] symbols = fileLines[rowIndex].split("");
      final List<MapSpace> mapRow = new ArrayList<>();

      for (int columnIndex = 0; columnIndex < symbols.length; columnIndex++) {
        final String symbol = symbols[columnIndex];
        if (Direction.isDirectionSymbol(symbol)) {
          guard = new Guard(symbol, columnIndex, rowIndex);
          mapRow.add(new MapSpace(columnIndex, rowIndex, guard));
        } else if (symbol.equals(Obstacle.SYMBOL)) {
          mapRow.add(new MapSpace(columnIndex, rowIndex, new Obstacle()));
        } else {
          mapRow.add(MapSpace.empty(columnIndex, rowIndex));
        }
      }
      map.add(mapRow);
    }

    if (guard == null) {
      throw new IllegalArgumentException("No guard found in the file");
    }

    return new Map(map, guard);
  }

  private Map(List<List<MapSpace>> map, Guard guard) {
    this.map = map;
    this.guard = guard;
  }

  public void predictGuardRoute() {
    final MapSpace currentMapSpace = this.getSpaceAt(this.guard.getPosition());
    try {
      final MapSpace nextMapSpace = this.getSpaceAt(this.guard.getPositionInFront());
      if (nextMapSpace.isEmpty()) {
        this.moveGuard(currentMapSpace, nextMapSpace);
        this.predictGuardRoute();
      } else {
        this.guard.turnRight();
        this.predictGuardRoute();
      }
    } catch (IndexOutOfBoundsException exception) {
      currentMapSpace.setAsVisited();
      this.guard.leave();
    }
  }

  public long countVisitedSpaces() {
    return this.map.stream()
        .flatMap(List::stream)
        .filter(mapSpace -> mapSpace.hasBeenVisited())
        .count();
  }

  private void moveGuard(MapSpace from, MapSpace to) {
    from.setAsVisited();
    to.setMapObject(this.guard);
    this.guard.moveTo(to.getPosition());
  }

  private MapSpace getSpaceAt(Position position) {
    return this.map.get(position.getY()).get(position.getX());
  }

  public void print() {
    for (final List<MapSpace> row : this.map) {
      for (final MapSpace mapSpace : row) {
        System.out.printf("%s", mapSpace);
      }
      System.out.println();
    }
  }
}
