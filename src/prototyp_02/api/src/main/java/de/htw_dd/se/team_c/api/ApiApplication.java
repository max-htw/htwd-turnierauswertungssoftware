package de.htw_dd.se.team_c.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

@SpringBootApplication
@RestController
public class ApiApplication {
    static SQL sql;
    public static void main(String[] args) {
      try {
        sql = new SQL();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      SpringApplication.run(ApiApplication.class, args);
    }

    @CrossOrigin()
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        System.out.println("/hello get");
      try(PreparedStatement pstmt = sql.getConnection().prepareStatement("SELECT 'Hello from Database';")) {
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getString(1);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
}
