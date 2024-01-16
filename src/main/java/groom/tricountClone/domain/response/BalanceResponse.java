package groom.tricountClone.domain.response;

import lombok.Data;

@Data
public class BalanceResponse {

  long senderUserNo;
  String senderUsername;
  int sendAmount;
  long receiverUserNo;
  String receiverUsername;

}
