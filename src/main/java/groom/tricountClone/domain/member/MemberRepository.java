package groom.tricountClone.domain.member;

import groom.tricountClone.domain.repository.MainRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberRepository {

  private final MainRepository mainRepository = MainRepository.getMainRepository();

  @Getter
  private static final MemberRepository memberRepository = new MemberRepository();

  // 사용자 추가
  public Member saveMember(Member member) {
    String sql = "insert into members(userID, password, username) values( ?, ?, ?)";
    Connection con = null;
    PreparedStatement preparedStatement = null;
    try {
      con = mainRepository.getConnection();
      preparedStatement = con.prepareStatement(sql);
      preparedStatement.setString(1, member.getUserId());
      preparedStatement.setString(2, member.getPassword());
      preparedStatement.setString(3, member.getUsername());
      preparedStatement.executeUpdate();
      return member;
    } catch (SQLException e) {
      logForSQLException(e);
      return null;
    } finally {
      mainRepository.close(con, preparedStatement, null);
    }
  }

  // member userId로 찾기
  public Optional<Member> findByUserId(String userId) {
    return findAll().stream().filter(member -> member.getUserId().equals(userId)).findFirst();
  }

  // member userName으로 찾기
  public List<Member> findByUserName(String username) throws SQLException {
    return findAll().stream().filter(member -> member.getUsername().equals(username)).collect(
        Collectors.toList());
  }

  // member 모두 불러오기
  public List<Member> findAll() {
    String sql = "select * from members";
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<Member> members = new ArrayList<>();
    try {
      con = mainRepository.getConnection();
      ps = con.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        Member member = new Member();
        member.setMemberId(rs.getLong("memberID"));
        member.setUserId(rs.getString("userID"));
        member.setPassword(rs.getString("password"));
        member.setUsername(rs.getString("username"));
        members.add(member);
      }
      return members;
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

  public Member searchByUsername(String username) {
    String sql = "select * from members where username = ?";
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Member member = new Member();
    try {
      con = mainRepository.getConnection();
      ps = con.prepareStatement(sql);
      ps.setString(1, username);
      rs = ps.executeQuery();
      if (rs.next()) {
        member.setMemberId(rs.getLong("memberID"));
        member.setUserId(rs.getString("userID"));
        member.setPassword(rs.getString("password"));
        member.setUsername(rs.getString("username"));
      }
      return member;
    } catch (SQLException e) {
      logForSQLException(e);
      return null;
    } finally {
      mainRepository.close(con, ps, rs);
    }
  }
}
