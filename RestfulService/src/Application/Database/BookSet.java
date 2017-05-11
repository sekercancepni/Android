package Database;

import com.sun.deploy.util.StringUtils;
import jdk.nashorn.internal.ir.Block;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class BookSet {
	public static Map getById(Integer id) {
		String sql = "SELECT * FROM public.book WHERE id='" + id + "';";
		Map<String, String> data = new HashMap<String, String>();
		try {
			Db db = new Db();
			ResultSet resultset = db.executeQuery(sql);
			resultset.next();
			data.put("id", resultset.getString("id"));
			data.put("name", resultset.getString("name"));
			data.put("author", resultset.getString("author"));
			data.put("summary", resultset.getString("summary"));
			data.put("point", resultset.getString("point"));
			data.put("image", resultset.getString("image"));
			return data;
		} catch (SQLException e) {
			return null;
		}
	}

	public static Map getBookByOwnerId(Integer id) {
		String sql = "SELECT * FROM public.bookowner WHERE id='" + id + "'";
		try {
			Db db = new Db();
			ResultSet resultset = db.executeQuery(sql);
			resultset.next();
			int bookId = resultset.getInt("book_id");
			return BookSet.getById(bookId);
		} catch (SQLException e) {
			return null;
		}
	}

	public static Boolean isbookAlreadyRequested(Integer bookOwnerId, Integer userId) {
		String sql = "SELECT * FROM \"bookTransfer\" WHERE book_owner_id=" + bookOwnerId + " AND to_user_id=" + userId + ";";
		Db db = new Db();
		ResultSet resultset = db.executeQuery(sql);
		try {
			return resultset.next();
		} catch(SQLException e) {
			return false;
		}
	}

	public static Boolean requestBook(Integer bookOwnerId, Integer userId) {
		String sql = "INSERT INTO \"bookTransfer\"(book_owner_id, to_user_id, status) VALUES (" + bookOwnerId + ", " + userId + ", 'Requested');";
		Db db = new Db();
		db.runAffectQuery(sql);
		return true;
	}

	public static Boolean changeBookTransferStatus(Integer bookTransferId, String status) {
		String sql = "UPDATE \"bookTransfer\" SET status='" + status + "' WHERE id="+ bookTransferId +";";
		Db db = new Db();
		db.runAffectQuery(sql);
		return true;
	}

	public static List getByIds(List ids, String query) {
		List<Map> data = new ArrayList<Map>();

		try {
			Db db = new Db();
			String sql = "SELECT * FROM public.book";
			if (ids != null) {
				String commaSepIds = "";
				for (int i=0; i < ids.size(); i++) {
					commaSepIds += ids.get(i) + ",";
				}
				commaSepIds = commaSepIds.substring(0, commaSepIds.length() - 1);

				sql += " WHERE id IN (" + commaSepIds + ")";
			}
			if (query != null) {
				sql += " WHERE LOWER(name) LIKE '%" + query.toLowerCase() +
						"%' OR LOWER(author) LIKE '%" + query.toLowerCase() + "%'";
			}

			sql += " ORDER BY name;";

			ResultSet resultset = db.executeQuery(sql);

			while(resultset.next()) {
				Map<String, String> row = new HashMap<String, String>();
				row.put("id", resultset.getString("id"));
				row.put("name", resultset.getString("name"));
				row.put("author", resultset.getString("author"));
				row.put("summary", resultset.getString("summary"));
				row.put("point", resultset.getString("point"));
				row.put("image", resultset.getString("image"));
				data.add(row);
			}
		} catch(SQLException e) {
			return data;
		}

		return data;
	}

	public static List getAll() {
		return BookSet.getByIds(null, null);
	}

	public static List getAll(String query) {
		if (query != null) {
			return BookSet.getByIds(null, query);
		}
		return BookSet.getByIds(null, null);
	}

	public static Boolean isUserOwner(Integer bookId, Integer userId) {
		String sql = "SELECT COUNT(*) as count FROM public.bookowner WHERE book_id=" + bookId + " AND user_id=" + userId + " AND status!='Sent';";
		Db db = new Db();
		ResultSet resultset = db.executeQuery(sql);
		try {
			resultset.next();
			int count = resultset.getInt("count");
			return count > 0;
		} catch (SQLException e) {
			return true;
		}
	}

	public static Boolean setOwner(Integer bookId, Integer userId) {
		String sql = "INSERT INTO public.bookowner(book_id, user_id, status) VALUES(" + bookId + ", " + userId + ", 'Added')";
		Db db = new Db();
		db.runAffectQuery(sql);
		return true;
	}

	public static Boolean removeOwner(Integer bookId, Integer userId) {
		String sql = "DELETE FROM public.bookowner WHERE book_id=" + bookId + " AND user_id=" + userId + ";";
		Db db = new Db();
		db.runAffectQuery(sql);
		return true;
	}
	public static List getOwners(Integer bookId) {
		String sql = "select B.id, U.id user_id, U.first_name, U.last_name, U.email, U.phone from bookowner B " +
				"LEFT JOIN public.user U ON U.id = B.user_id\n" +
				"WHERE B.book_id=" + bookId + " AND B.status != 'Sent';";
		List<Map> data = new ArrayList<Map>();
		try {
			Db db = new Db();
			ResultSet resultset = db.executeQuery(sql);

			while(resultset.next()) {
				Map<String, String> row = new HashMap<String, String>();
				row.put("id", resultset.getString("id"));
				row.put("user_id", resultset.getString("user_id"));
				row.put("first_name", resultset.getString("first_name"));
				row.put("last_name", resultset.getString("last_name"));
				row.put("email", resultset.getString("email"));
				row.put("phone", resultset.getString("phone"));
				data.add(row);
			}

			return data;
		} catch (SQLException e) {
			return null;
		}
	}

	private static List getBookInfoByTransferIds(List<Integer> transferIds) {
		String sql = "SELECT " +
				"  T.id AS transfer_id, " +
				"  T.status AS status, " +
				"  B.name, B.author, B.image, B.id AS book_id, B.point AS book_point, " +
				"  CONCAT(Sender.first_name, ' ', Sender.last_name) AS sender_name, " +
				"  O.id AS owner_id, " +
				"  Sender.id AS sender_id, " +
				"  CONCAT(Receiver.first_name, ' ', Receiver.last_name) AS receiver_name, " +
				"  Receiver.id AS receiver_id " +
				"FROM \"bookTransfer\" T " +
				"LEFT JOIN bookowner O ON O.id=T.book_owner_id " +
				"LEFT JOIN book B ON B.id=O.book_id " +
				"LEFT JOIN public.user Sender ON Sender.id=O.user_id " +
				"LEFT JOIN public.user Receiver ON Receiver.id=T.to_user_id " +
				"WHERE T.id=0";
		for (int transferId: transferIds) {
			sql += " OR T.id=" + transferId;
		}

		List<Map> data = new ArrayList<Map>();

		try {
			Db db = new Db();
			ResultSet resultset = db.executeQuery(sql);

			while(resultset.next()) {
				Map<String, String> row = new HashMap<String, String>();
				row.put("transfer_id", resultset.getString("transfer_id"));
				row.put("status", resultset.getString("status"));
				row.put("name", resultset.getString("name"));
				row.put("author", resultset.getString("author"));
				row.put("image", resultset.getString("image"));
				row.put("book_id", resultset.getString("book_id"));
				row.put("book_point", resultset.getString("book_point"));
				row.put("owner_id", resultset.getString("owner_id"));
				row.put("sender_name", resultset.getString("sender_name"));
				row.put("sender_id", resultset.getString("sender_id"));
				row.put("receiver_name", resultset.getString("receiver_name"));
				row.put("receiver_id", resultset.getString("receiver_id"));
				data.add(row);
			}

			return data;
		} catch (SQLException e) {
			return null;
		}
	}

	public static Map getBookByTransferId(Integer transferId) {
		List<Integer> transferIds = new ArrayList<Integer>();
		transferIds.add(transferId);
		List books = BookSet.getBookInfoByTransferIds(transferIds);
		if (books == null) {
			return null;
		}
		return (Map) books.get(0);
	}

	private static List getBookInfoBySql(String sql) {
		List<Integer> transferIds = new ArrayList<Integer>();
		try {
			Db db = new Db();
			ResultSet resultset = db.executeQuery(sql);
			while(resultset.next()) {
				transferIds.add(resultset.getInt("id"));
			}
			return BookSet.getBookInfoByTransferIds(transferIds);
		} catch(SQLException e) {
			return null;
		}
	}

	public static List getBookTransfersIn(Integer userId) {
		String sql = "SELECT id FROM \"bookTransfer\" WHERE to_user_id=" + userId + ";";
		return BookSet.getBookInfoBySql(sql);
	}

	public static List getBookTransfersOut(Integer userId) {
		String sql = "SELECT T.id FROM bookowner O " +
				"RIGHT JOIN \"bookTransfer\" T ON T.book_owner_id=O.id " +
				"WHERE O.user_id=" + userId;
		return BookSet.getBookInfoBySql(sql);
	}

	public static Boolean addNewRequest(String bookName, String author, Integer userId) {
		String sql = "INSERT INTO bookrequest(book_name, author, user_id) VALUES('" +
				bookName + "', '" + author+ "'," + userId + ")";
		Db db = new Db();
		db.runAffectQuery(sql);
		return true;
	}

	public static Boolean changeOwnerStatusAsSent(Integer ownerId) {
		String sql = "UPDATE public.bookowner SET status='Sent' where id=" + ownerId;
		Db db = new Db();
		db.runAffectQuery(sql);
		return true;
	}
}
