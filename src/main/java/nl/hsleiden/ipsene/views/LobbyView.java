package nl.hsleiden.ipsene.views;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class LobbyView {
  private final int WIDTH = 1600;
  private final int HEIGHT = 900;

  String labelCSS =
      "-fx-font-family: 'Comic Sans MS';-fx-font-size: 30; -fx-background-color: #FFFFFF";
  String headerCSS = "-fx-font-family: 'Comic Sans MS';-fx-font-size: 30;";
  String textFieldCSS = "-fx-font-size: 20";
  String playerDisplayCSS =
      "-fx-background-color: #444444; -fx-border-color: #111111; -fx-border-width: 5;"
          + " -fx-border-style: solid; -fx-font-size: 20; -fx-padding: 11 9";
  String quitButtonCSS =
      "-fx-font-family: 'Comic Sans MS';-fx-font-size: 30; -fx-background-color: #808080;"
          + " -fx-padding: 10 255";

  VBox playerDisplay;
  Button joinButton;
  Button hostButton;
  Button quitButton;
  TextField joinLobbyIDInput;
  PasswordField joinPasswordInput;
  Label hostLobbyIDDisplay;
  PasswordField hostPasswordInput;
  Stage primaryStage;

  Label joinInputErrorLabel;
  Label hostInputErrorLabel;

  public LobbyView(Stage primaryStage) {
    this.primaryStage = primaryStage;
    try {
      loadPrimaryStage(createPane());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
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

  private Pane createPane() throws FileNotFoundException {
    Pane pane = new Pane();

    Rectangle joinRect = rectangleBuilder();
    setNodeCoordinates(joinRect, 500, 200);

    Rectangle hostRect = rectangleBuilder();
    setNodeCoordinates(hostRect, 500, 500);

    this.hostLobbyIDDisplay = lobbyIDLabelBuilder("-");
    setNodeCoordinates(hostLobbyIDDisplay, 510, 560);

    this.joinLobbyIDInput = textFieldBuilder();
    setNodeCoordinates(joinLobbyIDInput, 510, 260);

    this.joinPasswordInput = passwordFieldBuilder();
    setNodeCoordinates(joinPasswordInput, 510, 360);

    this.hostPasswordInput = passwordFieldBuilder();
    setNodeCoordinates(hostPasswordInput, 510, 660);

    Label joinLobbyIDHeader = inputHeaderBuilder("JOIN: LobbyID");
    setNodeCoordinates(joinLobbyIDHeader, 510, 210);

    Label joinPasswordHeader = inputHeaderBuilder("Password (optional)");
    setNodeCoordinates(joinPasswordHeader, 510, 310);

    Label hostLobbyIDHeader = inputHeaderBuilder("HOST");
    setNodeCoordinates(hostLobbyIDHeader, 510, 510);

    Label hostPasswordHeader = inputHeaderBuilder("Password (optional)");
    setNodeCoordinates(hostPasswordHeader, 510, 610);

    this.joinButton = buttonBuilder("JOIN");
    setNodeCoordinates(joinButton, 1110, 350);

    this.hostButton = buttonBuilder("HOST");
    setNodeCoordinates(hostButton, 1220, 650);

    this.playerDisplay = vboxBuilder(playerListBuilder());
    setNodeCoordinates(playerDisplay, 1110, 500);

    this.quitButton = quitButtonBuilder();
    setNodeCoordinates(quitButton, 500, 800);

    Image image = new Image(new FileInputStream("keez.png"));
    ImageView imageView = new ImageView(image);
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(150);
    applyDropShadow(imageView);
    setNodeCoordinates(imageView, 677, 20);

    this.joinInputErrorLabel = errorLabelBuilder();
    setNodeCoordinates(joinInputErrorLabel, 520, 420);

    this.hostInputErrorLabel = errorLabelBuilder();
    setNodeCoordinates(hostInputErrorLabel, 520, 720);

    pane.getChildren()
        .addAll(
            joinRect,
            hostRect,
            joinLobbyIDInput,
            joinPasswordInput,
            hostPasswordInput,
            hostLobbyIDDisplay,
            joinLobbyIDHeader,
            joinPasswordHeader);
    pane.getChildren()
        .addAll(
            hostLobbyIDHeader,
            hostPasswordHeader,
            joinButton,
            hostButton,
            playerDisplay,
            quitButton);
    pane.getChildren().addAll(imageView, joinInputErrorLabel, hostInputErrorLabel);
    return pane;
  }

  public void setNodeCoordinates(Node node, int x, int y) {
    node.setTranslateX(x);
    node.setTranslateY(y);
  }

  private Rectangle rectangleBuilder() {
    Rectangle rect = new Rectangle();

    rect.setWidth(600);
    rect.setHeight(250);
    rect.setFill(Color.GREY);
    return rect;
  }

  private TextField textFieldBuilder() {
    TextField txtFld = new TextField();
    final int HEIGHT = 50;
    final int WIDTH = 580;

    txtFld.setPrefWidth(WIDTH);
    txtFld.setMaxWidth(WIDTH);
    txtFld.setPrefHeight(HEIGHT);
    txtFld.setMaxHeight(HEIGHT);
    txtFld.setStyle(textFieldCSS);

    return txtFld;
  }

  private PasswordField passwordFieldBuilder() {
    PasswordField pwdFld = new PasswordField();
    final int HEIGHT = 50;
    final int WIDTH = 580;

    pwdFld.setPrefWidth(WIDTH);
    pwdFld.setMaxWidth(WIDTH);
    pwdFld.setPrefHeight(HEIGHT);
    pwdFld.setMaxHeight(HEIGHT);
    pwdFld.setStyle(textFieldCSS);

    return pwdFld;
  }

  private Label lobbyIDLabelBuilder(String txt) {
    Label lbl = new Label();

    lbl.setStyle(labelCSS);
    lbl.setPrefWidth(580);
    lbl.setPrefHeight(50);
    lbl.setText(txt);

    return lbl;
  }

  private Label inputHeaderBuilder(String txt) {
    Label lbl = new Label();

    lbl.setPrefWidth(580);
    lbl.setPrefHeight(50);
    lbl.setText(txt);
    lbl.setStyle(headerCSS);

    return lbl;
  }

  private Button buttonBuilder(String txt) {
    Button btn = new Button();

    btn.setPrefWidth(100);
    btn.setPrefHeight(100);
    btn.setText(txt);
    btn.setStyle("-fx-font-size: 20; -fx-background-color: #00FF00");
    applyDropShadow(btn);

    return btn;
  }

  private ArrayList<Node> playerListBuilder() {
    ArrayList<Node> nodes = new ArrayList<>();

    for (int i = 0; i < 4; i++) {
      int j = i + 1;
      Label label = new Label();
      label.setText("Player " + j);
      label.setTextFill(Color.WHITE);
      nodes.add(label);
    }

    return nodes;
  }

  private VBox vboxBuilder(ArrayList<Node> nodes) {
    VBox vbx = new VBox();

    for (Node node : nodes) {
      node.setStyle(playerDisplayCSS);
      vbx.getChildren().add(node);
    }

    return vbx;
  }

  private Button quitButtonBuilder() {
    Button btn = new Button();

    btn.setText("QUIT");
    btn.setStyle(quitButtonCSS);
    btn.prefWidth(600);
    btn.minWidth(600);
    btn.prefHeight(50);

    return btn;
  }

  private void applyDropShadow(Node node) {
    DropShadow shadow = new DropShadow();
    shadow.setColor(Color.BLACK);
    shadow.setRadius(2);
    shadow.setOffsetX(1);
    shadow.setOffsetX(1);
    node.setEffect(shadow);
  }

  private Label errorLabelBuilder() {
    Label lbl = new Label();

    lbl.setTextFill(Color.RED);
    lbl.setStyle("-fx-font-size: 15");
    lbl.setText("PLACEHOLDER -> set de text van deze label om een error aan de user weer te geven");
    applyDropShadow(lbl);

    return lbl;
  }
}
