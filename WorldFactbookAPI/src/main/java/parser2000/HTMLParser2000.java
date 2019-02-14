package parser2000;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
			
			//ArrayList<Tuple<String, String>> info = new ArrayList<Tuple<String, String>>();
			HashMap<String, String> info = new HashMap<String, String>();
			
			
			for(int i =0; i<titles.size(); i++) {
				Tuple<String, Integer> t = titles.get(i);
				String stuff = t.getX();
				int b = stuff.indexOf("<b>");
				int e = stuff.indexOf("</b>");
				String topic = stuff.substring(b+3, e-1);
				
				String stuff1 = stuff.substring(e+4);
				//System.out.println(topic);
				
				
				int thisIndex = t.getY();
				if (i+1 < titles.size() && (thisIndex+1 != titles.get(i+1).getY())) {
					int[] indecies = range(thisIndex, titles.get(i+1).getY());
					for (int in: indecies) {
						stuff1 += lines[in];
					}
				}
				else {
					if (i+1 < lines.length) {
						int[] indecies = range(thisIndex, lines.length-1);
						for (int in: indecies) {
							stuff1 += lines[in];
						}
					}
				}
				
				info.put(stuff, stuff1);
			}
			
			System.out.println(info.get("Economy"));
			
			//System.out.println(titles.size());
			//System.out.println(titles.get(1));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static int[] range(int start, int stop){
	   int[] result = new int[stop-start];

	   for(int i=1;i<stop-start;i++)
	      result[i] = start+i;

	   return result;
	}
}
