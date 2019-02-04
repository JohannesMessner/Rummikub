package view;

public class ViewConstants {

  //Error-Messages
  static final String SERVER_NOT_AVAILABLE_ERROR = "Error!" +
          " Server is not available due to lack of host or not enough players in the game";
  static final String CONNECTION_REJECTED_ERROR = "The Host has rejected the connection." +
          " There might be no spot left in the game!";
  static final String MULTIPLE_HOSTS_ON_SINGLE_MACHINE_ERROR = "You are already hosting a Server!";
  static final String IP_NOT_DETERMINED_ERROR = "The IP address of a host could not be determined.";

  //FXML-files
  public static final String START_FXML = "login/start.fxml";
  public static final String WAIT_FXML = "lobby/wait.fxml";
  public static final String GAME_FXML = "game/game.fxml";
  public static final String ERROR_FXML = "error/error.fxml";
  public static final String WINNER_FXML = "ranking/winner.fxml";
  public static final String HELP_FXML = "instruction/help.fxml";

  //Sound-files
  public static final String START_MUSIC_MP3 = "startMusic.mp3";
  public static final String WAITING_MUSIC_MP3 = "startMusic.mp3";
  public static final String PICK_UP_STONE_MP3 = "pickupStone.mp3";
  public static final String DROP_STONE_MP3 = "dropStone.mp3";
  public static final String DRAW_STONE_MP3 = "drawStone.mp3";

  //Styling of text-fields
  public static final String ERROR_STYLE = "-fx-border-color: red ; -fx-border-width: 2px ;";

  //Styling of the Player
  public static final String CURRENTLY_PLAYING_STYLE = "-fx-border-color: #3BBA8F;";
  public static final String NOT_CURRENTLY_PLAYING_STYLE = "-fx-border-color: black;";
  public static final String NO_PLAYER_SYMBOL = "?";
  public static final String CURRENTLY_PLAYING_OPPONENT_STYLE = "-fx-background-color: #3BBA8F";
  public static final String NOT_CURRENTLY_PLAYING_OPPONENT_STYLE = "-fx-background-color: black";

  //Styling of the Stones
  public static final String STONE_FORMAT = "stoneFormat";
  public static final String STONE_STYLE = "-fx-background-color: none";
  public static final String STONE_WHILE_DRAGGING_STYLE = "-fx-background-color: #FFFFFF44";
  public static final String MULTIPLE_STONES_IMAGE = "images/MultiStoneDragView.png";
  public static final String STONE_BACKGROUND_IMAGE = "test/StoneBackground.png";

  //Joker
  public static final String JOKER_BACKGROUND_STYLE = "jokerBackground";
  public static final String JOKER_SYMBOL = "J";

  //Cell-Styles
  public static final String CELL_STYLE = "cell";
  public static final String SHADOW_STYLE = "shadow";
  public static final String STONE_VALUE_STYLE = "stoneValue";

  //ID's
  public static final String HAND_GRID_ID = "handGrid";
  public static final String TABLE_GRID_ID = "tableGrid";

  //Networking
  public static final String LOCAL_IP = "localhost";
}
