/**
 * Michael E Anilonis
 * Mar 29, 2019
 */
package parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class NewParser {
	public static void parse() {
		String file_location = "/home/maniloni/Senior Project/World Factbook Data/2007/factbook/geos/aa.html";
		File f = new File(file_location);
		try {
			Document d = Jsoup.parse(f, "UTF-8", f.getName());
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
			//Pattern p = Pattern.compile("^[a-zA-Z0-9]*$");
			for(String t: texts) {
				t = t.trim();
				if(t.equals("Aruba") || t.length() <= 1) continue;
				if(t.matches("^[a-zA-Z0-9]*$")) continue;
				texts_trimmed.add(t);
			}
			System.out.println(texts_trimmed);
			System.out.println(texts_trimmed.get(texts_trimmed.size()-7).length());
			System.out.println((int)(texts_trimmed.get(texts_trimmed.size()-7).toCharArray()[0]));
			System.out.println((int)(texts_trimmed.get(texts_trimmed.size()-7).toCharArray()[1]));
			//System.out.println(Arrays.toString(text.split("\n")));
			char[] text_array = text.toCharArray();
			ArrayList<Integer> indicies = new ArrayList<Integer>();
			for(int i =0; i< text_array.length; i++) {
				if(text_array[i] == ':') indicies.add((Integer)i);
			}
			//System.out.println(indicies);
			ArrayList<String> lines = new ArrayList<String>();
			int lastInd = -1;
			for(Integer i: indicies) {
				int ind = i.intValue();
				int titleIndex = indexOfTitle(text_array, ind);
				if(lastInd >= 0) lines.add(text.substring(lastInd+1, titleIndex));
				//System.out.println("Colon idex: " + ind + " Title index: " + titleIndex);
				//System.out.println("Substring title is: " + text.substring(titleIndex, ind));
				lines.add(text.substring(titleIndex, ind));
				lastInd = ind;
			}
			//System.out.println(lines);
			//System.out.println(d.text());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static int indexOfTitle(char[] array, int i) {
		boolean dashLastFound = false;
		for(int q=i; q>=0 && q<array.length; q--) {
			if(array[q] == ' ') {
				if (dashLastFound) {
					dashLastFound = false;
					continue;
				}
				if(q>0) {
					if(array[q-1] == '-') {
						dashLastFound = true;
						continue;
					}
					else {
						return q+1;
					}
				}
				else return -1;
			}
		}
		return 0;
	}
}
