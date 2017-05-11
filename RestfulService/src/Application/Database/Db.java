package Database;

import javax.xml.transform.Result;
import java.sql.*;
import java.sql.SQLException;
import java.util.HashMap;

public class Db {
	public Connection conn = null;

	public Connection getConnection() throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		if (this.conn == null) {
			this.conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/booksharing", "root", "root");
		}
		return this.conn;
	}

	//sql statement
	public void runAffectQuery(String sql) {
		System.out.println(sql);
		try {
			Statement statement = this.getConnection().createStatement();
			statement.executeUpdate(sql);
		} catch(SQLException e) {
			e.printStackTrace();
			// Util.alertError("Database Error", "Occured unknonwn error. please try again");
		}
	}

	public ResultSet executeQuery(String sql) {
		System.out.println(sql);
		try {
			Statement statement = this.getConnection().createStatement();
			return statement.executeQuery(sql);
		} catch(SQLException e) {
			e.printStackTrace();
			//Util.alertError("Database Error", "Occured unknown error.");
		}
		return null;
	}

}
