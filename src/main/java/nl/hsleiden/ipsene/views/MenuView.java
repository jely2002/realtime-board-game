package nl.hsleiden.ipsene.views;

import java.io.FileNotFoundException;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.controllers.LobbyController;
import nl.hsleiden.ipsene.exceptions.GameNotFoundException;
import nl.hsleiden.ipsene.exceptions.ServerConnectionException;
import nl.hsleiden.ipsene.interfaces.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuView implements View {

  private static final Logger logger = LoggerFactory.getLogger(MenuView.class.getName());

  private final int WIDTH = 1600;
  private final int HEIGHT = 900;

  private String labelCSS =
      "-fx-font-family: 'Comic Sans MS';-fx-font-size: 30; -fx-background-color: #FFFFFF";
  private String textFieldCSS = "-fx-font-size: 20";
  private String quitButtonCSS =
      "-fx-font-family: 'Comic Sans MS';-fx-font-size: 30; -fx-background-color: #808080;"
          + " -fx-padding: 10 255";

  private Button joinButton;
  private Button hostButton;
  private Button quitButton;

  private TextField joinLobbyIDInput;
  private Label hostLobbyIDDisplay;
  private Stage primaryStage;

  private Label joinInputErrorLabel;
  private Label hostInputErrorLabel;

  private final LobbyController lobbyController;

  public MenuView(Stage primaryStage, LobbyController lobbyController) {
    this.primaryStage = primaryStage;
    this.lobbyController = lobbyController;
    try {
      loadPrimaryStage(createPane());
    } catch (FileNotFoundException e) {
      logger.error(e.getMessage(), e);
    }
    lobbyController.registerObserver(this);

    // todo remove this when done debugging
        try {
          // lobbyController.join("76430");
          lobbyController.host();
          toLobby();
        } catch (ServerConnectionException e) {
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
    ViewHelper.setNodeCoordinates(joinRect, 500, 200);

    Rectangle hostRect = rectangleBuilder();
    ViewHelper.setNodeCoordinates(hostRect, 500, 500);

    this.hostLobbyIDDisplay = lobbyIDLabelBuilder("-");
    ViewHelper.setNodeCoordinates(hostLobbyIDDisplay, 510, 560);

    this.joinLobbyIDInput = textFieldBuilder();
    ViewHelper.setNodeCoordinates(joinLobbyIDInput, 510, 260);

    Label joinLobbyIDHeader = ViewHelper.headerLabelBuilder("JOIN: LobbyID");
    ViewHelper.setNodeCoordinates(joinLobbyIDHeader, 510, 210);

    Label hostLobbyIDHeader = ViewHelper.headerLabelBuilder("HOST");
    ViewHelper.setNodeCoordinates(hostLobbyIDHeader, 510, 510);

    this.joinButton = buttonBuilder("JOIN");
    ViewHelper.setNodeCoordinates(joinButton, 1110, 250);
    this.joinButton.addEventFilter(MouseEvent.MOUSE_CLICKED, joinButtonClicked);

    this.hostButton = buttonBuilder("HOST");
    ViewHelper.setNodeCoordinates(hostButton, 1110, 550);
    this.hostButton.addEventFilter(MouseEvent.MOUSE_CLICKED, hostButtonClicked);

    this.quitButton = quitButtonBuilder();
    ViewHelper.setNodeCoordinates(quitButton, 500, 800);
    this.quitButton.addEventFilter(MouseEvent.MOUSE_CLICKED, quitButtonClicked);

    ImageView imageView = ViewHelper.createLogo(null, 150);
    ViewHelper.applyDropShadow(imageView);
    ViewHelper.setNodeCoordinates(imageView, 677, 20);

    this.joinInputErrorLabel = errorLabelBuilder();
    ViewHelper.setNodeCoordinates(joinInputErrorLabel, 520, 320);

    this.hostInputErrorLabel = errorLabelBuilder();
    ViewHelper.setNodeCoordinates(hostInputErrorLabel, 520, 620);

    pane.getChildren()
        .addAll(joinRect, hostRect, joinLobbyIDInput, hostLobbyIDDisplay, joinLobbyIDHeader);
    pane.getChildren().addAll(hostLobbyIDHeader, joinButton, hostButton, quitButton);
    pane.getChildren().addAll(imageView, joinInputErrorLabel, hostInputErrorLabel);
    return pane;
  }

  private Rectangle rectangleBuilder() {
    Rectangle rect = new Rectangle();

    rect.setWidth(600);
    rect.setHeight(150);
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

  private Label lobbyIDLabelBuilder(String txt) {
    Label lbl = new Label();

    lbl.setStyle(labelCSS);
    lbl.setPrefWidth(580);
    lbl.setPrefHeight(50);
    lbl.setText(txt);

    return lbl;
  }

  private Button buttonBuilder(String txt) {
    Button btn = new Button();

    btn.setPrefWidth(100);
    btn.setPrefHeight(100);
    btn.setText(txt);
    btn.setStyle("-fx-font-size: 20; -fx-background-color: #00FF00");
    ViewHelper.applyDropShadow(btn);

    return btn;
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

  private Label errorLabelBuilder() {
    Label lbl = new Label();

    lbl.setTextFill(Color.RED);
    lbl.setStyle("-fx-font-size: 15");

    lbl.setText("");
    ViewHelper.applyDropShadow(lbl);

    return lbl;
  }

  EventHandler<MouseEvent> quitButtonClicked =
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
          logger.debug("Quit button has been pressed");
          lobbyController.quit();
        }
      };

  public EventHandler<MouseEvent> joinButtonClicked =
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          String token = joinLobbyIDInput.getText();
          logger.debug("Join has been clicked");
          try {
            lobbyController.join(token);
            toLobby();
          } catch (GameNotFoundException | ServerConnectionException e) {
            logger.warn(e.getMessage(), e);
            joinInputErrorLabel.setText(e.getMessage());
          }
        }
      };

  EventHandler<MouseEvent> hostButtonClicked =
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          logger.debug("Host has been clicked");
          try {
            lobbyController.host();
            toLobby();
          } catch (ServerConnectionException e) {
            hostInputErrorLabel.setText(e.getMessage());
          }
        }
      };

  private void toLobby() {
    new LobbyView(primaryStage, lobbyController);
  }

  public void update() {}
}
