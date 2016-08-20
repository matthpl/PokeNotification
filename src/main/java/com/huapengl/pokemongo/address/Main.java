package com.huapengl.pokemongo.address;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.ImmutableList;

public class Main {
    private static final int CHECK_INTERVAL_SECONDS = 30;

    private static final Random RANDOM = new Random();
    private static final List<String> GOOGLE_MAP_API_KEYS = ImmutableList.of(
            "You Key here");

    public static final void main(String... args) throws InterruptedException {
        PokeSniperAddressParser parser = new PokeSniperAddressParser();
        while (true) {
            try {
                String key = randomlyGetApiKey(GOOGLE_MAP_API_KEYS);
                parser.getPokemonsInCity("Seattle", key);
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
