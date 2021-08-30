package model;

public enum CurrencyEnum {
  EUR,
  USD,
  RUB;

  public static boolean isExist(String str) {
    for (CurrencyEnum me : CurrencyEnum.values()) {
      if (me.name().equalsIgnoreCase(str))
        return true;
    }
    return false;
  }


}
