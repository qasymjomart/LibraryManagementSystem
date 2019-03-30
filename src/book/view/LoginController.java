package book.view;

import book.LibraryStart;
import book.UserStart;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

public class LoginController {
	
    private LibraryStart mainApp;
	
	@FXML
	private Label headerLabel;
	
	@FXML
	private Label nameLabel;
	
	@FXML
	private Label passwordLabel;
	
	@FXML
	private TextField nameField;
	
	@FXML
	private PasswordField passwordField;
	
	@FXML
	private Button submitLoginButton;
	
	@FXML
	private Hyperlink link;
	
	public void setMainApp(LibraryStart libraryStart) {
        this.mainApp = libraryStart;
    }
	
	void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
	    Alert alert = new Alert(alertType);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.initOwner(owner);
	    alert.show();
	}
	
	@FXML
	 private void handleSubmitLoginButton() {
		
		if(nameField.getText().isEmpty()) {
			
			AnchorPane pane = mainApp.getWindowInfo();
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), 
            "Error:", "Please enter your name");
            return;
        }
        if(passwordField.getText().isEmpty()) {
        	
			AnchorPane pane = mainApp.getWindowInfo();
            showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), 
            "Error:", "Please enter a password");
            return;
        }
        
        if(nameField.getText().equals("admin") && passwordField.getText().equals("admin"))  {
        	
        	UserStart userInterface = new UserStart();
        	String userRole = "Admin";
        	
        	userInterface.userPane(mainApp.getStageBack(), nameField.getText(), userRole);
        } else if(mainApp.checkUser(nameField.getText(), passwordField.getText()) == true){
            
        	UserStart userInterface = new UserStart();
        	String userRole = "User";
        	userInterface.userPane(mainApp.getStageBack(), nameField.getText(), userRole);
        	
        } else if(nameField.getText().equals("admin") && passwordField.getText().equals("admin"))  {
        	
        	UserStart userInterface = new UserStart();
        	String userRole = "Admin";
        	
        	userInterface.userPane(mainApp.getStageBack(), nameField.getText(), userRole);
        	
        } else {
        	Label label = new Label();
        	label.setText("Incorrect login or password!");
        	
			AnchorPane pane = mainApp.getWindowInfo();
        	showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), 
    	            "Error:", "Incorrect login or password!");
    	            return;
        }
        
       
    }
	
	@FXML
	private void handleHyperlinkPressed() {
		mainApp.getStageBack().close();
		mainApp.showSignUpForm();
	}
		
	
	
	
}
