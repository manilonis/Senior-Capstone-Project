package Attempt_1.WorldFactbookAPI;

import parsers.Early2000sParser;
import parsers.HTMLParser2000;
import parsers.HTMLParser2002;

public class App {
    public static void main(String[] args){
    	//HTMLParser2000.parse();
    	//System.out.println("2002 going");
    	//Early2000sParser.parse("2002", "/home/maniloni/Senior Project/World Factbook Data/2002/geos/");
    	System.out.println("2003 going");
    	Early2000sParser.parse("2003", "/home/maniloni/Senior Project/World Factbook Data/2003/geos/");
    	System.out.println("2004 going");
    	Early2000sParser.parse("2004", "/home/maniloni/Senior Project/World Factbook Data/2004/factbook/geos/");
    	System.out.println("2005 going");
    	Early2000sParser.parse("2005", "/home/maniloni/Senior Project/World Factbook Data/2005/factbook/geos/");
    	System.out.println("2006 going");
    	Early2000sParser.parse("2006", "/home/maniloni/Senior Project/World Factbook Data/2006/factbook/geos/");
        System.out.println("Completed");
    }
}
