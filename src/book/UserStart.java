package book;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import book.model.Book;
import book.view.EditBookLayoutController;
import book.view.LayoutController;
import book.view.UserAccountController;


public class UserStart {
	
	String[] Info= {"","","","","","","",""};
	private ArrayList<Book> books = new ArrayList<Book>();
	
	Stage userStage; 
	Scene userLibraryScene;
	AnchorPane userLayout;
	AnchorPane userAccountLayout;
	
	LayoutController controller;
	
	String userName;
	String libraryRole;
	
	public void userPane(Stage flexibleStage, String username, String userRole) {
		
		userName = username;
		libraryRole = userRole;
		
		userStage = flexibleStage;
		//User account Pane goes below
		
		ReadExcel("lmsdb.xls");
		showMainView();
		//System.out.println(books.get(5).getStock());
		//for(int i = 0; i < books.size(); i++) {
		//	System.out.println(books.get(i).getStock());	
		//}		
		
		
	
	}	
	
	
	
	public void showMainView() {
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/LayoutLibrary.fxml"));
			userLayout = (AnchorPane) loader.load();
			Scene userScene1 = new Scene(userLayout);
			userStage.setScene(userScene1);
			userStage.show();
			
			controller = loader.getController();
			controller.setMainApp(this);
			controller.setUserFunctions(libraryRole);
			controller.displayBooks(books);
			controller.setUsernameDisplay(userName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//It is to read Excel and Create ArrayList<Book> books
	void ReadExcel(String FILE_NAME) {
		try {

	        FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
	        Workbook workbook = new HSSFWorkbook(excelFile);
	        Sheet datatypeSheet = workbook.getSheetAt(0);
	        Iterator<Row> iterator = datatypeSheet.iterator();
	        int stock=0;
	        while (iterator.hasNext()) {

	            Row currentRow = iterator.next();
	            
	            Iterator<Cell> cellIterator = currentRow.iterator();
	            int i=0;
	            String[] data= {"","","","","","","","0"};
	            while (cellIterator.hasNext()&&stock==0) {
	            	Cell currentCell = cellIterator.next();
	                //getCellTypeEnum shown as deprecated for version 3.15
	                //getCellTypeEnum ill be renamed to getCellType starting from version 
	                    Info[i]=currentCell.getStringCellValue();
	               i++;
	            }
	            while (cellIterator.hasNext()&&stock>0) {
	       
	                Cell currentCell = cellIterator.next();
	                //getCellTypeEnum shown as deprecated for version 3.15
	                //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
	                
	                if (currentCell.getCellTypeEnum() == CellType.STRING) {
	                    data[i]=currentCell.getStringCellValue();
	                    
	                } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
	                    data[i]=Double.toString(currentCell.getNumericCellValue());
	                }
	                i++;
	            }
	            if(stock>0) 
	            books.add(new Book(data[0], data[1], data[2], data[3],data[4],data[5],data[6],(int)Double.parseDouble(data[7])));
	            stock++;
	        }
	        workbook.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	}
	
	AnchorPane bookEditPane;
	
	
	
	
	
	//Show Edit Book Window
	public boolean showEditWindow(Book selectedBook) {
		try {
			
			FXMLLoader editBookWindowLoader = new FXMLLoader();
			editBookWindowLoader.setLocation(getClass().getResource("view/EditBookLayout.fxml"));
            bookEditPane = (AnchorPane) editBookWindowLoader.load();

            Stage editBookStage = new Stage();
            editBookStage.setTitle("Edit book");
            editBookStage.initModality(Modality.WINDOW_MODAL);
            editBookStage.initOwner(userStage);
            Scene editBookScene = new Scene(bookEditPane);
            editBookStage.setScene(editBookScene);
			

            // Give the controller access to the mainApp.
            EditBookLayoutController controller = editBookWindowLoader.getController();
            controller.setCurrentStage(editBookStage);
            controller.setEditingBook(selectedBook);
            controller.setMainApp(this);
			editBookStage.showAndWait();
			
			return controller.editFinished();

        } catch (IOException e) {
            e.printStackTrace();
        }
		return false;
		
	}
	
	public AnchorPane getEditBookWindowInfo() {
		return bookEditPane;
	}
	
	//It is to recreate our File(Data)
	public void createExcel(String FILE_NAME, ArrayList<Book> booksInController) {
		 HSSFWorkbook workbook = new HSSFWorkbook();
	    HSSFSheet sheet = workbook.createSheet("Datatypes in Java");
	    int rowNum = 0;
	    //System.out.println("Creating excel");
	    
	    for (int i = -1; i < booksInController.size(); i++) {
	        Row row = sheet.createRow(rowNum++);
	        if(rowNum==1) { //It is done to show INFO part (Author Title...) 1st stage
	        	int colNum=0;
	        	 Cell cell = row.createCell(colNum++);
	             cell.setCellValue((String)Info[(colNum-1)]);
	             cell = row.createCell(colNum++);
	             cell.setCellValue((String)Info[(colNum-1)]);
	             cell = row.createCell(colNum++);
	             cell.setCellValue((String)Info[(colNum-1)]);
	             cell = row.createCell(colNum++);
	             cell.setCellValue((String)Info[(colNum-1)]);
	             cell = row.createCell(colNum++);
	             cell.setCellValue(Info[(colNum-1)]);
	             cell = row.createCell(colNum++);
	             cell.setCellValue(Info[(colNum-1)]);
	             cell = row.createCell(colNum++);
	             cell.setCellValue(Info[(colNum-1)]);
	             cell = row.createCell(colNum++);
	             cell.setCellValue(Info[(colNum-1)]);
	        }else {
	        int colNum = 0;
	            Cell cell = row.createCell(colNum++);
	            cell.setCellValue((String) booksInController.get(i).getNum());
	            cell = row.createCell(colNum++);
	            cell.setCellValue((String) booksInController.get(i).getAuthor());
	            cell = row.createCell(colNum++);
	            cell.setCellValue((String) booksInController.get(i).getTitle());
	            cell = row.createCell(colNum++);
	            cell.setCellValue((String) booksInController.get(i).getPublishingYear());
	            cell = row.createCell(colNum++);
	            cell.setCellValue((String) booksInController.get(i).getISBN());
	            cell = row.createCell(colNum++);
	            cell.setCellValue((String) booksInController.get(i).getPublisher());
	            cell = row.createCell(colNum++);
	            cell.setCellValue((String) booksInController.get(i).getLlc());
	            cell = row.createCell(colNum++);
	            cell.setCellValue((Integer)booksInController.get(i).getStock());
	        }
	    }
	    try {
	        FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
	        workbook.write(outputStream);
	        workbook.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    System.out.println("Done");
	}


	public AnchorPane getWindowInfo() {
		return userLayout;
	}


	public String getUsername() {
		return userName;
	}


	public ArrayList<Book> sortByFeature(ArrayList<Book> booksToSort, String option) {
        
		ArrayList<Book> left = new ArrayList<Book>();
	    ArrayList<Book> right = new ArrayList<Book>();
	    int center;
	    if (booksToSort.size() == 1) {    
	        return booksToSort;
	    } else {
	        center = booksToSort.size()/2;
	        // copy the left half of whole into the left.
	        for (int i=0; i<center; i++) {
	                left.add(booksToSort.get(i));
	        }

	        //copy the right half of whole into the new arraylist.
	        for (int i=center; i<booksToSort.size(); i++) {
	                right.add(booksToSort.get(i));
	        }

	        // Sort the left and right halves of the arraylist.
	        left  = sortByFeature(left, option);
	        right = sortByFeature(right, option);

	        // Merge the results back together.
	        Merge(booksToSort, left, right, option);
	    }

	    return booksToSort;
		
	}
	
	public void Merge(ArrayList<Book> titles, ArrayList<Book> left, ArrayList<Book> right, String option) {
	    int leftIndex = 0;
	    int rightIndex = 0;
	    int wholeIndex = 0;

	    // As long as neither the left nor the right ArrayList has
	    // been used up, keep taking the smaller of left.get(leftIndex)
	    // or right.get(rightIndex) and adding it at both.get(bothIndex).
	    
	    if(option.equals("Author")) {
	    
	    while (leftIndex < left.size() && rightIndex < right.size()) {
	        if ( (left.get(leftIndex).getAuthor().compareTo(right.get(rightIndex).getAuthor())) < 0) {
	            titles.set(wholeIndex, left.get(leftIndex));
	            leftIndex++;
	        } else {
	            titles.set(wholeIndex, right.get(rightIndex));
	            rightIndex++;
	        }
	        wholeIndex++;
	    }
	    
	    }
	    
	    if(option.equals("Title")) {
		    
		    while (leftIndex < left.size() && rightIndex < right.size()) {
		        if ( (left.get(leftIndex).getTitle().compareTo(right.get(rightIndex).getTitle())) < 0) {
		            titles.set(wholeIndex, left.get(leftIndex));
		            leftIndex++;
		        } else {
		            titles.set(wholeIndex, right.get(rightIndex));
		            rightIndex++;
		        }
		        wholeIndex++;
		    }
		    
		 }
	    
	    if(option.equals("Year")) {
		   
		    while (leftIndex < left.size() && rightIndex < right.size()) {
		        if ( (left.get(leftIndex).getPublishingYear().replaceAll("©","").replaceAll(" ", "").compareTo(right.get(rightIndex).getPublishingYear().replaceAll("©","").replaceAll(" ", ""))) < 0) {
		            titles.set(wholeIndex, left.get(leftIndex));
		            leftIndex++;
		        } else {
		            titles.set(wholeIndex, right.get(rightIndex));
		            rightIndex++;
		        }
		        wholeIndex++;
		    }
		    
		 }
	    
	    if(option.equals("ISBN")) {
		    
		    while (leftIndex < left.size() && rightIndex < right.size()) {
		        if ( (left.get(leftIndex).getISBN().compareTo(right.get(rightIndex).getISBN())) < 0) {
		            titles.set(wholeIndex, left.get(leftIndex));
		            leftIndex++;
		        } else {
		            titles.set(wholeIndex, right.get(rightIndex));
		            rightIndex++;
		        }
		        wholeIndex++;
		    }
		    
		 }
	    
	    if(option.equals("Publisher")) {
		    
		    while (leftIndex < left.size() && rightIndex < right.size()) {
		        if ( (left.get(leftIndex).getPublisher().compareTo(right.get(rightIndex).getPublisher())) < 0) {
		            titles.set(wholeIndex, left.get(leftIndex));
		            leftIndex++;
		        } else {
		            titles.set(wholeIndex, right.get(rightIndex));
		            rightIndex++;
		        }
		        wholeIndex++;
		    }
		    
		 }
	    
	    if(option.equals("LLC")) {
		    
		    while (leftIndex < left.size() && rightIndex < right.size()) {
		        if ( (left.get(leftIndex).getLlc().compareTo(right.get(rightIndex).getLlc())) < 0) {
		            titles.set(wholeIndex, left.get(leftIndex));
		            leftIndex++;
		        } else {
		            titles.set(wholeIndex, right.get(rightIndex));
		            rightIndex++;
		        }
		        wholeIndex++;
		    }
		    
		 }
	    
	    if(option.equals("SortNum")){
		    
		    while (leftIndex < left.size() && rightIndex < right.size()) {
		        if ( ((left.get(leftIndex).getsortingNum()>right.get(rightIndex).getsortingNum())) == true) {
		            titles.set(wholeIndex, left.get(leftIndex));
		            leftIndex++;
		        } else {
		            titles.set(wholeIndex, right.get(rightIndex));
		            rightIndex++;
		        }
		        wholeIndex++;
		    }
		    
		 }
	    

	    ArrayList<Book> rest;
	    int restIndex;
	    if (leftIndex >= left.size()) {
	        // The left ArrayList has been use up...
	        rest = right;
	        restIndex = rightIndex;
	    } else {
	        // The right ArrayList has been used up...
	        rest = left;
	        restIndex = leftIndex;
	    }

	    // Copy the rest of whichever ArrayList (left or right) was not used up.
	    for (int i=restIndex; i<rest.size(); i++) {
	        titles.set(wholeIndex, rest.get(i));
	        wholeIndex++;
	    }
	}


	public ArrayList<Book> search(ArrayList<Book> booksToSearch, String[] searchedTextArray, boolean authorCheckValue, boolean titleCheckValue, boolean yearCheckValue, boolean isbnCheckValue, boolean publisherCheckValue, boolean llcCheckValue) {
		
		ArrayList<Book> found = new ArrayList<Book>();
		String[] bookNumbers=new String[booksToSearch.size()];
		String s = "";
	    for (String n:searchedTextArray)
	        s+= n+" ";
	    char[] c = s.toCharArray();
	    String str = String.valueOf(c);
	    System.out.print(str);
		for(int i=0;i<booksToSearch.size();i++) {
			int z=0;
			
				if(authorCheckValue) {
				if(booksToSearch.get(i).getAuthor().matches("(?i:.*"+str+".*)"))
					z++;
				}
				
				if(titleCheckValue) {
				if(booksToSearch.get(i).getTitle().matches("(?i:.*"+str+".*)"))
					z++;
				}
				
				if(yearCheckValue) {
				if(booksToSearch.get(i).getPublishingYear().matches("(?i:.*"+str+".*)"))
					z++;
				}
				
				if(isbnCheckValue) {
				if(booksToSearch.get(i).getISBN().matches("(?i:.*"+str+".*)"))
					z++;
				}
				
				if(publisherCheckValue) {
				if(booksToSearch.get(i).getPublisher().matches("(?i:.*"+str+".*)"))
					z++;
				}
				
				if(llcCheckValue) {
				if(booksToSearch.get(i).getLlc().matches("(?i:.*"+str+".*)"))
					z++;
				}
				
			
			if(z>=1) {
				booksToSearch.get(i).setSortingNum(10000000);
				found.add(booksToSearch.get(i));
				 bookNumbers[i]=booksToSearch.get(i).getNum();
				System.out.print(z);
			}
		}
		
		for(int i=0;i<booksToSearch.size();i++) {
			int z=0;
			
			for(int j=0;j<searchedTextArray.length;j++) {
				
				if(authorCheckValue) {
				if(booksToSearch.get(i).getAuthor().matches("(?i:.*"+searchedTextArray[j]+".*)"))
					z++;
				}
				
				if(titleCheckValue) {
				if(booksToSearch.get(i).getTitle().matches("(?i:.*"+searchedTextArray[j]+".*)"))
					z++;
				}
				
				if(yearCheckValue) {
				if(booksToSearch.get(i).getPublishingYear().matches("(?i:.*"+searchedTextArray[j]+".*)"))
					z++;
				}
				
				if(isbnCheckValue) {
				if(booksToSearch.get(i).getISBN().matches("(?i:.*"+searchedTextArray[j]+".*)"))
					z++;
				}
				
				if(publisherCheckValue) {
				if(booksToSearch.get(i).getPublisher().matches("(?i:.*"+searchedTextArray[j]+".*)"))
					z++;
				}
				
				if(llcCheckValue) {
				if(booksToSearch.get(i).getLlc().matches("(?i:.*"+searchedTextArray[j]+".*)"))
					z++;
				}
				
			}
			if(z>=searchedTextArray.length&&bookNumbers[i]!=booksToSearch.get(i).getNum()) {
				booksToSearch.get(i).setSortingNum(z);
				found.add(booksToSearch.get(i));
			}
		}
		
		found=sortByFeature(found, "SortNum");
		return found;

		
	}


	public void showUserAccount(String userName2, ArrayList<Book> booksInController) {
		
		try {
			FXMLLoader userAccountLoader = new FXMLLoader();
			userAccountLoader.setLocation(getClass().getResource("view/UserAccountLayout.fxml"));
			userAccountLayout = (AnchorPane) userAccountLoader.load();
			Scene userAccountScene = new Scene(userAccountLayout);
						

            // Give the controller access to the mainApp.
			
			Stage userAccountStage = new Stage();
			userAccountStage.setTitle("User account");
			userAccountStage.initModality(Modality.WINDOW_MODAL);
			userAccountStage.initOwner(userStage);
			userAccountStage.setScene(userAccountScene);
			
			UserAccountController controllerUserAccount = userAccountLoader.getController();
			controllerUserAccount.setStage(userAccountStage);
			controllerUserAccount.setUsernameToDisplay(userName2);
			System.out.print("Hey I am here");
			ArrayList<Book> borrowedBooks = booksBorrowedByUser(userName2, booksInController);
			controllerUserAccount.initializeBooks(borrowedBooks);
			controllerUserAccount.setMainApp(this);
			userAccountStage.showAndWait();

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	ArrayList<Book> borrowedBooks;
	ArrayList<Book> booksFromController;


	private ArrayList<Book> booksBorrowedByUser(String userName2, ArrayList<Book> booksInController) {
		booksFromController = booksInController;
		borrowedBooks = new ArrayList<Book>();
		
		try {
            FileInputStream excelFile = new FileInputStream(new File("bookissue.xlsx"));
        	Workbook workbook = new XSSFWorkbook(excelFile);

            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            
            while (iterator.hasNext()) {
                
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.cellIterator();
                currentRow.getRowNum();
                
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                	System.out.println(cell.getStringCellValue());

                    if(cell.getStringCellValue().equals(userName2)) {
                    	cell = cellIterator.next();
                    	String borrowedBooksISBN = cell.getStringCellValue();
                    	//All borrowed books ISBN are separated to '//' in the excel cell
                    	String[] booksBorrowed = borrowedBooksISBN.split("//");
                    	for(int i = 0; i < booksBorrowed.length; i++) {
                    		for(int k = 0; k < booksInController.size();k++) {
                    			if(booksBorrowed[i].equals(booksInController.get(k).getISBN())) {
                    				borrowedBooks.add(booksInController.get(k));
                    			}
                    		}
                    	}
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
		
		return borrowedBooks;
		
	}


	public void actionsAfterBookReturnedAtUserAccount(String userName2, Book selectedBook) {
			
		for(int i = 0; i < booksFromController.size(); i++) {
				if((booksFromController.get(i).getISBN()).equals(selectedBook.getISBN())) {
					booksFromController.get(i).setStock(booksFromController.get(i).stock);
				}
		}
		
		books = booksFromController;
		showMainView();
	

		
	}

	
	
	
}
