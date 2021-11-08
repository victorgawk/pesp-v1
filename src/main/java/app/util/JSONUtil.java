package app.util;

import java.io.UnsupportedEncodingException;

import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;

public class JSONUtil {
	private static JSONParser parser;
	
	public static Object parse(String str) throws UnsupportedEncodingException, ParseException {
		if (parser == null) {
			parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
		}
		return parser.parse(str);
	}
}
