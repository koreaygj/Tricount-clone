package groom.tricountClone.domain.repository;

import static groom.tricountClone.connection.ConnectionConst.PASSWORD;
import static groom.tricountClone.connection.ConnectionConst.URL;
import static groom.tricountClone.connection.ConnectionConst.USERNAME;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

@Slf4j
public class MainRepository {

  @Getter
  private final static MainRepository mainRepository = dataSourceConnectionPool();
  private final DataSource dataSource;

  public MainRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  static public MainRepository dataSourceConnectionPool() {
    HikariDataSource hikariDataSource = new HikariDataSource();
    hikariDataSource.setJdbcUrl(URL);
    hikariDataSource.setUsername(USERNAME);
    hikariDataSource.setPassword(PASSWORD);
    hikariDataSource.setMaximumPoolSize(10);
    hikariDataSource.setPoolName("connection-pool");
    return new MainRepository(hikariDataSource);
  }

  public Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  public void close(Connection conn, PreparedStatement preparedStatement, ResultSet resultSet) {
    JdbcUtils.closeResultSet(resultSet);
    JdbcUtils.closeStatement(preparedStatement);
    JdbcUtils.closeConnection(conn);
  }
}
