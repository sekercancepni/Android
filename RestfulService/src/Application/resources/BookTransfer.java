package resources;


import Database.BookSet;
import Database.PersonSet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("book_transfer")
public class BookTransfer {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/in/{userId}")
	public Response bookTransfersIn(@PathParam("userId") Integer userId) {
		Map<String, List> data = new HashMap<String, List>();
		List bookTransfers = BookSet.getBookTransfersIn(userId);
		data.put("data", bookTransfers);
		return Response.status(200).entity(data).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/out/{userId}")
	public Response bookTransfersOut(@PathParam("userId") Integer userId) {
		Map<String, List> data = new HashMap<String, List>();
		List bookTransfers = BookSet.getBookTransfersOut(userId);
		data.put("data", bookTransfers);
		return Response.status(200).entity(data).build();
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/request/{bookOwnerId}/{userId}")
	public Response bookTransferRequest(@PathParam("bookOwnerId") Integer bookOwnerId,
										@PathParam("userId") Integer userId) {
		Map<String, String> data = new HashMap<String, String>();
		Map user = PersonSet.getById(userId);
		Map book = BookSet.getBookByOwnerId(bookOwnerId);
		Integer userPoint;
		Integer bookPoint;

		if (BookSet.isbookAlreadyRequested(bookOwnerId, userId)) {
			data.put("message", "The book has been already requested.");
			return Response.status(200).entity(data).build();
		}

		if (book == null) {
			data.put("message", "The book could not be found");
			return Response.status(200).entity(data).build();
		}

		if (user == null) {
			data.put("message", "The user could not be found");
			return Response.status(200).entity(data).build();
		}

		userPoint = Integer.parseInt(user.get("point").toString());
		bookPoint = Integer.parseInt(book.get("point").toString());
		userPoint -= PersonSet.getUserRequestPoint(userId);

		if (bookPoint > userPoint) {
			data.put("message", "You don't have enough credit to take this book.");
		} else {
			Boolean requested = BookSet.requestBook(bookOwnerId, userId);

			if (requested) {
				data.put("message", "Your request have been sent.");
			} else {
				data.put("message", "Unknown error");
			}
		}
		return Response.status(200).entity(data).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/sending/{bookTransferId}")
	public Response bookTransferSending(@PathParam("bookTransferId") Integer bookTransferId) {
		Map<String, String> data = new HashMap<String, String>();
		BookSet.changeBookTransferStatus(bookTransferId, "Sending");
		data.put("message", "Your book has been sending.");
		return Response.status(200).entity(data).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/sent/{bookTransferId}")
	public Response bookTransferSent(@PathParam("bookTransferId") Integer bookTransferId) {
		Map<String, String> data = new HashMap<String, String>();
		BookSet.changeBookTransferStatus(bookTransferId, "Sent");
		Map book = BookSet.getBookByTransferId(bookTransferId);
		Integer bookPoint;
		if (book == null) {
			data.put("message", "Book could not be found.");
		} else {
			bookPoint = Integer.parseInt(book.get("book_point").toString());
			PersonSet.changePointBy(Integer.parseInt(book.get("sender_id").toString()), bookPoint);
			PersonSet.changePointBy(Integer.parseInt(book.get("receiver_id").toString()), bookPoint * -1);
			BookSet.changeOwnerStatusAsSent(Integer.parseInt(book.get("owner_id").toString()));
			data.put("message", "Good reads.");
		}
		return Response.status(200).entity(data).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cancelled/{bookTransferId}")
	public Response bookTransferCancel(@PathParam("bookTransferId") Integer bookTransferId) {
		Map<String, String> data = new HashMap<String, String>();
		BookSet.changeBookTransferStatus(bookTransferId, "Cancelled");
		data.put("message", "Your book has been cancelled.");
		return Response.status(200).entity(data).build();
	}

}
