/**
 * Michael E Anilonis
 * Mar 4, 2019
 */
package api_backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONObject;

public class JSONEncoder {
	
	public static JSONObject encodeYear(String year, HashMap<String, HashMap<String, String>> yearMap, HashMap<String, String> reverseCountryCode) {
		JSONObject r = new JSONObject();
		Set<String> keys = yearMap.keySet();
		ArrayList<String> countryNames = new ArrayList<String>();
		for(String str: keys) {
			str = str.replace(".html", "");
			String country = reverseCountryCode.get(str);
			if(country == null) {
				System.out.println("Error with "+str);
				continue;
			}
			countryNames.add(reverseCountryCode.get(str));
		}
		
		r.put("year", year);
		r.put("countries", countryNames.toArray());
		return r;
	}
	
	public static JSONObject encodeCountry(String name, HashMap<String, String> country) {
		System.out.println(country.size());
		Set<String> keys = country.keySet();
		JSONObject r = new JSONObject();
		r.put("Country", name);
		JSONObject countryData = new JSONObject();
		for(String str: keys) {
			String data = country.get(str);
			countryData.put(str, data);
		}
		
		r.put("data", countryData);
		
		return r;
	}
}
