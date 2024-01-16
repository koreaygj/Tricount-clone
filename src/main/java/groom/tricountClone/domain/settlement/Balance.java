package groom.tricountClone.domain.settlement;

import lombok.Data;

@Data
public class Balance {

  private long memberId;
  private String username;
  private int cost;

  public Balance(long memberId, String username) {
    this.memberId = memberId;
    this.username = username;
  }
}
