package com.huapengl.pokemongo.address;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.huapengl.pokemongo.config.Config;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    private static final int CHECK_INTERVAL_SECONDS = 30;

    private static final Random RANDOM = new Random();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static final void main(String... args) throws InterruptedException {
        final Config config;
        try {
            config = MAPPER.readValue(new File("configurations/config.json"), Config.class);
        } catch (IOException e) {
            throw new RuntimeException("Fail parsing config files", e);
        }
        PokeSniperAddressParser parser = new PokeSniperAddressParser();
        while (true) {
            try {
                String key = randomlyGetApiKey(config.getGoogleApiKeys());
                parser.getPokemonsInCity(config.getCities(), key);
                System.out.println("Sleeping " + CHECK_INTERVAL_SECONDS + " seconds");
            } catch (IOException e) {}
            Thread.sleep(TimeUnit.SECONDS.toMillis(CHECK_INTERVAL_SECONDS));
        }
    }

    private static String randomlyGetApiKey(List<String> keys) {
        final int indx = RANDOM.nextInt(keys.size());
        return keys.get(indx);
    }
}
