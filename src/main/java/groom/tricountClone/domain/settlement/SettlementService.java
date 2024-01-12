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
    settlementRepository.saveSettlement(settlement, loginMember);
    settlementRepository.addMemberToSettlement(settlement.getSettlementId(),
        loginMember.getMemberId());
    return settlement;
  }

  public Settlement getSettlementById(long settlementId) {
    return settlementRepository.searchSettlementBySettlementID(settlementId);
  }

  public boolean joinMemberToSettlement(Member joinMember, long settlementId) {
    return settlementRepository.joinMember(joinMember.getMemberId(), settlementId);
  }

  public boolean checkMemberAccessSettlement(Member loginMember, long settlementId) {
    return settlementRepository.findSettlementListByMemberId(loginMember.getMemberId())
        .stream().filter(id -> id.equals(settlementId)).count() != 0;
  }

  public List<String> getJoinMembers(long settlementId) {
    return settlementRepository.getJoinMembersBySettlementId(settlementId);
  }
}
