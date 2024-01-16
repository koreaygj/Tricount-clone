package groom.tricountClone.web.settlement;

import static groom.tricountClone.web.sessionConst.LOGIN_MEMBER;

import groom.tricountClone.domain.member.Member;
import groom.tricountClone.domain.member.MemberService;
import groom.tricountClone.domain.response.SettlementResponse;
import groom.tricountClone.domain.response.data.Status;
import groom.tricountClone.domain.settlement.Settlement;
import groom.tricountClone.domain.settlement.SettlementService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/settlements")
public class SettlementController {

  private final SettlementService settlementService;
  private final MemberService memberService;

  @ResponseBody
  @GetMapping("/lists")
  public SettlementResponse getSettlements(HttpSession session) {
    Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER.getName());
    SettlementResponse response = new SettlementResponse();
    //TODO login 여부확인 filter화
    if (loginMember == null) {
      response.setStatus(new Status(401, "로그인이 필요합니다."));
      return response;
    }
    response.setStatus(new Status(200, "ok"));
    response.setResults(settlementService.getAllSettlementsForMembers(loginMember));
    return response;
  }

  @ResponseBody
  @PostMapping("/add")
  public SettlementResponse addSettlements(HttpSession session,
      @RequestBody SettlementDTO settlementRequest) {
    Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER.getName());
    SettlementResponse response = new SettlementResponse();
    //TODO login 여부확인 filter화
    if (loginMember == null) {
      response.setStatus(new Status(401, "로그인이 필요합니다."));
      return response;
    }
    Settlement settlement = new Settlement();
    settlement.setHostUsername(loginMember.getUsername());
    settlement.setName(settlementRequest.getName());
    settlement.setDescription(settlementRequest.getDescription());

    response.setStatus(new Status(200, "ok"));
    response.setData(settlementService.saveSettlement(settlement, loginMember));
    return response;
  }

  @ResponseBody
  @PostMapping("/join")
  public SettlementResponse joinMemberToSettlements(HttpSession session,
      @RequestBody JoinSettlementForm joinSettlementForm, BindingResult bindingResult) {
    Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER.getName());
    SettlementResponse response = new SettlementResponse();
    //TODO login 여부확인 filter화
    if (loginMember == null) {
      response.setStatus(new Status(401, "로그인이 필요합니다."));
      return response;
    }
    // 맴버가 있는지 확인
    Member joinMember = memberService.findMemberByUsername(joinSettlementForm.getUsername());
    if (joinMember == null) {
      bindingResult.reject("memberError", "can't find member");
    }
    // settlement가 있는지 확인
    if (settlementService.getSettlementById(joinSettlementForm.getSettlementId()) == null) {
      bindingResult.reject("settlement", "can't find settlement");
    }
    if (settlementService.checkMemberAccessSettlement(loginMember,
        joinSettlementForm.getSettlementId()) == false) {
      bindingResult.reject("access", "can't access");
    }
    if (settlementService.joinMemberToSettlement(joinMember, joinSettlementForm.getSettlementId())
        == false) {
      bindingResult.reject("sqlerror", "sql error");
    }
    if (bindingResult.hasErrors()) {
      response.setStatus(new Status(404, "can't find"));
      return response;
    }
    response.setStatus(new Status(200, "ok"));
    response.setResults(settlementService.getJoinMembers(joinSettlementForm.getSettlementId()));
    return response;
  }

  // get settle balance information response by joining member
  @ResponseBody
  @GetMapping("/balance")
  public SettlementResponse getSettlementBalance(HttpSession session,
      @RequestParam("settlementId") long settlementId) {
    Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER.getName());
    SettlementResponse response = new SettlementResponse();
    //TODO login 여부확인 filter화
    if (loginMember == null) {
      response.setStatus(new Status(401, "로그인이 필요합니다."));
      return response;
    }
    response.setStatus(new Status(200, "ok"));
    response.setResults(settlementService.getSettlementBalance(loginMember, settlementId));
    return response;
  }
}
