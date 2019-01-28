package network.client;

import communication.gameinfo.*;
import view.Controller;

public class GameInfoHandler { //TODO FIGURE OUT A WAY TO BE NOT PUBLIC: PROBLEM: CLIENT-NETWORKCONTROLLER-GAMEINFOHANDLER

  private Controller controller;
//  private RummiController controller;

//  public GameInfoHandler(RummiController controller) {
//    this.controller = controller;
//  }

  public GameInfoHandler(Controller controller) {
    this.controller = controller;
  }

  void applyGameInfo(Object gameInfo) {
    switch (((GameInfo)gameInfo).getGameInfoID()) {
      // case CURRENT_PLAYER:
      case HAND:
        //System.out.println("handling hand");
        controller.setPlayerHand(((GridInfo) gameInfo).getGrid());
        return;
      case TABLE:
        //System.out.println("handling table");
        controller.setTable(((GridInfo) gameInfo).getGrid());
        return;
      case ERROR:
        //System.out.println("handling error");
        controller.showError(((ErrorInfo) gameInfo).getErrorMessage());
//        controller.notifyInvalidMove();
//        controller.notifyTurn();
        return;
      case BAG:
        //System.out.println("handling bag");
        controller.setBagSize(((BagInfo) gameInfo).getSize());
        return;
      case YOUR_TURN:
        System.out.println("handling yourturn");
        controller.notifyTurn();
        //controller.notifyCurrentPlayer(0);
        return;
      case HAND_SIZES:
        //System.out.println("handling handsizes");
        controller.setHandSizes(((HandSizesInfo) gameInfo).getHandSizes());
        return;
      case PLAYER_NAMES:
        //System.out.println("handling names");
        controller.setPlayerNames(((PlayerNamesInfo) gameInfo).getNames());
        return;
      case DRAW:
        //System.out.println("handling draw");
        break;
      case CURRENT_PLAYER:
        System.out.println("handling currentplayer");
        System.out.println("received " + ((CurrentPlayerInfo) gameInfo).getPlayerID() + " as current player");
        controller.notifyCurrentPlayer(((CurrentPlayerInfo) gameInfo).getPlayerID());
        return;
      case GAME_START:
        //System.out.println("handling gamestart");
        controller.notifyGameStart();
        return;
      case SERVER_NOT_AVAILABLE:
        controller.noServerAvailable();
        return;
      case RANK:
        controller.showRank(((RankInfo) gameInfo).getFinalRank());
//      case IP_ADDRESS:
//        System.out.println("handling ip");
//        controller.setIPAddress(((GameIPAddress) gameInfo).getIpAddress());
//        return;
//      case USERNAME:
//        System.out.println("handling username");
//          controller.setUsername(((GameUsernames) gameInfo).getUsername(), ((GameUsernames) gameInfo).getId());
      default:
    }
    System.out.println("Info handled");
  }


}
