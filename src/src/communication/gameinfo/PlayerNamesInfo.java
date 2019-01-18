package communication.gameinfo;

import java.io.Serializable;
import java.util.List;

/** Game info that stores all player names for the player who request it. */
public class PlayerNamesInfo implements GameInfo, Serializable {
  private List<String> names;

  public PlayerNamesInfo(List<String> names) {
    this.names = names;
  }

  @Override public GameInfoID getGameInfoID() {
    return null;
  }

  public List<String> getNames() {
    return names;
  }
}