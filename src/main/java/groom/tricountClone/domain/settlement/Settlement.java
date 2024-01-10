package groom.tricountClone.domain.settlement;

import lombok.Data;

@Data
public class Settlement {

  private long settlementId;
  private String hostUsername;
  private String name;
  private String description;

}
