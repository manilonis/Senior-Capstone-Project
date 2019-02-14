package Attempt_1.WorldFactbookAPI;

import java.io.File;
import java.io.IOException;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args){
    	File f = new File("/home/maniloni/Senior Project/World Factbook Data/2000/wfbfull/factbook/geos/aa.html");
    	try {
			Document d = Jsoup.parse(f, "UTF-8", "aa");
			Elements ee = d.select("p");
			//System.out.println(ee.html());
			String p = ee.html();
			String[] lines = p.split("\n");
			System.out.println(lines.length);
			System.out.println(lines[0]);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        System.out.println( "Hello World!" );
    }
}
