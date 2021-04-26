package nl.hsleiden.ipsene.views;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.controllers.AccountController;

public class AccountView {

  // SCREENSIZE
  final double width = 500;
  final double height = 300;

  AccountController accountController = new AccountController();

  TextField usernameField = new TextField();
  PasswordField passwordField = new PasswordField();

  Stage primaryStage;
  EventHandler<MouseEvent> startGameEventHandler =
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
          // SpelbordView sbc = new SpelbordView(primaryStage);
        }
      };
  EventHandler<MouseEvent> submitButtonClicked =
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
          if (accountController.validateLogin(usernameField.getText(), passwordField.getText())) {
            System.out.println("LoginView - GREEN == GOOD LOGIN");
            usernameField.setStyle("-fx-background-color: #00AA00; ");
            loadPrimaryStageWithGridPane(createGridPaneWithStartButton());
          } else {
            System.out.println("LoginView - RED == NO GOOD ");
            usernameField.setStyle("-fx-background-color: #AA0000; ");
          }
        }
      };
  EventHandler<MouseEvent> clearButtonEventHandler =
      new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
          usernameField.setText("");
          passwordField.setText("");
          usernameField.setStyle("-fx-background-color: #FFFFFF; ");
          passwordField.setStyle("-fx-background-color: #FFFFFF; ");
        }
      };

  public AccountView(Stage primaryStage) {
    this.primaryStage = primaryStage;
    loadPrimaryStageWithGridPane(createInitialGridPane());
  }

  private void loadPrimaryStageWithGridPane(GridPane gp) {
    try {
      GridPane root = gp;
      Scene scene = new Scene(root, width, height);
      primaryStage.setScene(scene);
      primaryStage.setTitle("LOGIN PLEASE");
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private GridPane createInitialGridPane() {
    Text enterUsernameText = new Text("Enter username :");
    Text enterPasswordText = new Text("Enter password :");
    Button submitButton = new Button("Submit");
    Button clearButton = new Button("Clear");

    GridPane gridPane = new GridPane();
    gridPane.setPadding(new Insets(10, 10, 10, 10));
    gridPane.setVgap(5);
    gridPane.setHgap(5);
    gridPane.setAlignment(Pos.CENTER);

    gridPane.add(enterUsernameText, 0, 0);
    gridPane.add(usernameField, 1, 0);
    gridPane.add(enterPasswordText, 0, 1);
    gridPane.add(passwordField, 1, 1);
    gridPane.add(submitButton, 0, 2);
    gridPane.add(clearButton, 1, 2);

    submitButton.addEventFilter(MouseEvent.MOUSE_CLICKED, submitButtonClicked);
    clearButton.addEventFilter(MouseEvent.MOUSE_CLICKED, clearButtonEventHandler);

    return gridPane;
  }

  private GridPane createGridPaneWithStartButton() {

    Text loginSuccesText = new Text(" Logged in success ! ");
    Button startGameButton = new Button("StartGame");

    GridPane gridPane = new GridPane();
    gridPane.setMinSize(width, height);
    gridPane.setPadding(new Insets(10, 10, 10, 10));
    gridPane.setVgap(5);
    gridPane.setHgap(5);
    gridPane.setAlignment(Pos.CENTER);
    gridPane.add(loginSuccesText, 0, 0);
    gridPane.add(startGameButton, 2, 0);

    startGameButton.addEventFilter(MouseEvent.MOUSE_CLICKED, startGameEventHandler);

    return gridPane;
  }
}
