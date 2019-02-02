package constants;

public class GlobalConstants {
  private static final int MIN_PLAYERS = 2;
  private static final int MAX_PLAYERS = 4;
  private static final int FIRST_STONES = 14;
  private static final int MIN_FIRST_MOVE_POINTS = 30;
  private static final int JOKERPOINTS = 10;

  public static int getMinPlayers() {
    return MIN_PLAYERS;
  }

  public static int getMaxPlayers() {
    return MAX_PLAYERS;
  }

  public static int getFirstStones() {
    return FIRST_STONES;
  }

  public static int getMinFirstMovePoints() {
    return MIN_FIRST_MOVE_POINTS;
  }

  public static int getJokerPoints() { return JOKERPOINTS; }
}
