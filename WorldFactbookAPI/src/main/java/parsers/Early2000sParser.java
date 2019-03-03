/**
 * Michael E Anilonis
 * Feb 21, 2019
 */
package parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Early2000sParser {
	public static void parse(String year, String htmlFolderPath) {
		boolean filesLoaded = true;

		HashMap<String, HashMap<String, String>> whole = null;

		try {
			FileInputStream filein = new FileInputStream("/home/maniloni/Senior Project/serialized_data/"+year+".ser");
			ObjectInputStream oin = new ObjectInputStream(filein);

			whole = (HashMap<String, HashMap<String, String>>) oin.readObject();
			filein.close();
			oin.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			filesLoaded = false;
		} catch (IOException e) {
			e.printStackTrace();
			filesLoaded = false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("ERROR EXITING");
			System.exit(0);
		}
		if (!filesLoaded) {
			File folder = new File(htmlFolderPath);
			File[] files = folder.listFiles();

			whole = new HashMap<String, HashMap<String, String>>();

			for (File f : files) {
				try {
					if (!f.getName().endsWith(".html"))
						continue;
					if (f.getName().equals("index.html") || f.getName().equals("ee.html"))
						continue;
					Document d = Jsoup.parse(f, "UTF-8", f.getName());
					if(d.baseUri().equals("aa.html")) parseTables(d);
					else whole.put(f.getName(), parseCountry(d));
					System.out.println("Done " + f.getName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println(whole.size());

			try {
				FileOutputStream fileout = new FileOutputStream(
						"/home/maniloni/Senior Project/serialized_data/"+year+".ser");
				ObjectOutputStream out = new ObjectOutputStream(fileout);

				out.writeObject(whole);

				fileout.close();
				out.close();
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
	}
	
	private static void parseTables(Document d) {
		Elements table = d.select("table");
		/*for(Element e :table) {
			Elements rows = e.select("tr");
			for(int q=1; q<rows.size(); q++) {
				Element row= rows.get(q);
				System.out.println("Row " + q + " " + row.html());
				Elements cols = row.select("td");
				for(Element ee: cols) {
					System.out.println("Column " + ee.html());
				}
			}
		}*/
		
		int tableCount = 0;
		ArrayList<String> headers = new ArrayList<String>();
		ArrayList<String> data = new ArrayList<String>();
		for(Element e: table) {
			System.out.println("Table count is " + tableCount + "\n\n");
			Elements cols = e.select("td");
			int actualCount = 0;
			for(int q=0; q<cols.size(); q++) {
				Element col = cols.get(q);
				if(actualCount%2 == 0) {
				if(!cols.html().contains("&nbsp")) {
					headers.add(cols.html());
					actualCount++;
				}
				}
				else {
					data.add(cols.html());
					actualCount++;
				}
				System.out.println("Column " +q + " " + col.html());
			}
			tableCount++;
		}
		System.out.println("Size of new lists: headers: " + headers.size()+" data: " + data.size());
		for(int i =0; i< headers.size(); i++) {
			System.out.println("Header: " + headers.get(i) + " data: "+ data.get(i));
		}
	
	}
	
	private static HashMap<String, String> parseCountry(Document d) {
		Elements e = d.select("tr");
		Elements div = d.select("div");

		String[] divs = div.html().split("\n");
		ArrayList<String> topics = new ArrayList<String>();
		for (String s : divs) {
			if (s.contains("<") || s.contains("nbsp"))
				continue;
			else
				topics.add(s.trim());
		}
		String[] breaks = e.html().split("</td>");
		ArrayList<String> data = new ArrayList<String>();
		for (String str : breaks) {
			int c = str.lastIndexOf("</a>");
			if (c >= 0) {
				String temp = str.substring(c + 4);
				if (temp.contains("</font>") || temp.contains("</div>") || temp.contains("</p>") || temp.contains("</span>"))
					continue;
				if (temp.lastIndexOf("<br>") >= 0) {
					data.add(temp.substring(temp.lastIndexOf("<br>") + 4).trim());
				}
				else {
					data.add(str.substring(c + 4).trim());
				}
			}
			if(d.baseUri().equals("aa.html")) {
				System.out.println(str);
			}
		}
		while(data.contains("")) data.remove("");
		HashMap<String, String> allData = new HashMap<String, String>();

		System.out.println("Sizes: " + topics.size() + "   " + data.size());
		if (topics.size() != data.size()) {
			System.out.println("Error at " + d.baseUri());
			//System.out.println(topics.toString());
			//System.out.println(data.toString());
		}
		for (int i = 0; i < topics.size(); i++) {
			//System.out.println("Header: " + topics.get(i) + "Body: " + data.get(i));
			if(i >= data.size()) {
				//System.out.println("Extra Header: " + topics.get(i));
				continue;
			}
			else allData.put(topics.get(i), data.get(i));
		}
		if(topics.size() != data.size() && d.baseUri().equals("aa.html")) {
			System.out.println(allData.toString());
			//System.out.println(topics.get(0));
			//System.out.println(allData.get(topics.get(0)));
			///System.out.println(topics.toString());
			///System.out.println(data.toString());
			for(int k=0; k<data.size(); k++) {
				if(data.get(k).length() == 0) System.out.println(k);
				System.out.println(k+": "+data.get(k));
			}
		}
		return allData;
	}
}
