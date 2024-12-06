package day06;

public class MapSpace {
  private static final String EMPTY_SYMBOL = ".";
  private static final String VISITED_SYMBOL = "X";

  private final Position position;
  private MapObject mapObject;
  private boolean hasBeenVisited;

  public static MapSpace empty(int x, int y) {
    return new MapSpace(x, y, null);
  }

  public MapSpace(int x, int y, MapObject mapObject) {
    this(new Position(x, y), mapObject);
  }

  public MapSpace(Position position, MapObject mapObject) {
    this.position = position;
    this.mapObject = mapObject;
  }

  public Position getPosition() {
    return this.position;
  }

  public boolean isEmpty() {
    return this.mapObject == null;
  }

  public void setMapObject(MapObject mapObject) {
    this.mapObject = mapObject;
  }

  public boolean hasBeenVisited() {
    return this.hasBeenVisited;
  }

  public void setAsVisited() {
    this.mapObject = null;
    this.hasBeenVisited = true;
  }

  @Override
  public String toString() {
    if (this.isEmpty()) {
      return this.hasBeenVisited ? VISITED_SYMBOL : EMPTY_SYMBOL;
    } else {
      return this.mapObject.toString();
    }
  }
}
