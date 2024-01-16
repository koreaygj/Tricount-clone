package groom.tricountClone.domain.expense;

import groom.tricountClone.domain.repository.MainRepository;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExpenseRepository {

  private final static MainRepository mainrepository = MainRepository.getMainRepository();

  @Getter
  private static final ExpenseRepository expenseRepository = new ExpenseRepository();


  public List<Expense> getAllExpensesBySettlementId(long settlementId) {
    String sql = "select * from expenses where settlementID = ?";
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<Expense> expenseList = new ArrayList<>();
    try {
      con = mainrepository.getConnection();
      ps = con.prepareStatement(sql);
      ps.setLong(1, settlementId);
      ps.executeQuery();
      if (rs == null) {
        return expenseList;
      }
      while (rs.next()) {
        Expense expense = new Expense();
        expense.setExpenseId(rs.getLong("expenseID"));
        expense.setMemberId(rs.getLong("memberID"));
        expense.setSettlementId(rs.getLong("settlementID"));
        expense.setName(rs.getString("name"));
        expense.setCost(rs.getInt("cost"));
        expense.setDate(rs.getDate("date").toString());
        expenseList.add(expense);
      }
      return expenseList;
    } catch (SQLException e) {
      logForSQLException(e);
      return null;
    } finally {
      mainrepository.close(con, ps, rs);
    }
  }

  public Expense saveExpense(Expense expense) {
    String sql = "insert into expenses (memberID, settlementID, name, cost, date) values (?, ?, ?, ?, ?)";
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = mainrepository.getConnection();
      ps = con.prepareStatement(sql);
      ps.setLong(1, expense.getMemberId());
      ps.setLong(2, expense.getSettlementId());
      ps.setString(3, expense.getName());
      ps.setInt(4, expense.getCost());
      ps.setDate(5, Date.valueOf(expense.getDate()));
      ps.executeUpdate();
      return expense;
    } catch (SQLException e) {
      logForSQLException(e);
      return null;
    } finally {
      mainrepository.close(con, ps, null);
    }
  }

  private static void logForSQLException(SQLException e) {
    log.error("DB error", e);
    log.error("SQLException Message: " + e.getMessage());
    log.error("SQLState: " + e.getSQLState());
    log.error("ErrorCode: " + e.getErrorCode());
  }
}
