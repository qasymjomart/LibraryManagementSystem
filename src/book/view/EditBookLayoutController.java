package book.view;

import book.UserStart;
import book.model.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class EditBookLayoutController {
	
    private UserStart mainApp;
	
	private Stage thisStage;
	private Book editingBook;
	boolean editSuccessful = false;
	
	public void setMainApp(UserStart userStart) {
        this.mainApp = userStart;
    }
	
	@FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField llcField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField publishingYearField;
    @FXML
    private TextField stockField;
    

	public void setCurrentStage(Stage editBookStage) {
		this.thisStage = editBookStage;
	}

	public void setEditingBook(Book selectedBook) {
		this.editingBook = selectedBook;

		if(editingBook != null) {
			
        titleField.setText(editingBook.getTitle());
        authorField.setText(editingBook.getAuthor());
        llcField.setText(editingBook.getLlc());
        isbnField.setText(editingBook.getISBN());
        publisherField.setText(editingBook.getPublisher());
        publishingYearField.setText(editingBook.getPublishingYear());
        stockField.setText(Integer.toString(editingBook.getStock()));

		} else {
			
			AnchorPane pane = mainApp.getEditBookWindowInfo();
        	showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), 
        			"Book not chosen", "Please choose the book you want to edit");
			
		}
	}
    
	@FXML
	private void handleSubmitButton() {
		
		String errorShowingMessage = checkIfInputValid();
		
		boolean validInput = false;
		
		if (errorShowingMessage.length() == 0) {
            validInput = true;
        } else {
            // Show the error message.
       
            validInput = false;
            
            AnchorPane pane = mainApp.getEditBookWindowInfo();
        	showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(), 
    	            "Error:", errorShowingMessage);
        }
		
        if (validInput) {
        	editingBook.setTitle(titleField.getText());
        	editingBook.setAuthor(authorField.getText());
        	editingBook.setLlc(llcField.getText());
        	editingBook.setISBN(isbnField.getText());
        	editingBook.setPublisher(publisherField.getText());
        	editingBook.setPublishingYear(publishingYearField.getText());
        	editingBook.setStock(Integer.parseInt(stockField.getText()));
        	
        	editSuccessful = true;
            thisStage.close();
        }
    }
	
	private void showAlert(AlertType error, Window window, String string, String errorShowingMessage) {
		 	Alert alert = new Alert(error);
		    alert.setTitle(string);
		    alert.setHeaderText(null);
		    alert.setContentText(errorShowingMessage);
		    alert.initOwner(window);
		    alert.show();
	}

	@FXML
    private void handleCancelButton() {
        thisStage.close();
    }
	
	 private String checkIfInputValid() {
	        String errorMessage = "";

	        if (titleField.getText() == null || titleField.getText().length() == 0) 
	            errorMessage += "No valid title!\n"; 
	        
	        if (authorField.getText() == null || authorField.getText().length() == 0) 
	            errorMessage += "No valid author!\n"; 
	        
	        if (llcField.getText() == null || llcField.getText().length() == 0) 
	            errorMessage += "No valid llc!\n"; 
	        

	        if (isbnField.getText() == null || isbnField.getText().length() == 0)
	            errorMessage += "No valid ISBN!\n"; 
	     
	        if (publisherField.getText() == null || publisherField.getText().length() == 0)
	            errorMessage += "No valid publisher!\n"; 
	       

	        if (publishingYearField.getText() == null || publishingYearField.getText().length() == 0)
	            errorMessage += "No valid publishing year!\n"; 
	        
	        if (stockField.getText() == null || stockField.getText().length() == 0) {
	            errorMessage += "No valid stock number\n";
	        }
	        else {
	        	try {
	                Integer.parseInt(stockField.getText());
	            } catch (NumberFormatException e) {
	                errorMessage += "No valid Stock number (must be an Integer)!\n"; 
	            }
	        }

	        return errorMessage;
	        
	 }

	public boolean editFinished() {
		
		if(editSuccessful == true)
			return true;
		
		return false;
	}
    
       
}
