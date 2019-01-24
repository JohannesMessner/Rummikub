package communication.gameinfo;

public enum GameInfoID {

  /**
   * Identifier for classes implementing the GameInfo-Interface.
   * Every implementation of GameInfo should have an GameInfoID associated with it.
   */

  TABLE,
  HAND,
  BAG,
  HAND_SIZES,
  PLAYER_NAMES,
  DRAW, // for shell,
  CURRENT_PLAYER,
  ERROR,
  YOUR_TURN,
  GAME_START,
  IP_ADDRESS,
  USERNAME,
  SERVER_NOT_AVAILABLE
}
