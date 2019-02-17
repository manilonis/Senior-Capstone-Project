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
			Elements e = d.select("tr");
			Elements div = d.select("div");
			
			String[] divs = div.html().split("\n");
			ArrayList<String> topics = new ArrayList<String>();
			for (String s: divs) {
				if(s.contains("<") || s.contains("nbsp")) continue;
				else topics.add(s.trim());
			}
			String[] breaks = e.html().split("</td>");
			ArrayList<String> data = new ArrayList<String>();
			for (String str: breaks) {
				int c = str.lastIndexOf("</a>");
				if (c>=0) {
					String temp = str.substring(c+4);
					if (temp.contains("</font>") || temp.contains("</div>")) continue;
					if(temp.lastIndexOf("<br>") >=0 ) data.add(temp.substring(temp.lastIndexOf("<br>")+4).trim());
					else data.add(str.substring(c+4).trim());
				}
			}
			//System.out.println(data.toString());
			//System.out.println(topics.toString());
			
			System.out.println("Sizes: " + topics.size() + "   " + data.size());
			
			
		}catch(IOException ie) {
			
		}
	}
}
