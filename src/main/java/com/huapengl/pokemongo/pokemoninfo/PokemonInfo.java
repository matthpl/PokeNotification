package com.huapengl.pokemongo.pokemoninfo;

public class PokemonInfo {

    private final String name;
    private final String coord;
    private final String lastUntil;

    public PokemonInfo(String name, String coord, String lastUntil) {
        this.name = name;
        this.coord = coord;
        this.lastUntil = lastUntil;
    }

    public String getName() {
        return name;
    }

    public String getCoord() {
        return coord;
    }

    public String getLastUntil() {
        return lastUntil;
    }

}
