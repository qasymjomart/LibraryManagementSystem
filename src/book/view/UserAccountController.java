package book.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import book.UserStart;
import book.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class UserAccountController {
	
	 UserStart mainApp;
	 //private Stage thisStage;

	 @FXML
	 private Label username;
	
	 @FXML
	 private TableView<Book> bookBorrowedTable;
	 @FXML
	 private TableColumn<Book, String> titleColumn;
	 @FXML
	 private TableColumn<Book, String> authorColumn;
	 @FXML
	 private TableColumn<Book, String> llcColumn;
	 @FXML
	 private TableColumn<Book, String> isbnColumn;
	 @FXML
	 private TableColumn<Book, String> publisherColumn;
	 @FXML
	 private TableColumn<Book, String> publishingYearColumn;

	 
	 @FXML
	 private Button returnBook;
	 
	 private ObservableList<Book> borrowedBooksData;
	 private ArrayList<Book> booksInAccount;
	 
	 public void setMainApp(UserStart userStart) {
	        this.mainApp = userStart;
	 }
	 	 
	 public void setUsernameToDisplay(String usernameToDisplay) {
			
		 	username.setText(usernameToDisplay);
						
	}
	 
	 public void setStage(Stage userAccountStage) {
			//this.thisStage = userAccountStage;
	 }
	 
	public void initializeBooks(ArrayList<Book> books) {
		booksInAccount = books;
		
		borrowedBooksData = FXCollections.observableArrayList(booksInAccount);
		
		authorColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		publishingYearColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("publishingYear"));
		isbnColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("ISBN"));
		publisherColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
		llcColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("llc"));
		
		bookBorrowedTable.setItems(borrowedBooksData);
		
	}
	
	public void handleReturnTheBookButton() {
		String userName = username.getText();
		Book selectedBook = bookBorrowedTable.getSelectionModel().getSelectedItem();
		
		
		if(selectedBook == null) {
			AnchorPane pane = mainApp.getWindowInfo();
			showAlert(Alert.AlertType.CONFIRMATION, pane.getScene().getWindow(),"Books not chosen!", "Choose the book from the list!");
			
		} else {
		
		try {
            FileInputStream excelFile = new FileInputStream(new File("bookissue.xlsx"));
        	Workbook workbook = new XSSFWorkbook(excelFile);
            FileOutputStream outputStream = new FileOutputStream("bookissue.xlsx");

            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            
            while (iterator.hasNext()) {
                
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.cellIterator();
                currentRow.getRowNum();
                
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                	System.out.println(cell.getStringCellValue());

                    if(cell.getStringCellValue().equals(userName)) {
                    	cell = cellIterator.next();
                    	String borrowedBooksISBN = cell.getStringCellValue();
                    	//All borrowed books ISBN are separated to '//' in the excel cell
                    	String[] booksBorrowed = borrowedBooksISBN.split("//");
                    	borrowedBooksISBN = "Bookstaken";
                    	for(int i = 0; i < booksBorrowed.length; i++) {
                    		if(booksBorrowed[i].equals(selectedBook.getISBN())) {
                    			booksBorrowed[i] = "";
                    			for(int j = 0; j < booksBorrowed.length; j++) {
                    				if((!(booksBorrowed[j].equals(""))) || (!(booksBorrowed[j].equals("Bookstaken")))){
                    				borrowedBooksISBN = borrowedBooksISBN + "//" + booksBorrowed[j]; 
                    				}
                    			}
                    			
                            	cell.setCellValue(borrowedBooksISBN);
                    			selectedBook.stock++;
                    			AnchorPane pane = mainApp.getWindowInfo();
                    			showAlert(Alert.AlertType.CONFIRMATION, pane.getScene().getWindow(),"Book Returned!", "Book Returned!");
                    			break;
                    		}
                    		
                    	}
                    	
                 	
                    }
                    
            }
        }
  
            
         	workbook.write(outputStream);
            workbook.close();
            excelFile.close();
  
            
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
		}
		mainApp.actionsAfterBookReturnedAtUserAccount(userName, selectedBook);

		booksInAccount.remove(selectedBook);
		initializeBooks(booksInAccount);
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
