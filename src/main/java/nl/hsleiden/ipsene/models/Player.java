package nl.hsleiden.ipsene.models;

import java.util.ArrayList;
import java.util.List;
import nl.hsleiden.ipsene.observers.PlayerObservable;
import nl.hsleiden.ipsene.observers.PlayerObserver;

public class Player implements PlayerObservable {
  private ArrayList<Card> cards;
  /**
   * index of the player in its team
   */
  private int playerIndex;
  private Team team;

  private int selectedPawnIndex = 0;
  private int selectedCardIndex = 0;

  /** should not be called manually, call through Team#createPlayers
   * @param team the players team
   * @param index the players index within its team
   */
  public Player(Team team, int index) {
    cards = new ArrayList<Card>();
    this.team = team;
    this.playerIndex = index;
  }
  public void doTurn() {
    // just zero for now, should use a callback from the view to get the clicked pawns index or something
    // todo set selected card and pawn before calling before getting the pawn and playing the card
    Pawn selectedPawn = team.getPawn(playerIndex, selectedPawnIndex);
    if (selectedPawn != null)
      playCard(selectedPawn);
    else {
      System.out.println("pawn not selected!!!");
    }
  }
  public void addCard(Card card) {
    if (card == null) System.out.println("Warning, card given to player with Player#addCard is null!!!");
    cards.add(card);
  }
  private void playCard(Pawn pawn) {
    Card c = cards.get(selectedCardIndex);
    c.play(this, pawn);
    cards.remove(c);
    // removed a card so call observers
    notifyAllObservers();
  }

  private List<PlayerObserver> observers = new ArrayList<PlayerObserver>();

  // Add an observer to the list
  public void register(PlayerObserver plo) {
    observers.add(plo);
  }
  // Signal all observers that something has changed.
  // Also send <<this>> object to the observers.
  public void notifyAllObservers() {
    for (PlayerObserver plo : observers) {
      plo.update((PlayerObserver) this);
    }
  }
}
