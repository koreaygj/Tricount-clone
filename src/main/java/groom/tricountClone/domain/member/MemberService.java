package groom.tricountClone.domain.member;

import java.sql.SQLException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

  private final static MemberRepository memberRepository = MemberRepository.getMemberRepository();

  public boolean registerNewMember(Member member) throws SQLException {
    return memberRepository.saveMember(member) == null ? false : true;
  }

}
