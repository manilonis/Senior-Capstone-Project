package parser2000;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import generalHelpers.Tuple;

public class HTMLParser2000 {
	public static void parse() {
		File folder = new File("/home/maniloni/Senior Project/World Factbook Data/2000/wfbfull/factbook/geos/");
		File[] files = folder.listFiles();
		// File f = new File("/home/maniloni/Senior Project/World Factbook
		// Data/2000/wfbfull/factbook/geos/aa.html");

		for (File f : files) {
			try {
				HashMap<String, String> data;
				System.out.println(f.getName());
				if(!f.getName().endsWith(".html")) continue;
				Document d = Jsoup.parse(f, "UTF-8", f.getName());
				Elements ee = d.select("p");
				Elements top = d.select("b");
				data = grabInfo(top, ee);
				if(f.getName().contains("fg")) {
					System.out.println(data.toString());
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static HashMap<String, String> grabInfo(Elements topics, Elements all) {
		String allHTML = all.html();
		String[] allFirstSplit = allHTML.split("</b>");
		ArrayList<String> alls = new ArrayList<String>();
		for (String s : allFirstSplit) {
			if (s.equals(allFirstSplit[allFirstSplit.length - 1])) {
				alls.add(s);
				continue;
			}
			int c = s.indexOf("<b>");
			if (c >= 0) {
				if (c == 0)
					alls.add(s.substring(3));
				else
					alls.add(s.substring(0, c));
			}
		}
		String[] topic_array = topics.html().split("\n");

		int arrayListCount = 1;
		HashMap<String, String> allData = new HashMap<String, String>();
		for (int i = 0; i < topic_array.length; i++) {
			if (topic_array[i].contains("</a>") || (topic_array[i]).contains("</font>")) {
				continue;
			}
			String a;
			try {
				a = alls.get(arrayListCount);
				arrayListCount++;
			} catch (IndexOutOfBoundsException ae) {
				a = null;
			}

			if (a != null) {
				allData.put(topic_array[i], a);
			} else
				System.out.println("Extra Header: " + topic_array[i]);
		}
		System.out.println(allData.size());

		return allData;
	}

	private static int[] range(int start, int stop) {
		int[] result = new int[stop - start];

		for (int i = 1; i < stop - start; i++)
			result[i] = start + i;

		return result;
	}
}
