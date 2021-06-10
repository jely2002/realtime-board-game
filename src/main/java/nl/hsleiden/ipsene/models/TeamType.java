package nl.hsleiden.ipsene.models;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum TeamType {
  RED("#ff0000"),
  GREEN("#00ff00"),
  BLUE("#0000ff"),
  YELLOW("#ffff00");

  private String colour;

  public String getCode() {
    return colour;
  }
  private TeamType(String code) {
    this.colour = code;
  }

  private static final Map<String, TeamType> lookup = new HashMap<String, TeamType>();
  static {
    for (TeamType s : EnumSet.allOf(TeamType.class)) lookup.put(s.getCode(), s);
  }

}
