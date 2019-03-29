/**
 * Michael E Anilonis
 * Mar 4, 2019
 */
package api_backend;

import static spark.Spark.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MyAPI {
	public static void main(String[] args) {
		HashMap<String, String> countryDict = getCountryNames();
		HashMap<String, String> reverseDict = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : countryDict.entrySet()) {
			reverseDict.put(entry.getValue(), entry.getKey());
		}
		File[] serialized = new File("/home/maniloni/Senior Project/serialized_data/").listFiles();
		String[] years = new String[serialized.length];
		for (int i = 0; i < serialized.length; i++) {
			String file = serialized[i].getName();
			years[i] = file.substring(0, file.indexOf(".ser"));
		}
		System.out.println(Arrays.toString(years));

		@SuppressWarnings("unchecked")
		HashMap<String, HashMap<String, String>>[] maps = new HashMap[years.length];
		for (int i = 0; i < years.length; i++) {
			maps[i] = loadFile(years[i]);
		}
		get("/hello", (req, res) -> "Hello, world");

		get("/:year", (req, res) -> {
			String year = req.params("year");
			HashMap<String, HashMap<String, String>> thisYear = maps[getYearfromArray(year, years)];
			return JSONEncoder.encodeYear(year, thisYear, reverseDict).toString(4);
		});

		get("/:year/:country", (req, res) -> {
			String year = req.params("year");
			String country = req.params("country");
			String file;
			if (country.length() == 2)
				file = country.toLowerCase() + ".html";
			else {
				file = countryDict.get(country) + ".html";
			}
			HashMap<String, HashMap<String, String>> thisYear = maps[getYearfromArray(year, years)];

			if (thisYear.get(file) == null) {
				return "Error: Country does not exist";
			}

			return JSONEncoder.encodeCountry(country, thisYear.get(file)).toString(4);
		});
		
		post("/stop/:key", (req, res) -> {
			System.out.println(req.contentType());
			return null;
//			String keyfilename = "mykey.pub";
//			byte[] bytes = Files.readAllBytes(Paths.get(keyfilename));
//			PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
//			KeyFactory kf = KeyFactory.getInstance("RSA");
//			PrivateKey pvt = kf.generatePrivate(ks);
		});

	}

	@SuppressWarnings("unchecked")
	private static HashMap<String, HashMap<String, String>> loadFile(String year) {
		try {
			FileInputStream fin = new FileInputStream("/home/maniloni/Senior Project/serialized_data/" + year + ".ser");
			ObjectInputStream oin = new ObjectInputStream(fin);
			HashMap<String, HashMap<String, String>> r = (HashMap<String, HashMap<String, String>>) oin.readObject();
			oin.close();
			fin.close();
			return r;
		} catch (IOException io) {
			io.printStackTrace();
		} catch (ClassNotFoundException ce) {
			ce.printStackTrace();
		}
		System.out.println("LOAD ERROR " + year);
		return null;
	}

	private static int getYearfromArray(String year, String[] years) {
		for (int i = 0; i < years.length; i++) {
			if (year.equals(years[i]))
				return i;
		}
		return -1;
	}

	private static HashMap<String, String> getCountryNames() {
		HashMap<String, String> names = new HashMap<String, String>();
		File f = new File("/home/maniloni/Senior Project/World Factbook Data/country codes.csv");
		try {
			Scanner s = new Scanner(f);
			s.useDelimiter(",|\n");
			while (s.hasNext()) {
				String wholeName = s.next();
				String code = s.next();
				if (!code.contains("-"))
					names.put(wholeName.trim(), code.toLowerCase().trim());
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return names;

	}
}
