package groom.tricountClone.web.expense;

import static groom.tricountClone.web.sessionConst.LOGIN_MEMBER;

import groom.tricountClone.domain.expense.Expense;
import groom.tricountClone.domain.expense.ExpenseService;
import groom.tricountClone.domain.member.Member;
import groom.tricountClone.domain.response.ExpenseResponse;
import groom.tricountClone.domain.response.data.Status;
import groom.tricountClone.domain.settlement.SettlementService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/settlement/{settlementId}/expense")
public class ExpenseController {

  private final SettlementService settlementService;
  private final ExpenseService expenseService;

  @GetMapping
  @ResponseBody
  public ExpenseResponse getAllExpense(HttpSession session,
      @PathVariable("settlementId") long settlementId) {
    Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER.getName());
    ExpenseResponse response = new ExpenseResponse();
    if (loginMember == null) {
      response.setStatus(new Status(401, "로그인이 필요합니다."));
      return response;
    }
    // 유효한 settlement인지
    if (settlementService.getSettlementById(settlementId) == null) {
      response.setStatus(new Status(404, "정산을 찾을 수 없습니다."));
      return response;
    }
    // 로그인한 멤버가 접근 가능한 settlment인지
    if (settlementService.checkMemberAccessSettlement(loginMember, settlementId) == false) {
      response.setStatus(new Status(401, "접근 권한이 없습니다."));
      return response;
    }
    // 해당 settlement에 expense들을 가져오기
    List<Expense> expenseList = expenseService.searchAllExpense(settlementId);
    if (expenseList == null) {
      response.setStatus(new Status(500, "DB에서 찾을 수 없습니다."));
      return response;
    }
    response.setStatus(new Status(200, "oK"));
    response.setResults(expenseList);
    return response;
  }


  @PostMapping("/save")
  @ResponseBody
  public ExpenseResponse saveExpense(HttpSession session,
      @PathVariable("settlementId") long settlementId, @RequestBody ExpenseDTO expenseDTO) {
    Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER.getName());
    ExpenseResponse response = new ExpenseResponse();
    if (loginMember == null) {
      response.setStatus(new Status(401, "로그인이 필요합니다."));
      return response;
    }
    // 유효한 settlement인지
    if (settlementService.getSettlementById(settlementId) == null) {
      response.setStatus(new Status(404, "정산을 찾을 수 없습니다."));
      return response;
    }
    // 로그인한 멤버가 접근 가능한 settlment인지
    if (settlementService.checkMemberAccessSettlement(loginMember, settlementId) == false) {

      response.setStatus(new Status(401, "접근 권한이 없습니다."));
      return response;
    }
    // 저장하기
    Expense expense = new Expense();
    expense.setMemberId(loginMember.getMemberId());
    expense.setSettlementId(settlementId);
    expense.setName(expenseDTO.getName());
    expense.setCost(expenseDTO.getCost());
    expense.setDate(expenseDTO.getDate());
    Expense savedExpense = expenseService.saveExpense(expense);
    if (savedExpense == null) {
      response.setStatus(new Status(500, "DB에서 찾을 수 없습니다."));
      return response;
    }
    log.info("savedExpense: {}", savedExpense);
    response.setStatus(new Status(200, "oK"));
    response.setData(savedExpense);
    return response;
  }
}
