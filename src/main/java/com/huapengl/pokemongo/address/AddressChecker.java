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
    private static final String GOOGL_MAP_API = "https://maps.googleapis.com/maps/api/geocode/json?sensor=false&key=";

    private static final JsonParserFactory FACTORY = JsonParserFactory.getInstance();
    private static final JSONParser PARSER = FACTORY.newJsonParser();
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final MapDataHelper MAP_CACHE = new MapDataHelper();

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String getFormatAddressForCoord(String latlng, String mapApiKey) throws IOException {
        if (MAP_CACHE.getCityNameIfInCache(latlng) != null) {
            return MAP_CACHE.getCityNameIfInCache(latlng);
        }
        final Request request = new Request.Builder().url(GOOGL_MAP_API + mapApiKey + "&latlng=" + latlng).build();

        Response response = HTTP_CLIENT.newCall(request).execute();
        String json = response.body().string();
        try {
            final List<HashMap> results = (ArrayList<HashMap>) PARSER.parseJson(json).get("results");

            if (results.isEmpty()){
                MAP_CACHE.putInCache(latlng, "");
                return "";
            }

            // take the first one, if error, try the other
            for (HashMap map : results) {
                try {
                    List<HashMap> addressComps = (List<HashMap>) map.get("address_components");
                    for (HashMap addComp : addressComps) {
                        List<String> types = (List<String>) addComp.get("types");
                        if (types.contains("locality")) {
                            String name = (String) addComp.get("long_name");
                            MAP_CACHE.putInCache(latlng, name);
                            return name;
                        }
                    }
                } catch (Exception ee) {
                    continue;
                }
            }
        } catch (JSONParsingException e) {
            MAP_CACHE.putInCache(latlng, "");
            return "";
        }
        MAP_CACHE.putInCache(latlng, "");
        return "";
    }

    /**
     * store map data
     */
    public void checkPoint() {
        MAP_CACHE.storeMapData();
    }
}
