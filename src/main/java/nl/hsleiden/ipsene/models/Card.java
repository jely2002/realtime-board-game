package nl.hsleiden.ipsene.models;

import java.util.HashMap;

import nl.hsleiden.ipsene.application.GameController;
import nl.hsleiden.ipsene.views.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

interface Playable {
  void play(Player player, Pawn pawn, Card card);
}

public class Card implements Model {

  private static final Logger logger = LoggerFactory.getLogger(Card.class);

  public final int steps;

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

  /**
   * calls the appropriate method for this card to be played
   *
   * @param player the player playing this card
   * @param pawn the pawn the card was used on
   */
  public void play(Player player, Pawn pawn) {
    onPlay.play(player, pawn, this);
  }

  private static void playSpawnCard(Player player, Pawn pawn, Card card) {
    logger.debug("spawn card played");
  }

  private static void playSubCard(Player player, Pawn pawn, Card card) {
    logger.debug("sub card played");
  }

  private static void playSpawnStep1Card(Player player, Pawn pawn, Card card) {
    logger.debug("step1 card played");
  }

  private static void playStep7Card(Player player, Pawn pawn, Card card) {
    logger.debug("step7 card played");
  }

  private static void playStep4Card(Player player, Pawn pawn, Card card) {
    logger.debug("step4 card played");
  }

  private static void playStepNCard(Player player, Pawn pawn, Card card) {
    logger.debug("n card played with value: {}", card.steps);
  }

  /** types: "spawn": 0 "sub": 1 "spawn_step_1": 2 "step_7": 3 "step_4": 4 "step_n": 5 */
  private static final HashMap<CardType, Playable> onPlayActions =
      new HashMap<CardType, Playable>();

  static {
    onPlayActions.put(CardType.SPAWN, Card::playSpawnCard);
    onPlayActions.put(CardType.SUB, Card::playSubCard);
    onPlayActions.put(CardType.SPAWN_STEP_1, Card::playSpawnStep1Card);
    onPlayActions.put(CardType.STEP_7, Card::playStep7Card);
    onPlayActions.put(CardType.STEP_4, Card::playStep4Card);
    onPlayActions.put(CardType.STEP_N, Card::playStepNCard);
  }

  @Override
  public void registerObserver(View v) {}

  @Override
  public void unregisterObserver(View v) {}

  @Override
  public void notifyObservers() {}
}
