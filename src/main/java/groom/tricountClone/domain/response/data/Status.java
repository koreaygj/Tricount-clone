package groom.tricountClone.domain.response.data;

import lombok.Data;

@Data
public class Status {

  private int code;
  private String message;

  public Status(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
