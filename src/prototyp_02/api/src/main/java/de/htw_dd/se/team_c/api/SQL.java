package de.htw_dd.se.team_c.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class SQL {
  SQL() throws SQLException {
    this("database","postgres");
  }
  String url;
  Connection conn;
  SQL(String host,String database) throws SQLException {
    url = "jdbc:postgresql://%s/%s?user=%s".formatted(host,database,"postgres");
    conn = DriverManager.getConnection(url);
  }
  public Connection getConnection(){
    return conn;
  }
}
