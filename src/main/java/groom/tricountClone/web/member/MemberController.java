package groom.tricountClone.web.member;

import groom.tricountClone.domain.member.Member;
import groom.tricountClone.domain.member.MemberService;
import java.sql.ResultSet;
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
@RequestMapping("/members")
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/add")
  public String addForm(@ModelAttribute("member") Member member) {
    return "members/addMemberForm";
  }

  @PostMapping("/add")
  public String saveMember(@ModelAttribute("member") Member member, BindingResult bindingResult)
      throws SQLException {
    if (bindingResult.hasErrors()) {
      return "members/addMemberForm";
    }
    memberService.registerNewMember(member);
    return "redirect:/";
  }


}
