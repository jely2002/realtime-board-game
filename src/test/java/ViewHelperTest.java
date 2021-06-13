import com.sun.javafx.geom.Vec2d;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.scene.control.Label;
import nl.hsleiden.ipsene.views.ViewHelper;
import org.hamcrest.Matcher;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.matcher.control.LabeledMatchers;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the ViewHelper utility class
 *
 * @author Tim Misiewicz
 */
@ExtendWith(ApplicationExtension.class)
public class ViewHelperTest {

  @Test
  @DisplayName("fillCoordinate list should translate a json array to a coordinates list")
  public void testFillCoordinatesList() {
    // Set up
    String testPath = "/test-coordinates.json";
    ArrayList<Vec2d> result;
    ArrayList<Vec2d> expectedCoordinates = new ArrayList<>();
    expectedCoordinates.add(new Vec2d(1, 2));
    expectedCoordinates.add(new Vec2d(3, 4));

    // Act
    try {
      ViewHelper.fillCoordinateList(testPath);
      result = ViewHelper.coordinates;
    } catch (FileNotFoundException e) {
      result = null;
    }

    // Assert
    assertEquals(expectedCoordinates, result, "Should translate the JSON to a coordinates list");
  }

  @Test
  @DisplayName("PlayerTurnDisplay should throw exception when the player given is out of bounds")
  public void playerTurnsDisplayExceptionTest() {
    int PLAYER_ID = 5;

    String expectedMessage = "Unexpected value: " + PLAYER_ID;

    assertThrows(IllegalStateException.class, () -> ViewHelper.playersTurnDisplay(PLAYER_ID), expectedMessage);
  }

  @Test
  @DisplayName("PlayerTurnDisplay should return a label with the right properties")
  public void playerTurnsDisplayTest() {
    int PLAYER_ID = 3;
    Label resultLabel;

    resultLabel = ViewHelper.playersTurnDisplay(PLAYER_ID);

    FxAssert.verifyThat(resultLabel, LabeledMatchers.hasText("Player 4's turn"));
    assertTrue(resultLabel.getStyle().contains("#FFFF00"));
  }

}
