package nl.hsleiden.ipsene.models;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/** types: "spawn": 0 "sub": 1 "spawn_step_1": 2 "step_7": 3 "step_4": 4 "step_n": 5 */
/* source: https://stackoverflow.com/questions/5021246/conveniently-map-between-enum-and-int-string */
public enum CardType {
  SPAWN(0),
  SUB(1),
  SPAWN_STEP_1(2),
  STEP_7(3),
  STEP_4(4),
  STEP_N(5);
  private int code;

  public int getCode() {
    return code;
  }

  /**
   * @return the amount of steps the card allows the pawn to take, returns 0 for N, spawn and sub
   *     cards
   */
  public int getSteps() {
    if (code == CardType.SPAWN_STEP_1.code) return 1;
    if (code == CardType.STEP_7.code) return 7;
    if (code == CardType.STEP_4.code) return 4;
    return 0;
  }

  public static CardType get(int code) {
    return lookup.get(code);
  }

  private static final Map<Integer, CardType> lookup = new HashMap<Integer, CardType>();

  static {
    for (CardType s : EnumSet.allOf(CardType.class)) lookup.put(s.getCode(), s);
  }

  private CardType(int code) {
    this.code = code;
  }
}
