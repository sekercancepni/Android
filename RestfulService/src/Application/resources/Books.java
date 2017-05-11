package resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Database.BookSet;
import Database.PersonSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Path("books")
public class Books {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{bookId}")
	public Response getBook(@PathParam("bookId") Integer bookId) {
		Map<String, String> responseData = new HashMap<String, String>();
		Map book = BookSet.getById(bookId);

		if (book == null) {
			responseData.put("success", "0");
			responseData.put("message", "Not Found");
			return Response.status(404).entity(responseData).build();
		} else {
			return Response.status(200).entity(book).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public Response getBooks(@QueryParam("query") String query) {
		List books = BookSet.getAll(query);
		Map<String, List> data = new HashMap<String, List>();
		data.put("data", books);
		return Response.status(200).entity(data).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{bookId}/owners")
	public Response getOwners(@PathParam("bookId") Integer bookId) {
		List owners = BookSet.getOwners(bookId);
		Map<String, List> data = new HashMap<String, List>();
		data.put("data", owners);
		return Response.status(200).entity(data).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{bookId}/owner/{userId}")
	public Response owner(@PathParam("bookId") Integer bookId,
						  @PathParam("userId") Integer userId) {
		Map<String, String> data = new HashMap<String, String>();
		Boolean isAlreadyOwner = BookSet.isUserOwner(bookId, userId);
		if (isAlreadyOwner) {
			data.put("message", "already_exist");
		} else {
			BookSet.setOwner(bookId, userId);
			data.put("message", "success");
		}

		return Response.status(200).entity(data).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{bookId}/remove_owner/{userId}")
	public Response remove_owner(@PathParam("bookId") Integer bookId,
						  @PathParam("userId") Integer userId) {
		Map<String, String> data = new HashMap<String, String>();
		Boolean isAlreadyOwner = BookSet.isUserOwner(bookId, userId);
		if (isAlreadyOwner) {
			BookSet.removeOwner(bookId, userId);
			data.put("message", "success");
		} else {
			data.put("message", "not_exist");
		}

		return Response.status(200).entity(data).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/new_request")
	public Response newRequest(@QueryParam("name") String bookName,
							   @QueryParam("author") String author,
							   @QueryParam("user_id") String userId) {
		Map<String, String> data = new HashMap<String, String>();
		BookSet.addNewRequest(bookName, author, Integer.parseInt(userId));
		data.put("message", "Success");
		return Response.status(200).entity(data).build();
	}
}
