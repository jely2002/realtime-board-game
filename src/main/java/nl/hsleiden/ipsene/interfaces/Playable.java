package nl.hsleiden.ipsene.interfaces;

import nl.hsleiden.ipsene.models.Card;
import nl.hsleiden.ipsene.models.Pawn;
import nl.hsleiden.ipsene.models.Player;

public interface Playable {
  void play(Player player, Card card);
}
