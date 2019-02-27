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
					whole.put(f.getName(), parseCountry(d));
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
					if (data.contains(temp.substring(temp.lastIndexOf(("<br>") + 4)).trim())) continue;
					data.add(temp.substring(temp.lastIndexOf("<br>") + 4).trim());
				}
				else {
					if (data.contains(str.substring(c + 4).trim())) continue;
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
			if(i >= data.size()) System.out.println("Extra Header: " + topics.get(i));
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
