/**
 * Michael E Anilonis
 * Mar 29, 2019
 */
package parsers;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class NewParser {
	public static void parse() {
		String file_location = "/home/maniloni/Senior Project/World Factbook Data/2007/factbook/geos/aa.html";
		File f = new File(file_location);
		try {
			Document d = Jsoup.parse(f, "UTF-8", f.getName());
			String text = d.text();
			int c = text.indexOf("Background:");
			System.out.println(text.substring(c));
			//System.out.println(d.text());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
