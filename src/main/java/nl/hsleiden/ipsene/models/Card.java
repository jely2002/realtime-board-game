package nl.hsleiden.ipsene.models;

import nl.hsleiden.ipsene.views.View;

import java.util.HashMap;

interface Playable {
  void play(Player player, Pawn pawn, Card card);
}

public class Card implements Model {

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
   * @param player the player playing this card
   * @param pawn the pawn the card was used on
   */
  public void play(Player player, Pawn pawn) {
    onPlay.play(player, pawn, this);
  }

  private static void playSpawnCard(Player player, Pawn pawn, Card card) {
    System.out.println("spawn");
  }

  private static void playSubCard(Player player, Pawn pawn, Card card) {
    System.out.println("sub");
  }

  private static void playSpawnStep1Card(Player player, Pawn pawn, Card card) {
    System.out.println("step1");
  }

  private static void playStep7Card(Player player, Pawn pawn, Card card) {
    System.out.println("step7");
  }

  private static void playStep4Card(Player player, Pawn pawn, Card card) {
    System.out.println("step4");
  }

  private static void playStepNCard(Player player, Pawn pawn, Card card) {
    System.out.println("n val: " + card.steps);
  }

  /** types: "spawn": 0 "sub": 1 "spawn_step_1": 2 "step_7": 3 "step_4": 4 "step_n": 5 */
  private static final HashMap<CardType, Playable> onPlayActions = new HashMap<CardType, Playable>();

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
