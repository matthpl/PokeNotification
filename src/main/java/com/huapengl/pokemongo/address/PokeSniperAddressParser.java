package com.huapengl.pokemongo.address;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;
import com.huapengl.pokemongo.pokemoninfo.PokemonInfo;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PokeSniperAddressParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(PokeSniperAddressParser.class);

    private static final String POKE_SNIPE_API = "http://www.pokesnipers.com/api/v1/pokemon.json";

    private static final JsonParserFactory FACTORY = JsonParserFactory.getInstance();
    private static final JSONParser PARSER = FACTORY.newJsonParser();
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    private final AddressChecker addressChecker = new AddressChecker();

    private String getPokeSnipeInfo() throws IOException {
        final Request request = new Request.Builder().url(POKE_SNIPE_API).build();

        final Response response = HTTP_CLIENT.newCall(request).execute();
        return response.body().string();
    }

    /**
     *
     * @param json
     * @return a map of pokemon name, list of PokemonInfo
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<String, List<PokemonInfo>> getPokemonCoordMap() throws IOException {
        /*
         * the json looks like this {"results":[{"id":134038,"name":"Vaporeon","coords": "-37.842543,144.933267", "until":"2016-08-20T02:15:58.000Z","iv":0,"attacks":[null,null], "icon":
         * "https://s3-eu-west-1.amazonaws.com/pokesnipers/blackwhite/134.png"}, {"id":134040,"name":"Kangaskhan","coords":"-37.833861,144.952364",
         * "until":"2016-08-20T02:15:59.000Z","iv":0,"attacks":[null,null], "icon": "https://s3-eu-west-1.amazonaws.com/pokesnipers/blackwhite/115.png"}] }
         */
        final String json = getPokeSnipeInfo();
        final Map<String, List<PokemonInfo>> pokeMap = new HashMap<String, List<PokemonInfo>>();
        final List<HashMap> results = (ArrayList<HashMap>) PARSER.parseJson(json).get("results");
        for (final HashMap result : results) {
            PokemonInfo info = new PokemonInfo((String) result.get("name"), (String) result.get("coords"),
                    (String) result.get("until"));
            if (!pokeMap.containsKey((String) result.get("name"))) {
                pokeMap.put((String) result.get("name"), new ArrayList<PokemonInfo>());
            }
            pokeMap.get((String) result.get("name")).add(info);
        }
        return pokeMap;
    }

    public List<PokemonInfo> getPokemonsInCity(List<String> cities, String mapApiKey) throws IOException {
        final List<PokemonInfo> pokeInfos = new ArrayList<PokemonInfo>();
        final Map<String, List<PokemonInfo>> pokeMap = getPokemonCoordMap();
        for (final List<PokemonInfo> infos : pokeMap.values()) {
            for (final PokemonInfo info : infos) {

                String formattedAddress = addressChecker.getFormatAddressForCoord(info.getCoord(), mapApiKey)
                        .toLowerCase();
                boolean found = false;
                for (String cityName : cities) {
                    if (formattedAddress.contains(cityName.toLowerCase()))
                        found = true;
                }
                if (!found)
                    continue;
                DateTime until = ISODateTimeFormat.dateTime().parseDateTime(info.getLastUntil());
                LOGGER.info("!!! found {} in {}, cord {},  despawn in {} seconds", info.getName(), formattedAddress,
                        info.getCoord(), Seconds.secondsBetween(DateTime.now(), until).getSeconds());
                pokeInfos.add(info);
            }
        }
        addressChecker.checkPoint();
        return pokeInfos;
    }
}
