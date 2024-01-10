package groom.tricountClone.domain.response;

import groom.tricountClone.domain.response.data.Status;
import java.util.List;
import lombok.Data;

@Data
public class SettlementResponse<T> {

  private Status status;
  private List<T> results;
  private Object data;

  public SettlementResponse() {
  }

  public SettlementResponse(int code, String message) {
    this.status = new Status(code, message);
  }

}
