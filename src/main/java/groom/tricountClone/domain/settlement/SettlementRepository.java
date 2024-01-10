package groom.tricountClone.domain.settlement;


import groom.tricountClone.domain.member.Member;
import groom.tricountClone.domain.repository.MainRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SettlementRepository {

  private static final MainRepository mainRepository = MainRepository.getMainRepository();

  @Getter
  private static final SettlementRepository settlementRepository = new SettlementRepository();


  public List<Settlement> searchAllSettlementByMemberId(long loginMemberId) {
    String sql = "select s.SettlementID, s.hostUsername, s.name, s.description from settlements s join settlementMembers sm on s.settlementID = sm.settlementID where sm.memberID = ? ";
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<Settlement> userSettlements = new ArrayList<>();
    try {
      con = mainRepository.getConnection();
      ps = con.prepareStatement(sql);
      ps.setLong(1, loginMemberId);
      rs = ps.executeQuery();
      while (rs.next()) {
        Settlement settlement = new Settlement();
        settlement.setSettlementsId(rs.getLong("settlementID"));
        settlement.setHostUsername(rs.getString("hostUsername"));
        settlement.setName(rs.getString("name"));
        settlement.setDescription(rs.getString("description"));
        log.info("id={}", settlement.getSettlementsId());
        log.info("hostusername={}", settlement.getHostUsername());
        log.info("name={}", settlement.getName());
        log.info("description={}", settlement.getDescription());
        userSettlements.add(settlement);
      }
      return userSettlements;
    } catch (SQLException e) {
      logForSQLException(e);
      return null;
    } finally {
      mainRepository.close(con, ps, rs);
    }
  }

  private static void logForSQLException(SQLException e) {
    log.error("DB error", e);
    log.error("SQLException Message: " + e.getMessage());
    log.error("SQLState: " + e.getSQLState());
    log.error("ErrorCode: " + e.getErrorCode());
  }
}
