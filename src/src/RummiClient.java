package network.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class RummiClient extends Thread {

  //Connection variables
  private boolean connected;
  private Socket server;
  private String serverIPAddress;


  //Player ID
  private String username;
  private Integer age;

  //SEND AND RECEIVE VARIABLES
  private GameInfo forwardToController;
  private Request sendToServer;
  private boolean readyToSend;


  //CREATE A NEW CLIENT WITH USERNAME, AGE AND IP ADDRESS OF THE SERVER("localhost" or ip)
  public RummiClient(String username, Integer age, String serverIPAddress) {
    this.username = username;
    this.age = age;
    this.serverIPAddress = serverIPAddress;
    connected = true;
  }

  @Override
  public void run() {
    try {
      server = new Socket(serverIPAddress, 3141);
      // Add a listener to this Client
      ClientListener listener = new ClientListener(server, this);
      listener.start();
      // Create the ObjectSender to the Server
      ObjectOutputStream outToServer = new ObjectOutputStream(server.getOutputStream());
      synchronized (this) {
        //As long as the Client is connected to the Server
        while (connected) {
          //Wait until you get the GREEN LIGHT
          // so you can send it to the Server
          while (!readyToSend) { // readyToSend = true?? where??
            try {
              wait();
            } catch (InterruptedException e) {
              connected = false;
            }
          }
          outToServer.writeObject(this.sendToServer);
          outToServer.flush();
          readyToSend = false;
          this.sendToServer = null;
        }
        //NOT CONNECTED ANYMORE
        outToServer.close();
        server.close();
      }
    } catch (UnknownHostException e) {
      e.printStackTrace(); // ????
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  //The Controller gives to RummiClient a package
  // to send to Server
  public synchronized void setSendToServer(Request sendToServer) {
    this.sendToServer = sendToServer;
    this.readyToSend = true;
    notifyAll();
  }

  //The Client Listener recieves "gameInfo" from Server
  // so it notifies the RummiClient to forward this message to Controller
  synchronized void setForwardToController(GameInfo gameInfo) {
    if (forwardToController != null) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println(gameInfo);
    this.forwardToController = gameInfo;
  }


  // access from Controller to get the recieved package (GameInfo)
  public synchronized GameInfo getGameInfo() {
    GameInfo send = forwardToController;
    this.forwardToController = null;
    notifyAll();
    return send;
  }

  public static void main(String[] args) {
    RummiClient client = new RummiClient("angelos", 21, "localhost");
    client.start();
    System.out.println("READY TO SEND");
    Scanner sc = new Scanner(System.in);
    String input;
    while ((input = sc.nextLine()).equals("send")) {
      Request send = new Concrete_Hand_Move();
      client.setSendToServer(send);
    }

  }



} // END OF RummiClient Class
