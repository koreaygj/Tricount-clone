package groom.tricountClone.domain.settlement;

import groom.tricountClone.domain.expense.Expense;
import groom.tricountClone.domain.expense.ExpenseRepository;
import groom.tricountClone.domain.member.Member;
import groom.tricountClone.domain.response.BalanceResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SettlementService {

  private static final SettlementRepository settlementRepository = SettlementRepository.getSettlementRepository();
  private static final ExpenseRepository expenseRepository = ExpenseRepository.getExpenseRepository();

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

  public List<BalanceResponse> getSettlementBalance(Member loginMember, long settlementId) {
    List<Balance> members = settlementRepository.findJoinMembersBySettlementId(settlementId);
    List<Expense> expenses = expenseRepository.getAllExpensesBySettlementId(settlementId);
    List<BalanceResponse> balances = new ArrayList<>();
    int totalCost = 0;
    // calculate total cost & calculate each member's cost
    for (Expense expense : expenses) {
      totalCost += expense.getCost();
      for (Balance member : members) {
        if (expense.getMemberId() == member.getMemberId()) {
          member.setCost(member.getCost() + expense.getCost());
        }
      }
    }
    int balanceCost = totalCost / members.size();
    // each member's cost - balance cost
    for (Balance member : members) {
      member.setCost(member.getCost() - balanceCost);
    }
    // calculate each member's balance'
    for (int i = 0; i < members.size(); i++) {
      if (members.get(i).getCost() > 0) {
        Balance receiver = members.get(i);
        for (int j = 0; j < members.size(); j++) {
          if (i == j) {
            continue;
          }
          Balance sender = members.get(j);
          if (sender.getCost() < 0) {
            BalanceResponse br = new BalanceResponse();
            if (receiver.getCost() + sender.getCost() >= 0) {
              br.setSenderUserNo(sender.getMemberId());
              br.setSenderUsername(sender.getUsername());
              br.setSendAmount(Math.abs(sender.getCost()));
              br.setReceiverUserNo(receiver.getMemberId());
              br.setReceiverUsername(receiver.getUsername());
              receiver.setCost(receiver.getCost() + sender.getCost());
              sender.setCost(0);
              balances.add(br);
            } else {
              br.setSenderUserNo(sender.getMemberId());
              br.setSenderUsername(sender.getUsername());
              br.setSendAmount(Math.abs(receiver.getCost()));
              br.setReceiverUserNo(receiver.getMemberId());
              br.setReceiverUsername(receiver.getUsername());
              sender.setCost(sender.getCost() + receiver.getCost());
              receiver.setCost(0);
              balances.add(br);
            }
          }
        }
      }
    }
    return balances;
  }
}
