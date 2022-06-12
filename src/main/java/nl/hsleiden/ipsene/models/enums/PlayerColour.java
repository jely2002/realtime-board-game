package nl.hsleiden.ipsene.models.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum PlayerColour {
  RED("#ff0000"),
  GREEN("#00ff00"),
  BLUE("#0000ff"),
  YELLOW("#ffff00");

  private final String colour;

  public String getCode() {
    return colour;
  }

  PlayerColour(String code) {
    this.colour = code;
  }

  private static final Map<String, PlayerColour> lookup = new HashMap<>();

  static {
    for (PlayerColour s : EnumSet.allOf(PlayerColour.class)) lookup.put(s.getCode(), s);
  }
}
