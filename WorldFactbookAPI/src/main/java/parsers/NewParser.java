/**
 * Michael E Anilonis
 * Mar 29, 2019
 */
package parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class NewParser {
	public static void parse() {
		String file_location = "/home/maniloni/Senior Project/World Factbook Data/2007/factbook/geos/aa.html";
		File f = new File(file_location);
		try {
			Document d = Jsoup.parse(f, "UTF-8", f.getName());
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static HashMap<String, String> parseFile(Document d) {
		HashMap<String, String> r = new HashMap<String, String>();
		d.outputSettings(new Document.OutputSettings().prettyPrint(false));
		d.select("br").append("\\n");
	    d.select("p").prepend("\\n\\n");
	    d.select("td").prepend("\\n\\n");
		String text = d.text().replaceAll("\\\\n", "\n").replaceAll("(?m)^[ \t]*\r?\n", "");
		int c = text.indexOf("Background:");
		text = text.substring(c);
		text = text.replaceAll("Top of Page", "");
		String[] texts = text.split("\n");
		ArrayList<String> texts_trimmed = new ArrayList<String>();
		for(String t: texts) {
			t = t.trim();
			if(t.contains("This page was last updated") || t.length() <= 1) continue;
			if(onlySpaces(t)) continue;
			texts_trimmed.add(t);
		}
		ArrayList<String> tempData = new ArrayList<String>();
		String header = null;
		for(String s: texts_trimmed) {
			c = s.indexOf(':');
			if(c == s.length()-1) {
				if(header != null) {
					r.put(header, commaSeperateList(tempData));
				}
				header = s;
				tempData.clear();
			}
			else tempData.add(s);
		}
		System.out.println(r.toString());
	}
	
	private static String commaSeperateList (ArrayList<String> data) {
		String r = new String();
		for(String str: data) {
			if(str.equals(data.get(data.size()-1))) r += str;
			else r += str + ", ";
		}
		return r;
	}
	private static boolean onlySpaces(String s) {
		if(s.toCharArray()[0] == 160) return true;
		return false;
	}
	
}
