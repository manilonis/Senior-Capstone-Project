/**
 * Michael E Anilonis
 * Mar 29, 2019
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

public class NewParser {
	@SuppressWarnings("unchecked")
	public static void parse(String year, String html_directory) {
		HashMap<String, HashMap<String, String>> allData;
		try {
			FileInputStream fin = new FileInputStream("/home/maniloni/Senior Project/serialized_data/"+year+".ser");
			ObjectInputStream oin = new ObjectInputStream(fin);
			
			allData = (HashMap<String, HashMap<String, String>>) oin.readObject();
			fin.close();
			oin.close();
			return;
		}
		catch (FileNotFoundException e1) {
		e1.printStackTrace();
		} catch (IOException e) {
		e.printStackTrace();
		} catch (ClassNotFoundException e) {
		e.printStackTrace();
		System.out.println("ERROR EXITING");
		System.exit(0);
		}
		
		File folder = new File(html_directory);
		File[] files = folder.listFiles();
		allData = new HashMap<String, HashMap<String, String>>();
		
		for(File f: files) {
			try {
				if (!f.getName().endsWith(".html"))
					continue;
				if (f.getName().equals("index.html") || f.getName().equals("ee.html"))
					continue;
				Document d = Jsoup.parse(f, "UTF-8", f.getName());
				if(d.title().contains("Redirect")) continue;
				allData.put(f.getName(), parseFile(d));
			} catch (IOException e) {
				e.printStackTrace();
			}
			catch(Exception e) {
				e.printStackTrace();
				System.out.println("ERROR at " + f.getName());
				System.exit(1);
			}
		}
		
		FileOutputStream fout;
		try {
			fout = new FileOutputStream("/home/maniloni/Senior Project/serialized_data/"+year+".ser");
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			
			oout.writeObject(allData);
			System.out.println(year + "serialized");
			fout.close();
			oout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
		System.out.println(r.size());
		return r;
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
