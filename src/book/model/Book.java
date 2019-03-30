package book.model;

public class Book {
	
	String num;
	String title;
	String author;
	String llc;
	String ISBN;
	String publisher;
	String publishingYear;
	public int stock;
	public int sortingNum;
	
	public Book() {
        this(null, null, null, null, null, null, null, 0);
    }


	public Book(String num, String author, String title, String publishingYear, String ISBN, String publisher, String llc, int stock) {
		// TODO Auto-generated constructor stub
		this.num = num;
		this.title = title;
		this.author = author;
		this.ISBN = ISBN;
		this.publisher = publisher;
		this.publishingYear = publishingYear;
		this.llc = llc;
		this.stock = stock;
		
	}
	
	public String getNum() {
		return this.num;
	}
	
	public String getTitle() {
		return this.title;
	}
	
    public String getAuthor() {
		return this.author;
    }
    
    public String getLlc() {
		return this.llc;
    }
    
    public String getISBN() {
		return this.ISBN;
    }
    
    public String getPublisher() {
		return this.publisher;
    }
	
    public String getPublishingYear() {
		return this.publishingYear;
    }
    
    public int getStock() {
		return this.stock;
    }
    public int getsortingNum() {
    	return this.sortingNum;
    }
    public void setNum(String num) {
    	this.num = num;
    }
    
    public void setTitle(String title) {
    	this.title = title;
    }
	
    public void setAuthor(String author) {
    	this.author = author;
    }
    
    public void setLlc(String llc) {
    	this.llc = llc;
    }
    
    public void setISBN(String ISBN) {
    	this.ISBN = ISBN;
    }
    
    public void setPublisher(String publisher) {
    	this.publisher = publisher;
    }
    
    public void setPublishingYear(String publishingYear) {
    	this.publishingYear = publishingYear;
    }
	
    public void setStock(int stock) {
    	this.stock = stock;
    }
    public void setSortingNum(int sortingNum) {
    	this.sortingNum=sortingNum;
    }
    
}
