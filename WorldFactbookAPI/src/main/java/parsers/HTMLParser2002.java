/**
 * Michael E Anilonis
 * Feb 17, 2019
 */
package parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HTMLParser2002 {
	public static void parse() {
		File f = new File("/home/maniloni/Senior Project/World Factbook Data/2002/geos/aa.html");
		
		try {
			Document d = Jsoup.parse(f, "UTF-8", "aa");
			Elements e = d.select("td");
			Elements div = d.select("div");
			
			String[] divs = div.html().split("\n");
			ArrayList<String> topics = new ArrayList<String>();
			for (String s: divs) {
				if(s.contains("<")) continue;
				else topics.add(s.trim());
			}
			String[] breaks = e.html().split("<br>");
			System.out.println(Arrays.toString(breaks));
			System.out.println(topics.toString());
			
		}catch(IOException ie) {
			
		}
	}
}
