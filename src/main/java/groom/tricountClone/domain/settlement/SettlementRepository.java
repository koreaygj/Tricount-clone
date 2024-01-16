package groom.tricountClone.domain.settlement;


import groom.tricountClone.domain.member.Member;
import groom.tricountClone.domain.repository.MainRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SettlementRepository {

  private static final MainRepository mainRepository = MainRepository.getMainRepository();

  @Getter
  private static final SettlementRepository settlementRepository = new SettlementRepository();

  //settlement save to DB table settlements
  public Settlement saveSettlement(Settlement settlement, Member loginMember) {
    String sql = "insert into settlements (hostUsername, name, description) values (?, ?, ?)";
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet generatedKey = null;
    try {
      con = mainRepository.getConnection();
      ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, loginMember.getUsername());
      ps.setString(2, settlement.getName());
      ps.setString(3, settlement.getDescription());
      int affectedRows = ps.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("Creating settlement failed, no rows affected.");
      }
      generatedKey = ps.getGeneratedKeys();
      if (generatedKey.next()) {
        settlement.setSettlementId(generatedKey.getLong(1));
        log.info("id={}", generatedKey.getLong(1));
      } else {
        throw new SQLException("Creating settlement failed, no ID obtained.");
      }
      return settlement;
    } catch (SQLException e) {
      logForSQLException(e);
      return null;
    } finally {
      mainRepository.close(con, ps, null);
    }
  }

  //getSettlementID
  public long getSettlementId() {
    String sql = "insert last_insert_id()";
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    long id = 0L;
    try {
      con = mainRepository.getConnection();
      ps = con.prepareStatement(sql);
      ps.executeQuery();
      if (rs.next()) {
        id = rs.getLong(1);
        log.info("id={}", id);
      }
      return id;
    } catch (SQLException e) {
      logForSQLException(e);
      return 0L;
    } finally {
      mainRepository.close(con, ps, rs);
    }
  }

  //add Member to settlement
  public boolean addMemberToSettlement(long settlementId, long memberId) {
    String sql = "insert into settlementMembers (settlementID, memberID) values (?, ?)";
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = mainRepository.getConnection();
      ps = con.prepareStatement(sql);
      ps.setLong(1, settlementId);
      ps.setLong(2, memberId);
      ps.executeUpdate();
      return true;
    } catch (SQLException e) {
      logForSQLException(e);
      return false;
    } finally {
      mainRepository.close(con, ps, null);
    }
  }

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
        settlement.setSettlementId(rs.getLong("settlementID"));
        settlement.setHostUsername(rs.getString("hostUsername"));
        settlement.setName(rs.getString("name"));
        settlement.setDescription(rs.getString("description"));
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

  public List<Long> findSettlementListByMemberId(long loginMemberId) {
    String sql = "select * from settlementMembers  where memberID = ?";
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<Long> userSettlements = new ArrayList<>();
    try {
      con = mainRepository.getConnection();
      ps = con.prepareStatement(sql);
      ps.setLong(1, loginMemberId);
      rs = ps.executeQuery();
      while (rs.next()) {
        Long settlementId = rs.getLong("settlementID");
        userSettlements.add(settlementId);
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

  public Settlement searchSettlementBySettlementID(long settlementId) {
    String sql = "select * from settlements where settlementID = ?";
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Settlement settlement = new Settlement();
    try {
      con = mainRepository.getConnection();
      ps = con.prepareStatement(sql);
      ps.setLong(1, settlementId);
      rs = ps.executeQuery();
      if (rs.next()) {
        settlement.setSettlementId(rs.getLong("settlementID"));
        settlement.setHostUsername(rs.getString("hostUsername"));
        settlement.setName(rs.getString("name"));
        settlement.setDescription(rs.getString("description"));
      }
      return settlement;
    } catch (SQLException e) {
      logForSQLException(e);
      return null;
    } finally {
      mainRepository.close(con, ps, rs);
    }
  }

  public boolean joinMember(long memberId, long settlementId) {
    String sql = "insert into settlementMembers (memberID, settlementID) values (?, ?)";
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Settlement settlement = new Settlement();
    log.info("member={}", memberId);
    log.info("settlement={}", settlementId);
    try {
      con = mainRepository.getConnection();
      ps = con.prepareStatement(sql);
      ps.setLong(1, memberId);
      ps.setLong(2, settlementId);
      ps.executeUpdate();
      return true;
    } catch (SQLException e) {
      logForSQLException(e);
      return false;
    } finally {
      mainRepository.close(con, ps, rs);
    }
  }

  public List<String> getJoinMembersBySettlementId(long settlementId) {
    String sql = "select m.username from members m join settlementMembers sm on m.memberID = sm.memberID where sm.settlementID = ?";
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<String> joinMembers = new ArrayList<>();
    try {
      con = mainRepository.getConnection();
      ps = con.prepareStatement(sql);
      ps.setLong(1, settlementId);
      rs = ps.executeQuery();
      while (rs.next()) {
        String member = rs.getString("username");
        joinMembers.add(member);
      }
      return joinMembers;
    } catch (SQLException e) {
      logForSQLException(e);
      return null;
    } finally {
      mainRepository.close(con, ps, rs);
    }

  }
}
