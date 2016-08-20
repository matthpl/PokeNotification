package com.huapengl.pokemongo.address;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.codesnippets4all.json.exceptions.JSONParsingException;
import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddressChecker {
	private static final String GOOGL_MAP_API = "http://maps.googleapis.com/maps/api/geocode/json?sensor=false&latlng=";

	private static final JsonParserFactory FACTORY = JsonParserFactory.getInstance();
	private static final JSONParser PARSER = FACTORY.newJsonParser();
	private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getFormatAddressForCoord(String latlng) throws IOException {
		Request request = new Request.Builder().url(GOOGL_MAP_API + latlng).build();

		Response response = HTTP_CLIENT.newCall(request).execute();
		String json = response.body().string();
		try {
			List<HashMap> results = (ArrayList<HashMap>) PARSER.parseJson(json).get("results");

			if (results.isEmpty())
				return "";
			// take the first one, if error, try the other
			for(HashMap map : results){
				try {
					List<HashMap> addressComps = (List<HashMap>) map.get("address_components");
					for(HashMap addComp : addressComps) {
						List<String> types = (List<String>) addComp.get("types");
						if(types.contains("locality")) {
							return (String)addComp.get("long_name");
						}
					}
				} catch (Exception ee) {
					continue;
				}
			}
		} catch (JSONParsingException e) {
			return "";
		}
		return "";
	}
}
