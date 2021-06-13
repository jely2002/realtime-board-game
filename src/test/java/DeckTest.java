import nl.hsleiden.ipsene.exceptions.OverdrawException;
import nl.hsleiden.ipsene.models.Card;
import nl.hsleiden.ipsene.models.Deck;
import nl.hsleiden.ipsene.models.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for the Deck class
 * @author Mees
 */

@ExtendWith(MockitoExtension.class)
public class DeckTest {

    private Deck deck;
    private final int AMOUNT_OF_PLAYERS = 4;

    @Mock
    Game game;

    @BeforeEach
    public void setup() {
        deck = new Deck(AMOUNT_OF_PLAYERS, game);
    }

    @Test
    @DisplayName("Should be able to draw all 52 cards from the deck")
    public void generatedTokenTest() throws OverdrawException {
        Card drawnCard;
        for(int i = 0; i < 52; i++) {
            drawnCard = deck.drawCard();
            assertThat(drawnCard, notNullValue());
        }
    }

    @Test
    @DisplayName("Should serialize to a firebase list of hashmaps")
    public void serializeTest() {
        List<Map<String, Object>> serializedResult;

        serializedResult = deck.serialize();

        assertThat(serializedResult, notNullValue());
        // Test if all cards are present
        assertThat(serializedResult.size(), equalTo(52));

        // Test if all cards are serialized properly
        for(Map<String, Object> serializedCard : serializedResult) {
            assertThat(serializedCard.containsKey("value"), equalTo(true));
            assertThat(serializedCard.containsKey("type"), equalTo(true));
        }
    }

}
