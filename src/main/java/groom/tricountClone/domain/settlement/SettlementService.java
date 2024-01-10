package groom.tricountClone.domain.settlement;

import groom.tricountClone.domain.member.Member;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SettlementService {

  private static final SettlementRepository settlementRepository = SettlementRepository.getSettlementRepository();

  public List<Settlement> getAllSettlementsForMembers(Member loginMember) {
    return settlementRepository.searchAllSettlementByMemberId(loginMember.getMemberId());
  }

  public Settlement saveSettlement(Settlement settlement, Member loginMember) {
    return settlementRepository.saveSettlement(settlement, loginMember);

  }
}
