package Attempt_1.WorldFactbookAPI;

import parsers.Early2000sParser;
import parsers.HTMLParser2000;
import parsers.NewParser;

public class App {
	public static void main(String[] args) {
		System.out.println("2000 going");
		HTMLParser2000.parse();
		System.out.println("2002 going");
		Early2000sParser.parse("2002", "/home/maniloni/Senior Project/World Factbook Data/2002/geos/");
		System.out.println("2003 going");
		Early2000sParser.parse("2003", "/home/maniloni/Senior Project/World Factbook Data/2003/geos/");
		System.out.println("2004 going");
		Early2000sParser.parse("2004", "/home/maniloni/Senior Project/World Factbook Data/2004/factbook/geos/");
		System.out.println("2005 going");
		Early2000sParser.parse("2005", "/home/maniloni/Senior Project/World Factbook Data/2005/factbook/geos/");
		System.out.println("2006 going");
		Early2000sParser.parse("2006", "/home/maniloni/Senior Project/World Factbook Data/2006/factbook/geos/");
		System.out.println("2007 going");
		NewParser.parse("2007", "/home/maniloni/Senior Project/World Factbook Data/2007/factbook/geos/");
		System.out.println("2008 going");
		NewParser.parse("2008", "/home/maniloni/Senior Project/World Factbook Data/2008/factbook/geos/");
		System.out.println("2009 going");
		NewParser.parse("2009", "/home/maniloni/Senior Project/World Factbook Data/2009/geos/");
		System.out.println("2010 going");
		NewParser.parse("2010", "/home/maniloni/Senior Project/World Factbook Data/2010/factbook/geos/");
		System.out.println("2011 going");
		NewParser.parse("2011", "/home/maniloni/Senior Project/World Factbook Data/2011/factbook/geos/");
		System.out.println("2012 going");
		NewParser.parse("2012", "/home/maniloni/Senior Project/World Factbook Data/2012/factbook/geos/");
		System.out.println("2013 going");
		NewParser.parse("2013", "/home/maniloni/Senior Project/World Factbook Data/2013/factbook/geos/");
		System.out.println("2014 going");
		NewParser.testMethodforNewYear("2014", "/home/maniloni/Senior Project/World Factbook Data/2014/factbook/geos/");

	}
}
