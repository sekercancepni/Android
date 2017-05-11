package resources;

import Database.Mailer;
import Database.PersonSet;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Path("/users")
public class Users {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{userId}")
	public Response getPerson(@PathParam("userId") Integer userId) {
		Map<String, String> responseData = new HashMap<String, String>();
		Map user = PersonSet.getById(userId);
		if (user == null) {
			responseData.put("success", "0");
			responseData.put("message", "Not Found");
			return Response.status(404).entity(responseData).build();
		} else {
			return Response.status(200).entity(user).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{userId}/update")
	public Response updatePerson(@PathParam("userId") Integer userId,
								 @QueryParam("first_name") String first_name,
								 @QueryParam("last_name") String last_name,
								 @QueryParam("email") String email,
								 @QueryParam("phone") String phone,
								 @QueryParam("password") String password,
								 @QueryParam("new_password") String newPassword) {
		Map<String, String> responseData = new HashMap<String, String>();
		Map user = PersonSet.getById(userId);
		if (password == null) {
			responseData.put("success", "0");
			responseData.put("message", "Password is required");
		} else if (user == null) {
			responseData.put("success", "0");
			responseData.put("message", "User could not be found.");
		} else if (!password.equals(user.get("password").toString())){
			responseData.put("success", "0");
			responseData.put("message", "Password does not match.");
		} else {
			if (first_name == null || first_name.equals("")) {
				first_name = user.get("first_name").toString();
			}
			if (last_name == null || last_name.equals("")) {
				last_name = user.get("last_name").toString();
			}
			if (email == null || email.equals("")) {
				email = user.get("email").toString();
			}
			if (phone == null || phone.equals("")) {
				phone = user.get("phone").toString();
			}
			if (newPassword == null || newPassword.equals("")) {
				newPassword = password;
			}
			PersonSet.updateUserById(userId, first_name, last_name, phone, email, newPassword);
			responseData.put("success", "1");
		}

		return Response.status(200).entity(responseData).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{userId}/books")
	public Response myBooks(@PathParam("userId") Integer userId) {
		List books = PersonSet.myBooks(userId);
		Map<String, List> data = new HashMap<String, List>();
		data.put("data", books);
		return Response.status(200).entity(data).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/signup")
	public Response signup(@Context UriInfo ui) {
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		Map<String, String> responseData = new HashMap<String, String>();
		List<String> errorMessages = new ArrayList<String>();
		String[] requiredFields = new String[] {"first_name", "last_name", "email", "password"};
		for (String requiredField : requiredFields) {
			if (queryParams.getFirst(requiredField) == null || queryParams.getFirst(requiredField).equals("")) {
				errorMessages.add("This " + requiredField + " field is required");
			}
		}

		boolean isEmailExist = PersonSet.isEmailExist(queryParams.getFirst("email"));
		if (isEmailExist) {
			errorMessages.add("This email already exists.");
		}

		boolean isFormValid = errorMessages.size() == 0;
		if (isFormValid) {
			PersonSet.signup(
					queryParams.getFirst("first_name"),
					queryParams.getFirst("last_name"),
					queryParams.getFirst("email"),
					queryParams.getFirst("password"),
					queryParams.getFirst("phone")
			);

			responseData.put("success", "1");
			responseData.put("message", "Succesfully signed up.");
		} else {
			responseData.put("success", "0");
			responseData.put("message", errorMessages.get(0));
		}

		return Response.ok().entity(responseData).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/login")
	public Response login(@QueryParam("email") String email,
						  @QueryParam("password") String password) {
		Map<String, String> responseData = new HashMap<String, String>();
		// check from db with username and password.
		Map user = PersonSet.checkLogin(email, password);
		if (user == null) {
			responseData.put("success", "0");
		} else {
			responseData = user;
			responseData.put("success", "1");
		}

		return Response.status(200).entity(responseData).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/remember_password")
	public Response rememberPassword(@QueryParam("email") String email) {
		Map<String, String> responseData = new HashMap<String, String>();
		if (email == null || email.equals("")) {
			responseData.put("message", "Email is required.");
			return Response.status(200).entity(responseData).build();
		}

		Map user = PersonSet.getByEmail(email);
		if (user == null) {
			responseData.put("message", "Email could not be found.");
		} else {
			String password = user.get("password").toString();
			Mailer.sendPasswordRememberMail(email, password);
			responseData.put("message", "Password has been sent to email address.");
		}

		return Response.status(200).entity(responseData).build();
	}


}
