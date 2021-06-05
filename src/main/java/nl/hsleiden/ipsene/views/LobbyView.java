package nl.hsleiden.ipsene.views;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LobbyView {
  private final int WIDTH = 1600;
  private final int HEIGHT = 900;
  private Stage primaryStage;
  private final String RED = "#FF0000";
  private final String BLUE = "#0000FF";
  private final String GREEN = "#00FF00";
  private final String YELLOW = "#FFFF00";

  private Button player1Join;
  private Button player2Join;
  private Button player3Join;
  private Button player4Join;

  private Label waitingForPlayersLabel;

  public LobbyView(Stage primaryStage) throws FileNotFoundException {
    this.primaryStage = primaryStage;
    loadPrimaryStage(createPane());
  }

  private Pane createPane() throws FileNotFoundException {
    Pane pane = new Pane();

    // TODO: dit koppelen met Firebase voor aansturen van de view op de model
    boolean player1Available = true;
    boolean player2Available = true;
    boolean player3Available = false;
    boolean player4Available = true;
    String lobbyID = "123456";

    Label title = lobbyHeaderLabelBuilder(lobbyID);
    MenuView.setNodeCoordinates(title, 10, 10);

    Label player1Display = playerDisplayLabel("Player 1", player1Available, RED);
    MenuView.setNodeCoordinates(player1Display, 10, 150);

    Label player2Display = playerDisplayLabel("Player 2", player2Available, BLUE);
    MenuView.setNodeCoordinates(player2Display, 10, 295);

    Label player3Display = playerDisplayLabel("Player 3", player3Available, GREEN);
    MenuView.setNodeCoordinates(player3Display, 10, 520);

    Label player4Display = playerDisplayLabel("Player 4", player4Available, YELLOW);
    MenuView.setNodeCoordinates(player4Display, 10, 660);

    this.player1Join = buttonBuilder("Join", player1Available);
    MenuView.setNodeCoordinates(player1Join, 220, 150);

    this.player2Join = buttonBuilder("Join", player2Available);
    MenuView.setNodeCoordinates(player2Join, 220, 295);

    this.player3Join = buttonBuilder("Join", player3Available);
    MenuView.setNodeCoordinates(player3Join, 220, 520);

    this.player4Join = buttonBuilder("Join", player4Available);
    MenuView.setNodeCoordinates(player4Join, 220, 660);

    this.waitingForPlayersLabel = WaitingForPlayersLabelBuilder("Waiting for players");
    MenuView.setNodeCoordinates(waitingForPlayersLabel, 10, 790);
    WaitingForPlayersThread wfpt = new WaitingForPlayersThread(waitingForPlayersLabel);
    Thread wfptThread = new Thread(wfpt);
    wfptThread.setDaemon(true); // Zorgt ervoor dat deze thread samen afsluit met de View
    wfptThread.start();

    Image image = new Image(new FileInputStream("keez.png"));
    ImageView imageView = new ImageView(image);
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(400);
    MenuView.applyDropShadow(imageView);
    MenuView.setNodeCoordinates(imageView, 600, 200);

    pane.getChildren()
        .addAll(title, player1Display, player2Display, player3Display, player4Display);
    pane.getChildren().addAll(player1Join, player2Join, player3Join, player4Join);
    pane.getChildren().addAll(waitingForPlayersLabel, imageView);
    return pane;
  }

  private void loadPrimaryStage(Pane pane) {
    try {
      Pane root = pane;
      Scene scene = new Scene(root, WIDTH, HEIGHT);
      primaryStage.setScene(scene);
      primaryStage.setTitle("Keezbord");
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Hier moet je de parameter simpelweg de LobbyID uit firebase meegeven!
  private Label lobbyHeaderLabelBuilder(String lobbyID) {
    Label lbl = new Label();

    lbl.setText("LobbyID: " + lobbyID);
    lbl.setStyle("-fx-font-size: 75; -fx-font-family: 'Comic Sans MS'");
    MenuView.applyDropShadow(lbl);

    return lbl;
  }

  private Label WaitingForPlayersLabelBuilder(String txt) {
    Label lbl = new Label();

    lbl.setText(txt);
    lbl.setStyle("-fx-font-size: 75; -fx-font-family: 'Comic Sans MS'");
    MenuView.applyDropShadow(lbl);

    return lbl;
  }

  // De IsAvailable bool is om aan te geven of de player slot beschikbaar is of niet, ofwel, kan je
  // nog die slot
  // joinen of niet.
  // Deze bool moet je dus aansturen aan de hand van de model op firebase!
  //
  // De hex parameter is om de kleur van de speler aan te geven. Ik probeerde het te doen met
  // de Color.x class van JavaFX, maar dit werkte niet, dus dan maar zo!
  //
  // De hex waardes die je nodig hebt voor de player colors staan al in de final variables van deze
  // functie! lekker handig!

  private Label playerDisplayLabel(String txt, boolean isAvailable, String hex) {
    Label lbl = new Label();
    String avIndicator;

    if (isAvailable) {
      avIndicator = "#03a9f4";
    } else {
      avIndicator = "#FF0000";
    }

    lbl.setText(txt);
    lbl.setPrefWidth(200);
    lbl.setPrefHeight(125);
    lbl.setStyle(
        "-fx-font-size: 35; -fx-padding: 20; -fx-border-width: 10; "
            + "-fx-background-color: #AAAAAA; -fx-border-color: "
            + avIndicator
            + avIndicator
            + avIndicator
            + hex);

    return lbl;
  }

  // Stuur de IsAvailable bool aan a.d.h.v data uit firebase
  private Button buttonBuilder(String txt, boolean isAvailable) {
    Button btn = new Button();
    String bgColor;

    if (isAvailable) {
      bgColor = "#00FF00";
    } else {
      bgColor = "#FF0000";
    }

    btn.setPrefWidth(125);
    btn.setPrefHeight(125);
    btn.setText(txt);
    btn.setStyle("-fx-font-size: 20; -fx-background-color: " + bgColor);
    MenuView.applyDropShadow(btn);

    return btn;
  }

  public void update() throws FileNotFoundException {
    loadPrimaryStage(createPane());
  }
}
