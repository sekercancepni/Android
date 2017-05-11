package Database;

import Database.Db;

public class Book {
	public Integer id;
	public String name;
	public String author;
	public String summary;
	public Integer point;

	public Book(Integer id, String name, String author, String summary, Integer point) {
		this.id = id;
		this.name = name;
		this.author = author;
		this.summary = summary;
		this.point = point;
	}
}
