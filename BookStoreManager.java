import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;


/**
 *------------------------------------
 * BookStoreManager
 * Part: 1, 2
 * Written by Keyshawn James 
 * --------------------------------------
 * 
 * This Java program, BookStoreManager, serves as a comprehensive tool to assist 
 * Mr. Booker, a retired bookstore employee, in managing and navigating through 
 * his extensive book catalog.
 * 
 * The program is designed in two parts:
 * 
 * Part 1:
 *  - Reads and validates book records from CSV-formatted text files, checking for syntax errors.
 *  - Categorizes valid records into genre-specific CSV output files.
 *  - Logs syntax errors in a dedicated file (syntax_error_file.txt).
 *  
 * Part 2:
 *  - Reads genre based CSV files from Part 1, creating an array of valid Book objects.
 *  - Checks each record for semantic errors (ISBN, price, and year validation).
 *  - Serializes valid Book objects into binary files for efficient storage.
 *  
 */


/**
 *  Exceptions for Part 1 ==========================
 */
class TooManyFieldsException extends Exception {
	public TooManyFieldsException() {
		super("Error: too many Fields");
	}
}

class TooFewFieldsException extends Exception{
	public TooFewFieldsException() {
		super("Error: too few fields");
	}
}

class MissingFieldException extends Exception{
	public MissingFieldException() {
		super("Error: missing field");
	}
	
	public MissingFieldException(String message){
		super(message);
	}
}

class UnknownGenreException extends Exception{
	public UnknownGenreException() {
		super("Error: ivalid genre");
	}
}


/**
 *  Exceptions for Part 2 ==========================
 */
class BadIsbn10Exception extends Exception{
	BadIsbn10Exception(String message){
		super(message);		
	}		
}

class BadIsbn13Exception extends Exception{
	BadIsbn13Exception(String message){
		super(message);		
	}		
}

class BadIsbnException extends Exception{
	BadIsbnException(String message){
		super(message);
	}
}

class BadPriceException extends Exception{
	BadPriceException(String message){
		super(message);		
	}		
}

class BadYearException extends Exception{
	BadYearException(String message){
		super(message);		
	}		
}

/**
 *  Book class =================================
 */
class Book implements Serializable {
 private String title;
 private String authors;
 private double price;
 private String isbn;
 private String genre;
 private int year;

 /**
  * Constructor
  * @param title
  * @param authors
  * @param price
  * @param isbn
  * @param genre
  * @param year
  */
 public Book(String title, String authors, double price, String isbn, String genre, int year) {
     this.title = title;
     this.authors = authors;
     this.price = price;
     this.isbn = isbn;
     this.genre = genre;
     this.year = year;
 }

}

public class BookStoreManager {
	

	/**
	 * =======================================================================================
	 * Part 1
	 * ========================================================================================
	 */
	
	/**
	 * Book counter to count all books
	 */
	private static int bookCounter;
	
	public static void doPart1() {
		String fileName = "part1_input_file_names.txt";
		String syntaxErrorFile = "syntax_error_file.txt";
		
		/**
		 * Array of book genres
		 */
		String[] bookGenres = {
				"CCB", 
				"HCB", 
				"MTV", 
				"MRB", 
				"NEB", 
				"OTR", 
				"SSM", 
				"TPA"		
		};
		
		/**
		 * Array of the files to be creates
		 */
		String[] bookFiles = {
				"Cartoons_Comics_Books.csv.txt",
				"Hobbies_Collectibles_Books.csv.txt",
				"Movies_TV.csv.txt",
				"Music_Radio_Books.csv.txt",
				"Nostalgia_Eclectic_Books.csv.txt",
				"Old_Time_Radio.csv.txt",
				"Sports_Sports_Memorabilia.csv.txt",
				"Trains_Planes_Automobiles.csv.txt",
		};
		
		String book;
		int n,  numOfBooks = 0;
		int numberOfBooks = 0;
		Scanner sc = null;
		PrintWriter pw = null;
		PrintWriter writeToBookFile = null;
		Scanner sc2 = null;
		String bookContent =""; // Content of each line within the files
		String bookValues[] = null;
		String genreToWriteTo = "";
		
		try
		{
			System.out.println("Attempting to to open the file from the file: " + fileName);
			
			/**
			 * Scanner for the inside of part1_input_file_names.txt file
			 */
			sc = new Scanner(new FileInputStream(fileName)); 
			
			/**
			 * PrintWriter for the syntax_error_file.txt file
			 */
			pw = new PrintWriter(new FileOutputStream(syntaxErrorFile)); 
			
			/**
			 * For console
			 */
			System.out.println("Here are the cotents of the file: " + fileName); 
			
			 /**
			  * Number of book files in the input file (part1_input_file_names.txt)
			  */
			n = sc.nextInt(); 
			
			/**
			 * Skip line to access the books
			 */
			book = sc.nextLine(); 
			while (sc.hasNextLine())
			{
				/**
				 * The current book name file name
				 */
				book = sc.nextLine(); 
				
				/**
				  * For the console
				  */
				System.out.println("Book-" + numOfBooks++  + " " + book); 
				
				try
				{
					/**
					 * Scanner to open stream of the name of the file in part1_input_file_names.txt 
					 */
					sc2 = new Scanner(new FileInputStream(book)); 
					while (sc2.hasNextLine())
					{
						try
						{
							numberOfBooks++;
							bookContent = sc2.nextLine();
							
							/**
							 * This ignores the commas within the double quote marks using positive lookahead 
							 */
							bookValues = bookContent.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); 
							
							if(bookValues.length > 6) {
								throw new TooManyFieldsException();
							}
							
							if(bookValues.length < 6) {
								throw new TooFewFieldsException();
							}
							
							/**
							 * Testing for Missing fields
							 */
							if(bookValues[0].isEmpty()) {
								throw new MissingFieldException("Error: missing title");
							}
							
							if(bookValues[1].isEmpty()) {
								throw new MissingFieldException("Error: missing authors");
							}
							
							if(bookValues[2].isEmpty()) {
								throw new MissingFieldException("Error: missing price");
							}
							
							if(bookValues[3].isEmpty()) {
								throw new MissingFieldException("Error: missing isbn");
							}
							
							if(bookValues[4].isEmpty()) {
								throw new MissingFieldException("Error: missing genre");
							}
							
							if(bookValues[5].isEmpty()) {
								throw new MissingFieldException("Error: missing year");
							}
					
							String genreForThisBook = bookValues[4];
							
							/**
							 * Validate genre for the book and determine with book genre file to write to
							 */
							if(Arrays.asList(bookGenres).contains(genreForThisBook)) 
							{	
								if (genreForThisBook.equals("CCB"))
								{
									genreToWriteTo = "Cartoons_Comics_Books.csv.txt";
								}
								
								else if (genreForThisBook.equals("HCB")) 
								{
									genreToWriteTo = "Hobbies_Collectibles_Books.csv.txt";
								}
								
								else if (genreForThisBook.equals("MTV")) 
								{
									genreToWriteTo = "Movies_TV.csv.txt";
								}
								
								else if (genreForThisBook.equals("MRB")) 
								{
									genreToWriteTo = "Music_Radio_Books.csv.txt";
								}
								
								else if (genreForThisBook.equals("NEB")) 
								{
									genreToWriteTo = "Nostalgia_Eclectic_Books.csv.txt";
								}
								
								else if (genreForThisBook.equals("OTR"))
								{
									genreToWriteTo = "Old_Time_Radio.csv.txt";
								}
								
								else if (genreForThisBook.equals("SSM")) 
								{
									genreToWriteTo = "Sports_Sports_Memorabilia.csv.txt";
								}
								
								else if (genreForThisBook.equals("TPA")) 
								{
									genreToWriteTo = "Trains_Planes_Automobiles.csv.txt";
								}
								else {
									throw new UnknownGenreException();
								}
							}
							try 
							{
								writeToBookFile = new PrintWriter(new FileOutputStream(genreToWriteTo,true));
								writeToBookFile.println(bookContent);
								writeToBookFile.close();
							}
							finally{}
								
						}
						/**
						 * Catching exceptions and writing them in the syntax_error_file.txt file
						 */
						catch (TooManyFieldsException | TooFewFieldsException | MissingFieldException | UnknownGenreException e) { 
							pw.println("Syntax error in file: " + book);
							pw.println("====================");
							pw.println(e.getMessage());	
							pw.println("Record: " + bookContent + "\n" + "\n");
						}
						
						catch (FileNotFoundException e)
						{
							System.out.println("This file: " + genreToWriteTo + " could not be found");
						}	
					}
					sc2.close();
				}
				catch (FileNotFoundException e)
				{
					System.out.println("This book file with the title of: " + book + " does not exist.");
				} 
			}
			/**
			 * .close()
			 */
		
			sc.close();
			pw.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Sorry this file: " + fileName + " does not exist!");
		}
	}
	
	
	/**
	 * =======================================================================================
	 * Part 2
	 * ========================================================================================
	 */
	
	/**
	 * Validate ISBN-10
	 * @param bookIsbn
	 * @throws BadIsbn10Exception
	 */
	public static void Isbn10Validation(String bookIsbn) throws BadIsbn10Exception {
		int sum = 0;
		for (int i = 0; i < 9; i++) {
			int charDigit = Character.getNumericValue(bookIsbn.charAt(i));
			sum = sum + (10-i) * charDigit;
		}
		
		int lastCharDigit;
		
		if (bookIsbn.charAt(9) == 'X') {
		    lastCharDigit = 10;
		} else {
		    lastCharDigit = Character.getNumericValue(bookIsbn.charAt(9));
		}
		sum += lastCharDigit;
		
		if (sum % 11 != 0) {
			throw new BadIsbn10Exception("This ISBN-10: " + bookIsbn + " is invalid");
		}
	}
	
	/**
	 * Validate ISBN-13
	 * @param bookIsbn
	 * @throws BadIsbn13Exception
	 */
	public static void Isbn13Validation(String bookIsbn) throws BadIsbn13Exception {
		int sum = 0;
		int multiplier;
		for (int i = 0; i < 13; i++) {
			int charDigit = Character.getNumericValue(bookIsbn.charAt(i));
			if (i % 2 == 0) {
				multiplier = 1;
			}
			else {
				multiplier = 3;
			}
			sum = sum + multiplier * charDigit;
		}
		if (sum % 10 != 0) {
			throw new BadIsbn13Exception("This ISBN-13: " + bookIsbn + " is invalid");
		}
	}
	
	/**
	 * Validate the ISBN
	 * @param bookIsbn
	 * @throws BadIsbn10Exception
	 * @throws BadIsbn13Exception
	 * @throws BadIsbnException
	 */
	public static void IsbnValidation(String bookIsbn) throws BadIsbn10Exception, BadIsbn13Exception, BadIsbnException{
		if (bookIsbn.length() == 10) {
			Isbn10Validation(bookIsbn);
		}
		else if (bookIsbn.length() == 13) {
			Isbn13Validation(bookIsbn);
		}
		else
		{
			throw new BadIsbnException("This ISBN: " + bookIsbn + " is invalid");
		}
		
	}
	
	/**
	 * Validate price
	 * @param bookPrice
	 * @throws BadPriceException
	 */
	public static void PriceValidation(double bookPrice) throws BadPriceException{
		if (bookPrice < 0) {
			throw new BadPriceException("This bookPrice: " + bookPrice + " is invalid. Price cannot be negative" );
		}
	}
	
	/**
	 * Validate year
	 * @param bookYear
	 * @throws BadYearException
	 */
	public static void YearValidation(int bookYear) throws BadYearException{
		if (bookYear < 1995 || bookYear > 2010) {
			throw new BadYearException("This year: " + bookYear + " is invalid as it is not within the year 1995 and 2010");
		}
		
	}
	
	/**
	 * Method to write errors in the semantic errors file
	 * @param bookFileName
	 * @param bookErrorMessage
	 * @param bookRecord
	 */
	public static void writeSemanticErrors(String bookFileName, String bookErrorMessage, String bookRecord) {
		FileWriter fw= null;
		BufferedWriter bw = null;
		PrintWriter pw = null;
		
		try
		{
			fw = new FileWriter("semantic_error_file.txt", true);
			bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
			
			pw.println("Semantic error in file: " + bookFileName);
			pw.println("======================");
			pw.println("Error: " + bookErrorMessage);
			pw.println("Record: " + bookRecord + "\n" + "\n");
			pw.close();
			bw.close();
			fw.close();
		}
		catch (IOException e)
		{
            e.printStackTrace();
		}
	}
	
	
	public static void bookSerialization (Book[] books, String outputBookFile) {
		ObjectOutputStream oos = null;
		try
		{
			oos = new ObjectOutputStream (new FileOutputStream(outputBookFile));
			oos.writeObject(books);
			oos.close();
		}
		catch(IOException e)
		{
            e.printStackTrace();
		}
	}
	
	public static Book[] addTheBookToArray(Book[] books, Book currentBook, int bookCount) {
		if (bookCount == books.length) {
			Book[] newBookSize = new Book[books.length * 2];
			System.arraycopy(currentBook, 0, newBookSize, 0, books.length);
			books = newBookSize;
		}
		
		books[bookCount] = currentBook;
		return books;
	}
	
	public static void doPart2() {
		/**
		 *  Array of the validSyntaxFilles
		 */
		String[] validSyntaxBookFiles = {
			"Cartoons_Comics_Books.csv.txt",
			"Hobbies_Collectibles_Books.csv.txt",
			"Movies_TV.csv.txt",
			"Music_Radio_Books.csv.txt",
			"Nostalgia_Eclectic_Books.csv.txt",
			"Old_Time_Radio.csv.txt",
			"Sports_Sports_Memorabilia.csv.txt",
			"Trains_Planes_Automobiles.csv.txt"	
		};
		
		/**
		 * Initialize
		 */
		Scanner sc = null;
		String[] bookValues = null;
		
		for (String textFile : validSyntaxBookFiles) {
            try {
            	sc = new Scanner(new FileInputStream(textFile));
            	int bookIndex = 0;
            	Book[] books = new Book[3000];
            	
            	while(sc.hasNextLine()) {
            		String currentBook = sc.nextLine();
            		
            		/**
            		 * This ignores the commas within the double quote marks using positive lookahead 
            		 */
            		bookValues = currentBook.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); 
            		String bookTitle = bookValues[0];
            		String bookAuthors = bookValues[1];
            		double bookPrice = Double.parseDouble(bookValues[2]);
            		String bookIsbn = bookValues[3];
            		String bookGenre = bookValues[4];
            		int bookYear = Integer.parseInt(bookValues[5]);
            		
            		try 
            		{
            			PriceValidation(bookPrice);
            			YearValidation(bookYear);
            			
            			try
            			{
            				IsbnValidation(bookIsbn);
            			}
            			catch (BadIsbnException e)
            			{
            				writeSemanticErrors(textFile, e.getMessage(), currentBook);
            				continue;
            			}
            			books = addTheBookToArray(books, new Book(bookTitle, bookAuthors, bookPrice, bookIsbn, bookGenre, bookYear), bookCounter);
            			bookCounter++;
            		}
            		catch (BadPriceException | BadYearException | BadIsbn10Exception | BadIsbn13Exception e)
            		{
        				writeSemanticErrors(textFile, e.getMessage(), currentBook);
            		}	
            	}
            	books = Arrays.copyOf(books, bookCounter);
            	bookSerialization(books, textFile + ".ser");
            }
            catch(FileNotFoundException e)
            {
            	System.out.println("Error opening this file:" + textFile);
            }    
		}
	}
	
	/**
	 * Main Method
	 * @param args
	 */
	public static void main(String[] args) {
		doPart1(); // validating syntax, partition book records based on genre.
		doPart2(); // validating semantics, read the genre files each into arrays of book objects,
	               // then serialize the arrays of Book objects each into binary files.
		
		System.out.println("\nThanks for using!");
	}
}
