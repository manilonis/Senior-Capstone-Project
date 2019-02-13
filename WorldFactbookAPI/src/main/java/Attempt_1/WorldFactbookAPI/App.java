package Attempt_1.WorldFactbookAPI;

import java.io.File;
import java.io.IOException;

import org.jsoup.*;
import org.jsoup.nodes.Document;
/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args){
    	File f = new File("/home/maniloni/Senior Project/World Factbook Data/2000/wfbfull/factbook/geos/aa.html");
    	try {
			Document d = Jsoup.parse(f, "UTF-8", "aa");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println( "Hello World!" );
    }
}
