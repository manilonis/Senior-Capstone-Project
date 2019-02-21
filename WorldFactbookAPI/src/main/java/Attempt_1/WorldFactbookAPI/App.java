package Attempt_1.WorldFactbookAPI;

import parsers.Early2000sParser;
import parsers.HTMLParser2000;
import parsers.HTMLParser2002;

public class App {
    public static void main(String[] args){
    	HTMLParser2000.parse();
    	Early2000sParser.parse("2002", "/home/maniloni/Senior Project/World Factbook Data/2002/geos/");
    	Early2000sParser.parse("2003", "/home/maniloni/Senior Project/World Factbook Data/2003/geos/");
    	Early2000sParser.parse("2004", "/home/maniloni/Senior Project/World Factbook Data/2004/factbook/geos/");
    	Early2000sParser.parse("2005", "/home/maniloni/Senior Project/World Factbook Data/2005/factbook/geos/");
    	Early2000sParser.parse("2006", "/home/maniloni/Senior Project/World Factbook Data/2006/factbook/geos/");
    	//HTMLParser2002.parse();
        System.out.println("Completed");
    }
}
