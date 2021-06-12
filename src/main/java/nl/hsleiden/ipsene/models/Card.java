package nl.hsleiden.ipsene.models;

import com.google.cloud.firestore.DocumentSnapshot;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import nl.hsleiden.ipsene.interfaces.FirebaseSerializable;
import nl.hsleiden.ipsene.interfaces.Playable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Card implements FirebaseSerializable<Map<String, Object>> {

  private static final Logger logger = LoggerFactory.getLogger(Card.class.getName());

  public final int steps;
  private boolean isSelected = false;
  private Playable onPlay;
  private CardType type;

  /**
   * binds the appropriate method to be called when Card.play is called and determines the correct
   * steps to use * types: * "spawn": 0 * "sub": 1 * "spawn_step_1": 2 * "step_7": 3 * "step_4": 4 *
   * "step_n": 5
   *
   * @param type the type of card as an integer
   */
  public Card(CardType type, int steps) {
    this.type = type;
    this.steps = steps;
    onPlay = onPlayActions.get(type);
  }

  public boolean isSelected() {
    return isSelected;
  }

  public void setIsSelected(boolean selected) {
    isSelected = selected;
  }
  /**
   * calls the appropriate method for this card to be played
   *
   * @param player the player playing this card
   */
  public void play(Player player) {
    onPlay.play(player, this);
  }

  private static void playSpawnCard(Player player, Card card) {
    Pawn pawn = player.getSelectedPawn(true);
    pawn.takeOutOfPool();
    logger.debug("spawn card played");
  }

  private static void playSpawnStep1Card(Player player, Card card) {
    Pawn pawn = player.getSelectedPawn(true);
    // if pawn is out of pool move by 1 else take out of pool
    if (pawn.isOutOfPool()) pawn.addRelativeBoardPosition(1);
    else pawn.takeOutOfPool();

    logger.debug("step1 card played");
  }

  private static void playStep7Card(Player player, Card card) {
    Pawn pawn = player.getSelectedPawn(true);
    Pawn pawn2 = player.getSelectedPawn(false);
    // if pawn 2 was selected
    if (pawn2 != null) {
      // divide the amount to move
      pawn.addRelativeBoardPosition(3);
      pawn2.addRelativeBoardPosition(3);
    } else {
      // else move pawn by 7
      pawn.addRelativeBoardPosition(7);
    }
    logger.debug("step7 card played");
  }

  private static void playStep4Card(Player player, Card card) {
    Pawn pawn = player.getSelectedPawn(true);
    pawn.addRelativeBoardPosition(4);

    logger.debug("step4 card played");
  }

  private static void playStepNCard(Player player, Card card) {
    Pawn pawn = player.getSelectedPawn(true);
    pawn.addRelativeBoardPosition(card.steps);

    logger.debug("n card played with value: {}", card.steps);
  }

  /** types: "spawn": 0 "sub": 1 "spawn_step_1": 2 "step_7": 3 "step_4": 4 "step_n": 5 */
  private static final HashMap<CardType, Playable> onPlayActions =
      new HashMap<CardType, Playable>();

  static {
    onPlayActions.put(CardType.SPAWN, Card::playSpawnCard);
    onPlayActions.put(CardType.SPAWN_STEP_1, Card::playSpawnStep1Card);
    onPlayActions.put(CardType.STEP_7, Card::playStep7Card);
    onPlayActions.put(CardType.STEP_4, Card::playStep4Card);
    onPlayActions.put(CardType.STEP_N, Card::playStepNCard);
  }

  public CardType getType() {
    return type;
  }

  @Override
  public Map<String, Object> serialize() {
    LinkedHashMap<String, Object> serializedCard = new LinkedHashMap<>();
    serializedCard.put("type", this.type.toString());
    serializedCard.put("value", this.steps);
    return serializedCard;
  }

  @Override
  public void update(DocumentSnapshot document) {}
}
