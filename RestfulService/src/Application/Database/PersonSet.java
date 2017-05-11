package Database;

import javafx.scene.control.Alert;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PersonSet {
	public static Map getById(Integer id) {
		String sql = "SELECT * FROM public.user WHERE id='" + id + "';";
		Map<String, String> data = new HashMap<String, String>();
		try {
			Db db = new Db();
			ResultSet resultset = db.executeQuery(sql);
			resultset.next();
			data.put("id", resultset.getString("id"));
			data.put("first_name", resultset.getString("first_name"));
			data.put("last_name", resultset.getString("last_name"));
			data.put("email", resultset.getString("email"));
			data.put("phone", resultset.getString("phone"));
			data.put("password", resultset.getString("password"));
			data.put("point", resultset.getString("point"));
			return data;
		} catch (SQLException e) {
			return null;
		}
	}

	public static Map getByEmail(String email) {
		String sql = "SELECT id FROM public.user WHERE email='" + email + "'";
		try {
			Db db = new Db();
			ResultSet resultset = db.executeQuery(sql);
			resultset.next();
			return PersonSet.getById(resultset.getInt("id"));
		} catch (SQLException e) {
			return null;
		}
	}

	public static Integer getUserRequestPoint(Integer userId) {
		String sql = "SELECT SUM(B.point) as total_point FROM \"bookTransfer\" T\n" +
				"LEFT JOIN bookowner O ON O.id=T.book_owner_id\n" +
				"LEFT JOIN book B ON B.id=O.book_id\n" +
				"WHERE t.to_user_id=" + userId + " AND T.status IN ('Requested', 'Sending');";
		try {
			Db db = new Db();
			ResultSet resultset = db.executeQuery(sql);
			resultset.next();
			return resultset.getInt("total_point");
		} catch (SQLException e) {
			return 0;
		}
	}

	public static Boolean updateUserById(Integer id, String first_name, String last_name,
										 String phone, String email, String password) {
		String sql = "UPDATE public.user SET email='" + email +"', first_name='" + first_name +
				"', last_name='" + last_name + "', phone='" + phone +
				"', password='" + password + "' WHERE id='" + id + "';";
		Db db = new Db();
		db.runAffectQuery(sql);
		return true;
	}

	public static void signup(String firstName, String lastName, String email, String password, String phone) {
		String sql = "INSERT INTO public.user(first_name, last_name, email, password, phone, point) VALUES('" + firstName + "', '" + lastName + "','" + email + "','" + password + "','" + phone + "'," + 50 + " );";
		Db db = new Db();
		db.runAffectQuery(sql);
	}

	public static void person_login(String email, String password) {
		String sql = "SELECT email,password FROM user";
		Db db = new Db();
		db.runAffectQuery(sql);
	}

	public static boolean isEmailExist(String email) {
		String sql = "SELECT id FROM public.user WHERE email='" + email + "'";
		try {
			Db db = new Db();
			ResultSet result = db.executeQuery(sql);
			return result.next();
		} catch (SQLException e) {
			return true;
		}
	}

	public static List myBooks(Integer userId) {
		String sql = "SELECT * FROM public.bookowner WHERE user_id='" + userId + "';";
		Map<String, String> data = new HashMap<String, String>();
		List<String> bookIds = new ArrayList<String>();
		try {
			Db db = new Db();
			ResultSet resultset = db.executeQuery(sql);
			while(resultset.next()) {
				bookIds.add(resultset.getString("book_id"));
			}

			return BookSet.getByIds(bookIds, null);
		} catch (SQLException e) {
			return null;
		}
	}

	public static Map checkLogin(String email, String password) {
		Map<String, String> data = new HashMap<String, String>();

		try {
			String sql = "SELECT * FROM public.user WHERE email='" + email + "' AND password='" + password + "';";
			Db db = new Db();
			ResultSet resultset = db.executeQuery(sql);
			resultset.next();
			data.put("id", resultset.getString("id"));
			data.put("first_name", resultset.getString("first_name"));
			data.put("last_name", resultset.getString("last_name"));
			data.put("email", resultset.getString("email"));
			return data;
		} catch (SQLException e) {
			return null;
		}
	}

	public static Boolean changePointBy(Integer userId, Integer point) {
		String sql = "UPDATE public.user SET point=point+(" + point + ") where id=" + userId + ";";
		Db db = new Db();
		db.runAffectQuery(sql);
		return true;
	}
}
