import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sun.javafx.geom.Vec2d;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import nl.hsleiden.ipsene.views.ViewHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the ViewHelper utility class
 *
 * @author Tim Misiewicz
 */
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

  // TODO add 2nd test

}
