/**
 * Michael E Anilonis
 * Feb 17, 2019
 */
package parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HTMLParser2002 {
	public static void parse() {
		File folder = new File("/home/maniloni/Senior Project/World Factbook Data/2002/geos/");
		File[] files = folder.listFiles();
		
		HashMap<String, HashMap<String, String>> whole2002 = new HashMap<String, HashMap<String, String>>();
		
		for(File f: files) {
			try {
				if (!f.getName().endsWith(".html")) continue;
				Document d = Jsoup.parse(f, "UTF-8", f.getName());
				whole2002.put(f.getName(), parseCountry(d));
				System.out.println("Done " + f.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(whole2002.size());
		
		
	}
	
	private static HashMap<String, String> parseCountry(Document d) {
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
				if (temp.contains("</font>") || temp.contains("</div>") || temp.contains("</p>")) continue;
				if(temp.lastIndexOf("<br>") >=0 ) data.add(temp.substring(temp.lastIndexOf("<br>")+4).trim());
				else data.add(str.substring(c+4).trim());
			}
		}
		HashMap<String, String> allData = new HashMap<String, String>();
		
		System.out.println("Sizes: " + topics.size() + "   " + data.size());
		if(topics.size()!= data.size()) System.out.println("Error at " + d.baseUri());
		for(int i=0; i<topics.size(); i++) {
			System.out.println("Header: " + topics.get(i) + "Body: "+ data.get(i));
			allData.put(topics.get(i), data.get(i));
		}
		return allData;
	}
}
