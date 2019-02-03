package game;

/**
 * Class representing a stone.
 * Each Stone has one of four colors and a value between 1 and 13.
 * There are two special Stones, the jokers.
 */
public class Stone {
  public enum Color { RED, BLACK, YELLOW, BLUE, JOKER }
  public static final int MAX_VALUE = 13;
  public static final int MIN_VALUE = 1;

  private final int JOKER_POINTS = 20;
  private final Color color;
  private final int number;

  public Stone(Color color, int number) {
    this.color = color;
    this.number = number;
  }

  public Stone() {
    color = Color.JOKER;
    number = JOKER_POINTS;
  }

  public Color getColor() {
    return color;
  }

  public int getNumber() {
    return number;
  }

  // Testmethods
  @Override
  public String toString() {
    return "(Color: " + color + ", " + "Number: " + " " + number +")";
  }
}