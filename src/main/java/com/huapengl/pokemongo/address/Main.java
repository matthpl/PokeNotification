package com.huapengl.pokemongo.address;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {
	private static final int CHECK_INTERVAL_SECONDS = 60;
	private static final String GOOGLE_MAP_API_KEY = "";
	
	public static final void main(String ... args) throws InterruptedException {
		PokeSniperAddressParser parser = new PokeSniperAddressParser();
		while(true) {
			try {
				parser.getPokemonsInCity("Seattle", GOOGLE_MAP_API_KEY);
				System.out.println("Sleeping " + CHECK_INTERVAL_SECONDS + " seconds");
			} catch (IOException e) {}
			Thread.sleep(TimeUnit.SECONDS.toMillis(CHECK_INTERVAL_SECONDS));
		}
	}
}
