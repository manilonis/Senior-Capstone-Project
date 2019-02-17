/**
 * Michael E Anilonis
 * Feb 17, 2019
 */
package parsers;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HTMLParser2002 {
	public static void parse() {
		File f = new File("/home/maniloni/Senior Project/World Factbook Data/2002/geos/aa.html");
		
		try {
			Document d = Jsoup.parse(f, "UTF-8", "aa");
			Elements e = d.select("td");
			System.out.println(e.html());
			
		}catch(IOException ie) {
			
		}
	}
}
