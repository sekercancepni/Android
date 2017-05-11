package Database;

import javafx.scene.control.Alert;

public class Util {
	public static void showAlert(String header, String message, Alert.AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle("Book Sharing");
		alert.setHeaderText(header);
		alert.setContentText(message);

		alert.show();
	}

	public static void alertInfo(String header, String message) {
		Util.showAlert(header, message, Alert.AlertType.INFORMATION);
	}

	public static void alertError(String header, String message) {
		Util.showAlert(header, message, Alert.AlertType.ERROR);
	}
}
