package parser2000;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HTMLParser2000 {
	public static void parse() {
		boolean filegot = true;
		HashMap<String, HashMap<String, String>> countries = null;
		try {
			FileInputStream fin = new FileInputStream("/home/maniloni/Senior Project/serialized_data/2000.ser");
			ObjectInputStream oin = new ObjectInputStream(fin);
			
			countries = (HashMap<String, HashMap<String, String>>) oin.readObject();
		}catch(IOException io) {
			io.printStackTrace();
			filegot = false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("ERROR");
			System.exit(0);
		}
		if (!filegot) {
		System.out.println("Grabbing Files");
		File folder = new File("/home/maniloni/Senior Project/World Factbook Data/2000/wfbfull/factbook/geos/");
		File[] files = folder.listFiles();
		countries = new HashMap<String, HashMap<String, String>>();

		for (File f : files) {
			try {
				HashMap<String, String> data;
				System.out.println(f.getName());
				if(!f.getName().endsWith(".html")) continue;
				Document d = Jsoup.parse(f, "UTF-8", f.getName());
				Elements ee = d.select("p");
				Elements top = d.select("b");
				data = grabInfo(top, ee);
				countries.put(f.getName(), data);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileOutputStream fileout = new FileOutputStream("/home/maniloni/Senior Project/serialized_data/2000.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileout);
			out.writeObject(countries);
			
			fileout.close();
			out.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		else {
			System.out.println(countries.size());
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

		return allData;
	}
}
