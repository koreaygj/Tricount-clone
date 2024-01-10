package groom.tricountClone.domain.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Member {

  private long memberId;

  @NotEmpty
  private String userId;
  @NotEmpty
  private String password;
  @NotEmpty
  private String username;
}
