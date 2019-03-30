package book.view;

import book.LibraryStart;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

public class RegistrationController {

	LibraryStart mainApp;
	
	@FXML
	private TextField newUserNameField;
	
	@FXML
	private PasswordField newPasswordField;
	
	@FXML
	private PasswordField newPasswordConfirmationField;
	
	@FXML
	private Label title;
	
	@FXML
	private Label newUserName;
	
	@FXML
	private Label userExistsLabel;
	
	@FXML
	private Label newPassword;
	
	@FXML
	private Label newPasswordConfirmation;
	
	@FXML
	private Button submitRegistration;
	
	@FXML
	private Label passwordsMatch;
	
	public void setMainApp(LibraryStart libraryStart) {
        this.mainApp = libraryStart;
    }
	
	boolean exists = false;
	
	@FXML
	private void handleSubmitRegistrationButton() {
		
		if(exists) {
			AnchorPane pane = mainApp.getWindowInfo();
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), 
            "Error:", "Such user exists! Create another username");
            
		} else if((newUserNameField.getText() == null)) {
			AnchorPane pane = mainApp.getWindowInfo();
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), 
            "Error: Username is not entered", "Enter username! Create another username");
		} else if(!(newPasswordField.getText().equals(newPasswordConfirmationField.getText()))) {
			AnchorPane pane = mainApp.getWindowInfo();
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), 
            "Error: Passwords do not match", "Passwords do not match");
		} else {
			String newUsername = newUserNameField.getText().trim();
	    	String newPassword = newPasswordField.getText().trim();
			
	    	boolean successfulSignUp = mainApp.signUpNewUser(newUsername, newPassword);
	    	if(successfulSignUp) {
	    		
	            
	            mainApp.getSignUpStageBack().close();
	    		mainApp.showLoginForm();
	    		
	    		AnchorPane pane = mainApp.getWindowInfo();
	            showAlert(Alert.AlertType.CONFIRMATION, pane.getScene().getWindow(), 
	            "A new user created", "Congratulations: You are Signed Up");
	    		
	    	} 
	    	
		}
		
	}
	
	
	
	@FXML
	private void handleKeyOnReleasedAtUsernameField() {
		
		String newUsername = newUserNameField.getText().trim();
		exists = mainApp.checkUsernameExists(newUsername);
		
		if(exists) {
        	userExistsLabel.setText("Such user already exists");
		} else {
        	userExistsLabel.setText("Username is available");
		}
		
	}
	
	@FXML
	private void handleKeyOnReleasedAtConfirmPasswordField() {
		
		if(newPasswordField.getText().equals(newPasswordConfirmationField.getText())) {
			passwordsMatch.setText("Passwords match!");
		} else {
			passwordsMatch.setText("Passwords do not match!");
		}
		
	}
	
	void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
	    Alert alert = new Alert(alertType);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.initOwner(owner);
	    alert.show();
	}
	
	
}
