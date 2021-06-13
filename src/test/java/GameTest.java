import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import nl.hsleiden.ipsene.controllers.LobbyController;
import nl.hsleiden.ipsene.models.Game;
import nl.hsleiden.ipsene.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for the Game class
 *
 * @author Frank van den Broek
 */
@ExtendWith(MockitoExtension.class)
public class GameTest {

  private Game game;

  @Mock LobbyController lobbyController;

  @BeforeEach
  public void setup() {
    game = new Game(lobbyController);
  }

  @Test
  @DisplayName("Should generate a random 5 number token")
  public void generatedTokenTest() {
    String resultToken = game.getToken();
    assertThat(Integer.parseInt(resultToken), notNullValue());
    assertThat(resultToken, notNullValue());
    assertThat(resultToken.length(), equalTo(5));
  }

  @Test
  @DisplayName("Should generate 4 players")
  public void getAllPlayersTest() {
    int expectedSize = 4;

    ArrayList<Player> players = game.getAllPlayers();
    int resultSize = players.size();

    assertThat(resultSize, equalTo(expectedSize));

    // Test if the players have correct ID's
    for (int i = 0; i < expectedSize; i++) {
      assertThat(players.get(i).getId(), equalTo(i));
    }
  }
}
