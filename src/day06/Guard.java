package day06;

public class Guard extends MapObject {
  private Direction direction;
  private Position position;

  public Guard(String symbol, int x, int y) {
    this(Direction.fromSymbol(symbol), new Position(x, y));
  }

  public Guard(Direction direction, Position position) {
    this.direction = direction;
    this.position = position;
  }

  public Direction getDirection() {
    return this.direction;
  }

  public Position getPosition() {
    return this.position;
  }

  public Position getPositionInFront() {
    return switch (this.direction) {
      case Direction.UP -> new Position(
          this.position.getX(),
          this.position.getY() - 1);
      case Direction.DOWN -> new Position(
          this.position.getX(),
          this.position.getY() + 1);
      case Direction.RIGHT -> new Position(
          this.position.getX() + 1,
          this.position.getY());
      case Direction.LEFT -> new Position(
          this.position.getX() - 1,
          this.position.getY());
    };
  }

  public void moveTo(Position position) {
    this.position = position;
  }

  public void leave() {
    this.position = null;
  }

  public void turnRight() {
    this.direction = switch (this.direction) {
      case Direction.UP -> Direction.RIGHT;
      case Direction.DOWN -> Direction.LEFT;
      case Direction.RIGHT -> Direction.DOWN;
      case Direction.LEFT -> Direction.UP;
    };
  }

  @Override
  public String toString() {
    return this.direction.toString();
  }
}
