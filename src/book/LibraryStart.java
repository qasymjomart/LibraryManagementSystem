package book;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.util.ArrayList;
import java.util.Iterator;
//import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import book.view.LoginController;
import book.view.RegistrationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LibraryStart extends Application {

	public static String FILE_NAME = "users.xlsx";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	Stage flexibleStage;
	Stage Registration;
	Scene scene;
	AnchorPane anchorPane;
	AnchorPane signUpPane;
	
	String userRole;
	
	@Override
    public void start(Stage primaryStage) {
		
		flexibleStage = primaryStage;
		Registration = primaryStage;
		showLoginForm();
        
	}
	
	public void showLoginForm() {
		
		try {
			FXMLLoader loaderLogin = new FXMLLoader();
			loaderLogin.setLocation(getClass().getResource("view/LoginLayout.fxml"));
			anchorPane = (AnchorPane) loaderLogin.load();
			Scene libraryScene = new Scene(anchorPane);
			flexibleStage.setScene(libraryScene);
			flexibleStage.setTitle("Library Management System");
	        Image image = new Image("file:images/open-book.png");
	        flexibleStage.getIcons().add(image); 
	        flexibleStage.show();
			
			LoginController loginController = loaderLogin.getController();
			loginController.setMainApp(this);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	
		
	public void showSignUpForm() {
		
		try {
			FXMLLoader loaderSignUp = new FXMLLoader();
			loaderSignUp.setLocation(getClass().getResource("view/RegistrationLayout.fxml"));
			signUpPane = (AnchorPane) loaderSignUp.load();
			Scene libraryScene1 = new Scene(signUpPane);
			Registration.setScene(libraryScene1);
			Registration.setTitle("Library Management System");
	        Image image = new Image("file:images/open-book.png");
	        Registration.getIcons().add(image); 
	        Registration.show();
			
			RegistrationController signUpController = loaderSignUp.getController();
			signUpController.setMainApp(this);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
		
	
	int rowNumber =0;

	public boolean checkUsernameExists(String newUsername){
		
		try {
			disableWarning();
            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
        	Workbook workbook = new XSSFWorkbook(excelFile);
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);

        	
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

                while (iterator.hasNext()) {
                    Row currentRow = iterator.next();
                    //Iterator<Cell> cellIterator = currentRow.cellIterator();
                    rowNumber = currentRow.getRowNum();

                   //while (cellIterator.hasNext()) {
    	            Iterator<Cell> cellIterator = currentRow.iterator();
                    Cell currentCell = cellIterator.next();
	                String testName = currentCell.getStringCellValue();
                    if(newUsername.equals(testName) || newUsername.equals("admin")) {
                      	workbook.write(outputStream);

                        workbook.close();
                        excelFile.close();
                    	return true;
                    	
                    } else {
                      	workbook.write(outputStream);

                        workbook.close();
                        excelFile.close();
                        
                    	return false;
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

		return false;
	}
				
	boolean newUserInsertedIntoDatabase = false;
	boolean newUserInsertedIntoBookIssue = false;
	
		
	public boolean signUpNewUser(String newUsername, String newPassword) {
		    	
		        try {
		        	disableWarning();
		            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
		        	Workbook workbook = new XSSFWorkbook(excelFile);
	                FileOutputStream outputStream = new FileOutputStream(FILE_NAME);

		        	
		            Sheet datatypeSheet = workbook.getSheetAt(0);
		            Iterator<Row> iterator = datatypeSheet.iterator();

		                while (iterator.hasNext()) {
		                    Row currentRow = iterator.next();
		                    //Iterator<Cell> cellIterator = currentRow.cellIterator();
		                    rowNumber = currentRow.getRowNum();

		                   //while (cellIterator.hasNext()) {
		                    
		                        
		                //}
		                }

		                Row row = datatypeSheet.createRow(rowNumber + 1);
		                int columnCount = 0;

		                Cell cell = row.createCell(columnCount++);
		                cell.setCellValue(newUsername);
		                cell = row.createCell(columnCount++);
		                cell.setCellValue(newPassword);

		               	workbook.write(outputStream);
		               	
		            
		               	newUserInsertedIntoDatabase = true;
		               	
		               	
		                workbook.close();
		                excelFile.close();
		                
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        
		        
		        try {
		        	FileInputStream excelFile = new FileInputStream(new File("bookissue.xlsx"));
		        	Workbook workbook = new XSSFWorkbook(excelFile);
	                FileOutputStream outputStream = new FileOutputStream("bookissue.xlsx");

		        	
		            Sheet datatypeSheet = workbook.getSheetAt(0);
		            Iterator<Row> iterator = datatypeSheet.iterator();
		            
		            while (iterator.hasNext()) {
	                    Row currentRow = iterator.next();
	                    //Iterator<Cell> cellIterator = currentRow.cellIterator();
	                    rowNumber = currentRow.getRowNum();

	                   //while (cellIterator.hasNext()) {
	                    
	                        
	                //}
	                }
		            
		            Row row = datatypeSheet.createRow(rowNumber + 1);
	                int columnCount = 0;

	                Cell cell = row.createCell(columnCount++);
	                cell.setCellValue(newUsername);
	                cell = row.createCell(columnCount++);
	                cell.setCellValue("Bookstaken");

	               	workbook.write(outputStream);
	               	
	               	newUserInsertedIntoBookIssue = true;
	               	
	                workbook.close();
	                excelFile.close();
		        	
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        
		        if(newUserInsertedIntoDatabase && newUserInsertedIntoBookIssue) {
		        	return true;
		        } else {
		        	return false;
		        }
		        
	}
	

	
	
	
	public boolean checkUser(String username, String password) {
		
		boolean login = false;
		username.trim();
		password.trim();
        
        try {
        	disableWarning();
            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
        	Workbook workbook = new XSSFWorkbook(excelFile);
            
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
                
            int j = 0;
            
                while (iterator.hasNext()) {
                	j = 0;
                    Row currentRow = iterator.next();
                    Iterator<Cell> cellIterator = currentRow.cellIterator();
                    currentRow.getRowNum();
                    
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        
                        if(cell.getStringCellValue().equals(username)) {
                        	j++;
                        }
                        if((cell.getStringCellValue().equals(password)) && (j == 1)) {
                        	j++;
                        }
                        
                        if(j == 2) {
                        	login = true;
                        }
                        
                }
            }
                workbook.close();
                excelFile.close();
                
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
		return login;
	}
	
	public static void disableWarning() {
	    System.err.close();
	    System.setErr(System.out);
	}


	public AnchorPane getWindowInfo() {
		return anchorPane;
	}


	public Stage getStageBack() {
		return flexibleStage;
	}
	
	public Stage getSignUpStageBack() {
		return Registration;
	}

}
