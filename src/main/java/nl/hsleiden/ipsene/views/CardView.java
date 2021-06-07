package nl.hsleiden.ipsene.views;

import java.io.FileNotFoundException;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import nl.hsleiden.ipsene.controllers.CardController;

public class CardView implements View {

  private final int WIDTH = 1600;
  private final int HEIGHT = 250;

  private Stage primaryStage;

  CardController cardController;

  public CardView(Stage s) {
    primaryStage = s;
    loadPrimaryStage(createInitialPane());
    //    cardController = CardController.getInstance();
    //
    //    // PASS IT TO THE CONTROLLER WHO WILL PASS IT TO THE MODEL
    //    cardController.registerObserver(this);
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

  private Pane createInitialPane() {
    Pane pane = new Pane();
    return pane;
  }

  @Override
  public void update() throws FileNotFoundException {
    loadPrimaryStage(createInitialPane());
  }
}
