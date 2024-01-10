package groom.tricountClone.web.login;

import lombok.Data;

// 로그인 폼(userId, password)입력받기 위한 객체
@Data
public class LoginForm {

  private String userId;
  private String password;

}
