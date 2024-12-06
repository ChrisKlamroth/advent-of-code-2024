package day06;

public class Position {
  private final int x;
  private final int y;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Position(Position position) {
    this.x = position.getX();
    this.y = position.getY();
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  @Override
  public String toString() {
    return String.format("Position(x=%d, y=%d)", this.x, this.y);
  }
}
