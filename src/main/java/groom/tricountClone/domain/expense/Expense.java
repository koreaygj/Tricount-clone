package groom.tricountClone.domain.expense;

import lombok.Data;

@Data
public class Expense {

  private long expenseId;
  private long memberId;
  private long settlementId;
  private String name;
  private int cost;
  private String date;

}
