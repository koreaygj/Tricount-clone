package groom.tricountClone.domain.response;

import groom.tricountClone.domain.response.data.Status;
import java.util.List;
import lombok.Data;

@Data
public class ExpenseResponse<T> {

  private Status status;
  private List<T> results;
  private Object data;

  public ExpenseResponse() {
  }

  public ExpenseResponse(int code, String message) {
    this.status = new Status(code, message);
  }


  public ExpenseResponse(Status status) {
    this.status = status;
  }

}

