package network.client;

import communication.gameinfo.GameInfo;

import communication.request.ConcreteHandMove;
import communication.request.ConcretePutStone;
import communication.request.ConcreteTableMove;
import game.Coordinate;
import game.Stone;
import java.util.Map;
import java.util.Scanner;
import network.server.RummiServer;
import view.DemoView;

public class Controller {

  private RummiClient client;
  DemoView view;

  public Controller(DemoView view) {
    this.view = view;
  }

  public void send() {
    //this.client.setSendToServer((Request) new Timer(InfoID.DRAW));
  }

  public void host(String name, int age, int numberOfPlayers) {
    new RummiServer(numberOfPlayers).start();
    join(name, age, "localhost");
  }

  public void join(String nane, int age, String serverIP) {
    client = new RummiClient(nane, age, serverIP);
    client.setGameInfoHandler(new GameInfoHandler(this));
    client.start();
  }

  public void printGame() {
    view.printGame();
  }

  void setTable(int width, int height, Map<Coordinate, Stone> stones) {
    view.setTable(width, height, stones);
  }

  void setPlayerHand(int width, int height, Map<Coordinate, Stone> stones) {
    view.setPlayerHand(width, height, stones);
  }

  public void moveStoneOnTable(int x1, int y1, int x2, int y2) {
    client.qeueRequest(new ConcreteTableMove(new Coordinate(x1, y1), new Coordinate(x2, y2)));
  }

  public void moveStoneFromHand(int x1, int y1, int x2, int y2) {
    client.qeueRequest(new ConcretePutStone(new Coordinate(x1, y1), new Coordinate(x2, y2)));
  }

  public void moveStoneOnHand(int x1, int y1, int x2, int y2) {
    client.qeueRequest(new ConcreteHandMove(new Coordinate(x1, y1), new Coordinate(x2, y2)));
  }

  public static void main(String[] args) {
    Controller test = new Controller(new DemoView());

    GameInfo o;
    int alfa = 0;
    while (alfa < 9) {
      o = test.client.getGameInfo();
      if (o != null) {
        System.out.println(o);
        alfa++;
      }
      Scanner scanner = new Scanner(System.in);
      System.out.println("write command");
      String input = scanner.nextLine();
      if (input.equals("send")) {
        test.send();
        System.out.println("SENT SUCCESSFUL");
      }
    }
  }
}
