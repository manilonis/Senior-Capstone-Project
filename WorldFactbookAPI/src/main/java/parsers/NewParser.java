/**
 * Michael E Anilonis
 * Mar 29, 2019
 */
package parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
			text = text.substring(c);
			text = text.replaceAll("Aruba Top of Page", "");
			System.out.println(text);
			char[] text_array = text.toCharArray();
			ArrayList<Integer> indicies = new ArrayList<Integer>();
			for(int i =0; i< text_array.length; i++) {
				if(text_array[i] == ':') indicies.add((Integer)i);
			}
			System.out.println(indicies);
			for(Integer i: indicies) {
				int ind = i.intValue();
				System.out.println("Colon idex: " + ind + " Title index: " + indexOfTitle(text_array, ind));
			}
			//System.out.println(d.text());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static int indexOfTitle(char[] array, int i) {
		for(int q=i; q>=0 && q<array.length; q--) {
			if(array[q] == ' ') {
				if(q>0) {
					if(array[q-1] == '-') {
						continue;
					}
					else {
						return q+1;
					}
				}
				else return -1;
			}
		}
		return -1;
	}
}
