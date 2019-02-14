package parser2000;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import generalHelpers.Tuple;

public class HTMLParser2000 {
	public static void parse() {
		File f = new File("/home/maniloni/Senior Project/World Factbook Data/2000/wfbfull/factbook/geos/aa.html");
    	try {
			Document d = Jsoup.parse(f, "UTF-8", "aa");
			Elements ee = d.select("p");
			//System.out.println(ee.html());
			String p = ee.html();
			String[] lines = p.split("\n");
			//System.out.println(lines.length);
			//System.out.println(lines[0]);
			
			ArrayList<Tuple<String, Integer>> titles = new ArrayList<Tuple<String, Integer>>();
			
			for(int i=0; i<lines.length; i++) {
				String s = lines[i];
				if (s.indexOf("</b>") >= 0){
					titles.add(new Tuple<String, Integer>(s, i));
				}
				else continue;
			}
			
			System.out.println(titles.size());
			System.out.println(titles.get(1));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
