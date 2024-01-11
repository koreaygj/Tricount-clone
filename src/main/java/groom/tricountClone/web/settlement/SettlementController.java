package groom.tricountClone.web.settlement;

import static groom.tricountClone.web.sessionConst.LOGIN_MEMBER;

import groom.tricountClone.domain.member.Member;
import groom.tricountClone.domain.response.SettlementResponse;
import groom.tricountClone.domain.response.data.Status;
import groom.tricountClone.domain.settlement.Settlement;
import groom.tricountClone.domain.settlement.SettlementService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/settlements")
public class SettlementController {

  private final SettlementService settlementService;

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
}
