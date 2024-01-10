package groom.tricountClone.domain.login;

import groom.tricountClone.domain.member.Member;
import groom.tricountClone.domain.member.MemberRepository;
import java.sql.SQLException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  private final static MemberRepository memberRepository = MemberRepository.getMemberRepository();

  // login 가능하면 member 리턴 불가능하면 null
  public Member login(String userId, String password) {
    return memberRepository.findByUserId(userId)
        .filter(member -> member.getPassword().equals(password)).orElse(null);
  }
}
