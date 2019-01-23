package game;

import java.util.Map;

class Player {
  private final String name;
  private final int age;
  private RummiHand hand;
  private boolean hasPlayedFirstMove;

  Player(String name, int age) {
    this.name = name;
    this.age = age;
    hand = new RummiHand();
  }

  int getAge() {
    return age;
  }

  Map<Coordinate, Stone> getStones() {
    return hand.getStones();
  }

  /**
   * pushes the given stone into the next free position of the player's hand.
   *
   * @param stone the new stone to be pushed on hand
   */
  void pushStone(Stone stone) {
    hand.setStone(nextFreeCoordinate(hand.getStones()), stone);
  }

  private Coordinate nextFreeCoordinate(Map<Coordinate, Stone> stones){
    Coordinate coordinate;
    for (int row = 0; row < hand.getHeight(); row++){
      for (int col = 0; col < hand.getWidth(); col++){
        coordinate = new Coordinate(col, row);
        if (!stones.containsKey(coordinate)) {
          return coordinate;
        }
      }
    }
    throw new IndexOutOfBoundsException("Hand is full.");
  }

  /**
   * moves stone from the given sourcePosition to the given targetPosition o.
   * If a stone at the targetPosition already exist, it will be swapped.
   *
   * @param sourcePosition the position of the subject stone before moving it
   * @param targetPosition the position of the subject stone after moving it
   */
  void moveStone(Coordinate sourcePosition, Coordinate targetPosition) {
    // save the chosen stone
    Stone chosenStone = hand.removeStone(sourcePosition);
    // move the stone at targetPosition to the sourcePosition
    hand.setStone(sourcePosition, hand.removeStone(targetPosition));
    // move the chosen stone to the targetPosition
    hand.setStone(targetPosition, chosenStone);
  }

  /**
   * pops the stone at the given sourcePosition.
   *
   * @param sourcePosition the position of the wanted stone
   * @return the wanted stone
   */
  Stone popStone(Coordinate sourcePosition) {
    return hand.getStones().remove(sourcePosition);
  }

  int getHandSize() {
    return hand.size();
  }

  String getName() {
    return name;
  }

  // for test
  @Override public String toString() {
    return "Player(" + age + ")";
  }

  int getHandWidth() {
    return hand.getWidth();
  }

  int getHandHeight() {
    return hand.getHeight();
  }

  /** changes this player's state to hasPlayedFirstMove. */
  void playedFirstMove() {
    hasPlayedFirstMove = true;
  }

  /**
   * gives the state if this player has played the first move.
   *
   * @return true if only if this player has played the first move
   */
  boolean hasPlayedFirstMove() {
    return hasPlayedFirstMove;
  }

  /**
   * gives the sum of all of the points of stones on this player's hand.
   *
   * @return the sum of the points of stones on this player's hand
   */
  int getPoints() {
    // all points of stones represent a negative number
    return -hand.getStones().values().stream().mapToInt(Stone::getNumber).sum();
  }

  void sortHandByGroup() {
    hand.sortByGroup();
  }

  void sortHandByRun() {
    hand.sortByRun();
  }

  void clearHand() {
    hand.clear();
  }

  // tests sortHandByGroup and sortHandByRun
  public static void main(String[] args) {
    Player player = new Player("name1", 0);
    RummiBag bag = new RummiBag();
    for (int i = 0; i < 20; i++) {
      player.pushStone(bag.removeStone());
    }
    player.moveStone(new Coordinate(0, 0), new Coordinate(19, 1));
    player.moveStone(new Coordinate(3, 0), new Coordinate(3, 1));
    System.out.println("Normal: \n" + player.hand);
    player.sortHandByGroup();
    System.out.println("Group: \n" + player.hand);
    player.sortHandByRun();
    System.out.println("Run: \n" + player.hand);
    player.sortHandByGroup();
    System.out.println("Group: \n" + player.hand);
  }
}
