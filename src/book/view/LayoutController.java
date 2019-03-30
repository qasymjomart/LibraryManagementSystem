package book.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

public class LayoutController {
	int SortNum=0;
    private UserStart mainApp;
	@FXML
    public Button editButton;
	
	private ObservableList<Book> masterData;
	public ArrayList<Book> booksInController;
	public ArrayList<Book> booksSavedBeforeSearching;
	public ArrayList<Book> booksFound;

	@FXML
	private ChoiceBox<String> choiceBox;
	
	@FXML
	private ChoiceBox<String> choiceSortTypeBox;
	
	@FXML
	private Button addButton;
	@FXML
	private Button deleteButton;
	
	@FXML
	private Label usernameDisplay;

	@FXML
	private CheckBox globalCheck;
	
	@FXML
	private CheckBox authorCheck;
	
	@FXML
	private CheckBox titleCheck;
	
	@FXML
	private CheckBox yearCheck;
	
	@FXML
	private CheckBox ISBNCheck;
	
	@FXML
	private CheckBox publisherCheck;
	
	@FXML
	private CheckBox llcCheck;
	
	@FXML
	private TextField searchText;
	
		
    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, String> numberingColumn;
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
    private TableColumn<Book, Number> stockColumn;
	
	public void setMainApp(UserStart userStart) {
        this.mainApp = userStart;
    }
	
	String userName;
	
	public void setUsernameDisplay(String usernameToDisplay) {
		
		userName = usernameToDisplay;
		usernameDisplay.setText(usernameToDisplay);
		choiceBox.getItems().addAll("Author", "Title", "Year", "ISBN", "Publisher", "LLC");
		choiceBox.setValue("Author");
		
		choiceSortTypeBox.getItems().addAll("Ascending", "Descending");
		choiceSortTypeBox.setValue("Ascending");
		//optionsOfChoiceBox = choiceBox.getItems();
		
	}
	
	 @FXML
	 private void handleButtonClick() {
		 
		 System.out.println("Everything");
	 }
	 
	 @FXML
	 private void handleEditBookButton() {
		Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
			if(selectedBook != null) {
				boolean editFinished = mainApp.showEditWindow(selectedBook);
				if(editFinished == true) {
					updateBookDetails();
				}
			} else {
				
				AnchorPane pane = mainApp.getWindowInfo();
				showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(),"Error:", "Book not chosen!");
			}
	 }
	 
	 @FXML
	 private void handleDeleteBookButton() {
			int selectedBookIndex = bookTable.getSelectionModel().getSelectedIndex();
			if(selectedBookIndex >= 0) {
				booksInController.remove(selectedBookIndex);
				updateBookDetails();

			} else {
				AnchorPane pane = mainApp.getWindowInfo();
				showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(),"Error: Book ot chosen", "Choose a book to delete!");
			}
	 }
	 
	 @FXML
	 private void handleAddBookButton() {
		 Book newBook = new Book();
		 boolean editFinished = mainApp.showEditWindow(newBook);
			if(editFinished == true) {
				booksInController.add(newBook);
				newBook.setNum(Integer.toString(booksInController.indexOf(newBook) + 1));
				updateBookDetails();
			}
			
	 }
	 
	 @FXML
	 private void handleIssueBookButton() {
		String username = mainApp.getUsername();
		Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
		
		if(selectedBook.stock == 0) {
			AnchorPane pane = mainApp.getWindowInfo();
			showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(),"Error: No copies available!", "Books not available");
		
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

                        if(cell.getStringCellValue().equals(username)) {
                        	cell = cellIterator.next();
                        	String borrowedBooksISBN = cell.getStringCellValue();
                        	//
                            Pattern pattern = Pattern.compile("(electronic bk.)");
                            Pattern pattern2=Pattern.compile("(ebook)");
                            Pattern pattern3=Pattern.compile(",");
                            Matcher  matcher = pattern.matcher(selectedBook.getISBN());
                            Matcher  matcher2= pattern2.matcher(selectedBook.getISBN());
                            Matcher  matcher3= pattern3.matcher(selectedBook.getISBN());
                            int ebook=  0;
                            int comma=1;
                            while (matcher.find()||matcher2.find())
                                ebook++;
                            while(matcher3.find())
                            	comma++;
                        	
                        	//All borrowed books ISBN are separated to '//' in the excel cell
                            if(comma!=ebook) {
                        	borrowedBooksISBN = borrowedBooksISBN + "//" + selectedBook.getISBN();
                        	cell.setCellValue(borrowedBooksISBN);
                        	AnchorPane pane = mainApp.getWindowInfo();
                			showAlert(Alert.AlertType.CONFIRMATION, pane.getScene().getWindow(),"Book Issued!", "Book Issued!");
                        	System.out.println(borrowedBooksISBN);
                        	
                        	selectedBook.stock--; 
                            }else {
                            	AnchorPane pane = mainApp.getWindowInfo();
                    			showAlert(Alert.AlertType.CONFIRMATION, pane.getScene().getWindow(),"It is only in electronic", "It is only in electronic");
                    			
                            }
                        }
                        
                }
            }
                
                updateBookDetails();
             	workbook.write(outputStream);
                workbook.close();
                excelFile.close();
                
    		
                
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		}
	 }
	 
	 @FXML
	 private void handleReturnBookButton() {
		String username = mainApp.getUsername();
		Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
		
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

                    if(cell.getStringCellValue().equals(username)) {
                    	cell = cellIterator.next();
                    	String borrowedBooksISBN = cell.getStringCellValue();
                    	//All borrowed books ISBN are separated to '//' in the excel cell
                    	boolean bookIndeedBorrowed = false;
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
                    			updateBookDetails();
                    			AnchorPane pane = mainApp.getWindowInfo();
                    			showAlert(Alert.AlertType.CONFIRMATION, pane.getScene().getWindow(),"Book Returned!", "Book Returned!");
                    			bookIndeedBorrowed = true;
                    			break;
                    		}
                    		
                    	}
                    	
                    	if(!bookIndeedBorrowed) {
                    		AnchorPane pane = mainApp.getWindowInfo();
                			showAlert(Alert.AlertType.CONFIRMATION, pane.getScene().getWindow(),"Book was not Issued by this account!", "No such Book borrowed by the User!");
                			
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
	 
	 @FXML
	 private void handleSortButton() {
		 String option = choiceBox.getValue().toString();
		 String sortTypeOption = choiceSortTypeBox.getValue().toString();
		 
		 ArrayList<Book> booksToSort = new ArrayList<Book>(masterData);
		 
		 ArrayList<Book> sortedBooks = new ArrayList<Book>();
		 
		 switch(option) {
		 	case "Author": sortedBooks = mainApp.sortByFeature(booksToSort, option); 
		 	case "Title": sortedBooks = mainApp.sortByFeature(booksToSort, option); 
		 	case "Year": sortedBooks = mainApp.sortByFeature(booksToSort, option); 
		 	case "ISBN": sortedBooks = mainApp.sortByFeature(booksToSort, option); 
		 	case "Publisher": sortedBooks = mainApp.sortByFeature(booksToSort, option); 
		 	case "LLC": sortedBooks = mainApp.sortByFeature(booksToSort, option); 
		 }
		 
		 if(sortTypeOption.equals("Ascending")) {
			 booksInController = sortedBooks;
			 displayBooks();
		 } else if(sortTypeOption.equals("Descending")) {
			 Collections.reverse(sortedBooks);
			 booksInController = sortedBooks;
		 	 displayBooks();
		 }
	 }
	 
	 @FXML
	 private void handleGlobalCheck() {
		 if(globalCheck.isSelected() == true) {
			 authorCheck.setSelected(true);
			 titleCheck.setSelected(true);
			 yearCheck.setSelected(true);
			 publisherCheck.setSelected(true);
			 ISBNCheck.setSelected(true);
			 llcCheck.setSelected(true);
		 }
		 
		 if(globalCheck.isSelected() == false) {
			 authorCheck.setSelected(false);
			 titleCheck.setSelected(false);
			 yearCheck.setSelected(false);
			 publisherCheck.setSelected(false);
			 ISBNCheck.setSelected(false);
			 llcCheck.setSelected(false);
		 }
	 }
	 
	 @FXML
	 private void handleSearchButton() {
		 
		 //boolean globalCheckValue = globalCheck.isSelected();
		 
		 boolean authorCheckValue = authorCheck.isSelected();
		 boolean titleCheckValue = titleCheck.isSelected();
		 boolean yearCheckValue = yearCheck.isSelected();
		 boolean publisherCheckValue = publisherCheck.isSelected();
		 boolean isbnCheckValue = ISBNCheck.isSelected();
		 boolean llcCheckValue = llcCheck.isSelected();
		 
		 
		 
		
		 
		 
		 ArrayList<Book> booksToSearch = new ArrayList<Book>(masterData); 
		 
		 String searchedText = searchText.getText();
		 
		 if((authorCheckValue || titleCheckValue || yearCheckValue || publisherCheckValue || isbnCheckValue || llcCheckValue)==false) {
			AnchorPane pane = mainApp.getWindowInfo();
 			showAlert(Alert.AlertType.ERROR, pane.getScene().getWindow(),"Features to search are not specified!", "Please, choose one or more features to search");
 			
		 }
		 
		 
		 if(searchedText.isEmpty()) {
			 displayBooks();
		 } else {
		 
		 String[] searchedTextArray = searchedText.split(" ");
		 booksFound = mainApp.search(booksToSearch, searchedTextArray, authorCheckValue, titleCheckValue, yearCheckValue, isbnCheckValue, publisherCheckValue, llcCheckValue);
		 displayBooksAfterSearch(booksFound);
		 }
	 }
	 
	 @FXML
	 private void handleExportToExcel() {
		 
		 mainApp.createExcel("lmsdb.xls", booksInController);
		 
	 }
	 
	 @FXML
	 private void handleSeeUsernameAccount() {
		 mainApp.showUserAccount(userName, booksInController);
	 }
	 
	 private void displayBooksAfterSearch(ArrayList<Book> booksFound2) {
		 	ArrayList<Book> booksInController2 = booksFound2;
		 	ObservableList<Book> masterData2 = FXCollections.observableArrayList(booksInController2);
	
			numberingColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("num"));
			authorColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
			titleColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
			publishingYearColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("publishingYear"));
			isbnColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("ISBN"));
			publisherColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
			llcColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("llc"));
			stockColumn.setCellValueFactory(new PropertyValueFactory<Book, Number>("stock"));
			
			bookTable.setItems(masterData2);
	}

	void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
		    Alert alert = new Alert(alertType);
		    alert.setTitle(title);
		    alert.setHeaderText(null);
		    alert.setContentText(message);
		    alert.initOwner(owner);
		    alert.show();
		}
	
	 
	 public void updateBookDetails() {
		 
		 mainApp.showMainView();
	}

	public void displayBooks(ArrayList<Book> books) {
			booksInController = books;
			masterData = FXCollections.observableArrayList(booksInController);
	
			numberingColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("num"));
			authorColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
			titleColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
			publishingYearColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("publishingYear"));
			isbnColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("ISBN"));
			publisherColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
			llcColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("llc"));
			stockColumn.setCellValueFactory(new PropertyValueFactory<Book, Number>("stock"));
			
			bookTable.setItems(masterData);
	 }
	
	public void displayBooks() {

		masterData = FXCollections.observableArrayList(booksInController);

		numberingColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("num"));
		authorColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		publishingYearColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("publishingYear"));
		isbnColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("ISBN"));
		publisherColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
		llcColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("llc"));
		stockColumn.setCellValueFactory(new PropertyValueFactory<Book, Number>("stock"));
		
		bookTable.setItems(masterData);
	}

	public void setUserFunctions(String libraryRole) {
		
		if(libraryRole.equals("Admin")) {
			
		} else {
			addButton.setVisible(false);
			editButton.setVisible(false);
			deleteButton.setVisible(false);
		}
		
	}
	
}
