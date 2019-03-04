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
			FileInputStream filein = new FileInputStream(
					"/home/maniloni/Senior Project/serialized_data/" + year + ".ser");
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
					whole.put(f.getName(), parseTables(d));
					System.out.println("Done " + f.getName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println(whole.size());

			try {
				FileOutputStream fileout = new FileOutputStream(
						"/home/maniloni/Senior Project/serialized_data/" + year + ".ser");
				ObjectOutputStream out = new ObjectOutputStream(fileout);

				out.writeObject(whole);

				fileout.close();
				out.close();
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
	}

	private static HashMap<String, String> parseTables(Document d) {
		Elements table = d.select("table");
		int tableCount = 0;
		ArrayList<String> headers = new ArrayList<String>();
		ArrayList<String> data = new ArrayList<String>();
		for (Element e : table) {
			Elements rows = e.select("tr");
			for (Element r : rows) {
				Elements cols = r.select("td");
				int actualCount = 0;
				for (int q = 0; q < cols.size(); q++) {
					Element col = cols.get(q);
					if (col.html().contains("&nbsp") || col.html().contains("Introduction")
							|| col.html().contains("name=\"Geo\"") || col.html().contains("name=\"People\"")
							|| col.html().contains("name=\"Govt\"") || col.html().contains("name=\"Econ\"")
							|| col.html().contains("Communications") || col.html().contains("Transportation")
							|| col.html().contains("Transnational Issues") || col.html().contains("class=\"Defintion\"")
							|| col.html().contains("Top of Page"))
						continue;
					if (actualCount % 2 == 0) {
						if (col.html().contains("<img"))
							continue;
						if (headers.contains(col.text())) {
							continue;
						}
						headers.add(col.text());
						actualCount++;
					} else {
						data.add(col.text());
						actualCount++;
					}
				}
			}
			tableCount++;
		}
		HashMap<String, String> allData = new HashMap<String, String>();
		if (data.size() != headers.size())
			System.out.println(
					"SIZE DIFFERENCE AT " + d.baseUri() + "Headers: " + headers.size() + " data: " + data.size());
		for (int i = 0; i < data.size(); i++) {
			allData.put(headers.get(i), data.get(i));
		}

		return allData;
	}
}
