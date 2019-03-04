/**
 * Michael E Anilonis
 * Mar 4, 2019
 */
package api_backend;

import static spark.Spark.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class MyAPI {
	public static void main(String[] args) {
		String[] years = {"2000", "2002" , "2003", "2004" , "2005" , "2006"};
		HashMap<String, HashMap<String, String>>[] maps = new HashMap [years.length];
		for(int i=0; i<years.length; i++) {
			maps[i] = loadFile(years[i]);
		}
		get("/hello", (req, res)->"Hello, world");
	}
	
	private static HashMap<String, HashMap<String, String>> loadFile(String year){
		try {
			FileInputStream fin = new FileInputStream("/home/maniloni/Senior Project/serialized_data/"+year+".ser");
			ObjectInputStream oin = new ObjectInputStream(fin);
			return (HashMap<String, HashMap<String, String>>) oin.readObject();
		}catch(IOException io) {
			io.printStackTrace();
		} catch(ClassNotFoundException ce) {
			ce.printStackTrace();
		}
		System.out.println("LOAD ERROR " + year);
		return null;
	}
}
