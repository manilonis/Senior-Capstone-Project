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
			countryNames.add(reverseCountryCode.get(str));
		}
		
		r.put("year", year);
		r.put("countries", countryNames.toArray());
		return r;
	}
}
