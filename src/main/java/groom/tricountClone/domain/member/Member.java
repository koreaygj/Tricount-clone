package groom.tricountClone.domain.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Member {

  private long id;

  @NotEmpty
  private String loginId;
  @NotEmpty
  private String password;
  @NotEmpty
  private String username;
}
