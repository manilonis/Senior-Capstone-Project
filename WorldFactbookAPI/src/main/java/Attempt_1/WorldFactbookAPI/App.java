package Attempt_1.WorldFactbookAPI;

import parsers.HTMLParser2000;
import parsers.HTMLParser2002;

public class App {
    public static void main(String[] args){
    	HTMLParser2000.parse();
    	HTMLParser2002.parse();
        System.out.println("Completed");
    }
}
