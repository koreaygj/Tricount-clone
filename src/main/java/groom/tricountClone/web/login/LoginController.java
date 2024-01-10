package groom.tricountClone.web.login;

import groom.tricountClone.domain.login.LoginService;
import groom.tricountClone.domain.member.Member;
import groom.tricountClone.web.sessionConst;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

  private final LoginService loginService;

  //login Form 매핑
  @GetMapping
  public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
    return "login/loginForm";
  }

  //login 기능
  @PostMapping
  public String login(@ModelAttribute("loginForm") LoginForm loginForm,
      BindingResult bindingResult, HttpSession session) {
    Member loginMember = loginService.login(loginForm.getUserId(), loginForm.getPassword());
    if (loginMember == null) {
      bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
      return "login/loginForm";
    }
    session.setAttribute(sessionConst.LOGIN_MEMBER.getName(), loginMember);
    return "redirect:/";
  }

}
