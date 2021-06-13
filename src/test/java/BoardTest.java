import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import nl.hsleiden.ipsene.models.Board;
import nl.hsleiden.ipsene.models.Pawn;
import nl.hsleiden.ipsene.models.PlayerColour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Board class
 *
 * @author Jelle Glebbeek
 */
public class BoardTest {

  private Board board;

  @BeforeEach
  public void setup() {
    board = Board.getInstance();
  }

  @Test
  @DisplayName("Should empty the end pools on emptyEndPools")
  public void emptyEndPoolsTest() {
    PlayerColour PLAYER_COLOUR = PlayerColour.BLUE;

    // Fill an end pool, so the player should win.
    for (int i = 0; i < 4; i++) {
      Pawn pawn = new Pawn(PLAYER_COLOUR, i);
      board.putPawnIntoEndPool(PLAYER_COLOUR, pawn);
    }

    // Clear the end pools
    board.emptyEndPools();

    // The game should thus not been won
    assertThat(board.hasTheGameBeenWon(), nullValue());
  }

  @Test
  @DisplayName("Should return the colour of the team that won if endPool is full")
  public void hasTheGameBeenWonTest() {
    PlayerColour EXPECTED_WINNER = PlayerColour.BLUE;

    PlayerColour PLAYER_COLOUR = PlayerColour.BLUE;
    for (int i = 0; i < 4; i++) {
      Pawn pawn = new Pawn(PLAYER_COLOUR, i);
      board.putPawnIntoEndPool(PLAYER_COLOUR, pawn);
    }

    assertThat(board.hasTheGameBeenWon(), equalTo(EXPECTED_WINNER));
  }

  @Test
  @DisplayName("Should return null if no player has won yet")
  public void hasTheGameNotBeenWonTest() {
    PlayerColour PLAYER_COLOUR = PlayerColour.BLUE;
    for (int i = 0; i < 3; i++) {
      Pawn pawn = new Pawn(PLAYER_COLOUR, i);
      board.putPawnIntoEndPool(PLAYER_COLOUR, pawn);
    }

    assertThat(board.hasTheGameBeenWon(), nullValue());
  }
}
