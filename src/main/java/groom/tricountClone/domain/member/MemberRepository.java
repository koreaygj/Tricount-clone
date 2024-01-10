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
    String sql = "insert into members(userId, password, username) values( ?, ?, ?)";
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
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    List<Member> members = new ArrayList<>();
    try {
      con = mainRepository.getConnection();
      preparedStatement = con.prepareStatement(sql);
      rs = preparedStatement.executeQuery();
      while (rs.next()) {
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setUserId(rs.getString("userId"));
        member.setPassword(rs.getString("password"));
        member.setUsername(rs.getString("username"));
        members.add(member);
      }
      return members;
    } catch (SQLException e) {
      logForSQLException(e);
      return null;
    } finally {
      mainRepository.close(con, preparedStatement, rs);
    }
  }

  private static void logForSQLException(SQLException e) {
    log.error("DB error", e);
    log.error("SQLException Message: " + e.getMessage());
    log.error("SQLState: " + e.getSQLState());
    log.error("ErrorCode: " + e.getErrorCode());
  }
}
