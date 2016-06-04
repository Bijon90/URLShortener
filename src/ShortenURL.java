/**
 * @author Bijon
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Constructor for ShortenURL to initialize charSet for generating short urls
 *
 */
public class ShortenURL {

	private char charSet[];
	private String domain;

	public ShortenURL() {
		domain = "";
		charSet = new char[62];
		for (int i = 0; i < 62; i++) {
			int j = 0;
			if (i < 10) {
				j = i + 48;
			} else if (i > 9 && i <= 35) {
				j = i + 55;
			} else {
				j = i + 61;
			}
			charSet[i] = (char) j;
		}
	}

	/**
	 * Constructor with domain name to add to the short URL
	 * @param newDomain
	 */
	public ShortenURL(String newDomain){
		this();
		if (!newDomain.isEmpty()) {
			domain = newDomain;
		}

	}

	/**
	 * Gets index of a character in charSet and used for decoding shortened URL
	 * @param c
	 * @return of character of short URL in charSet 
	 */
	public int getIndex(char c){
		int indx = 0;
		for (int i = 0; i < charSet.length; i++) {
			if(charSet[i] == c){
				indx = i;
				break;
			}
		}
		return indx;
	}

	/**
	 * Method to generate absolute hash value for long urls enterd
	 * @param longURL
	 * @return absolute hash value for long urls
	 */
	public static int generateId(String longURL){
		return Math.abs(longURL.hashCode());
	}

	/**
	 * Encodes long urls to short version utilizing the generated id
	 * @param id
	 * @param longURL
	 * @return shortened url
	 */
	public String encodeURL(int id, String longURL){
		String shortURL = "";
		int temp = id;
		int index = 0;
		while(temp != 0){
			index = temp % 62;
			shortURL = charSet[index] + shortURL;
			temp = temp / 62;  
		}
		return (domain+shortURL);
	}

	/**
	 * Decodes shortened url and generates the id
	 * @param shortURL
	 * @return id generated from shortened url
	 */
	public int decodeURL(String shortURL){
		String longURL ="";
		int id =0;
		char c;
		int indx;
		int p = 0; 
		String key ="";
		if(shortURL.length()>domain.length() && shortURL.contains(domain))
			shortURL = shortURL.substring(domain.length());

		for (int i = shortURL.length() -1; i >= 0; i--) {
			c = shortURL.charAt(i);
			indx = getIndex(c);
			id += indx * Math.pow(62, p++);
		}
		return id;
	}

	/**
	 * inserts the record with generated id, shorturl and longurl into the database for futue use
	 * @param id
	 * @param longURL
	 * @param shortURL
	 */
	public void insertToDB(int id, String longURL, String shortURL){
		CreateConnectionMySQL dbConn = new CreateConnectionMySQL();
		PreparedStatement preparedStatement = null;
		Connection con = dbConn.createConn();

		try {
			preparedStatement = con.prepareStatement("insert into  url.url values (?, ?, ?)");
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, longURL);
			preparedStatement.setString(3, shortURL);
			preparedStatement.executeUpdate();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves expanded long url from the databse when the shortened url is given as input
	 * @param shortURL
	 * @return expanded long url
	 */
	public String retrieveFromDB(String shortURL){
		CreateConnectionMySQL dbConn = new CreateConnectionMySQL();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = dbConn.createConn();
		int id = decodeURL(shortURL);
		String longurl = "";

		try {
			preparedStatement = con.prepareStatement("SELECT longurl from url.url where id = ?");
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				longurl = rs.getString("LONGURL");
			}
			System.out.println("LongURL: "+longurl);
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return longurl;
	}

	/**
	 * Checks database if the url record already exists in database. 
	 * @param id
	 * @return null if record does not exist in database otherwise return already existing short url from database
	 */
	public String urlExists(int id){
		CreateConnectionMySQL dbConn = new CreateConnectionMySQL();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = dbConn.createConn();
		String shorturl = "";

		try {
			preparedStatement = con.prepareStatement("SELECT shorturl from url.url where id = ?");
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			if(!rs.equals(null)){
				while (rs.next()) {
					shorturl = rs.getString("SHORTURL");
				}
			}
			//System.out.println("LongURL: "+shorturl);
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return shorturl;
	}

	/**
	 * Main method that provides the interface for the application. User can either opt to shorten an url or 
	 * retrieve original longer one from database.
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String cont = "Y";
		do{
			System.out.println("Do you want to shorten a URL or expand a short URL?");
			String choice = "";
			do{
				System.out.println("Enter shorten/expand?");
				choice = sc.nextLine().trim();
				if(!choice.equalsIgnoreCase("shorten") && !choice.equalsIgnoreCase("expand")){
					System.out.println("Please enter shorten or expand!");
				}
			}while(!choice.equalsIgnoreCase("shorten") && !choice.equalsIgnoreCase("expand"));
			System.out.println("Entered choice: "+choice);
			String longurl = "";
			String shorturl = "";
			int id = 0;
			ShortenURL surl = new ShortenURL("www.bitly.com/");

			if(choice.equalsIgnoreCase("shorten")){
				System.out.println("Enter URL to shorten: ");
				longurl = sc.nextLine().trim();
				id = generateId(longurl);
				shorturl = surl.urlExists(id);
				if(shorturl.equals(null) || shorturl.equals("")){
					shorturl = surl.encodeURL(id, longurl);
					surl.insertToDB(id, longurl, shorturl);
					System.out.println("Record inserted to Database!");
				}
				System.out.println("Id: "+ id + " LongURL: "+longurl +" Shortened URL: "+shorturl);

			}
			else if(choice.equalsIgnoreCase("expand")){
				System.out.println("Enter URL to expand: ");
				shorturl = sc.nextLine().trim();
				id = surl.decodeURL(shorturl);
				longurl = surl.retrieveFromDB(shorturl);
				if(longurl.equals(null) || longurl.equals(""))
					System.out.println("URL does not exist in database! Please try another.");
				else
					System.out.println("ShortURL: "+shorturl+" Id: "+id +" Expanded URL: "+longurl);
			}
			System.out.println("Do you want to continue(Y/N): ");
			cont = sc.nextLine().trim();
		}while(cont.equalsIgnoreCase("Y"));
		System.out.println("Exit!");
		sc.close();
	}

}
