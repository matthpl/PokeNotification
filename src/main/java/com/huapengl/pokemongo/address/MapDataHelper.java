package com.huapengl.pokemongo.address;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Avoid making too much API calls to google map API
 *
 */
public class MapDataHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapDataHelper.class);
    
	private Map<String, String> map; // "coordinate, city";

	@SuppressWarnings("unchecked")
	public MapDataHelper() {
		// restore map from file
	    try{
	        File toRead=new File("db");
	        if(!toRead.exists()) {
	        	map = new HashMap<String, String>();
	        	return;
	        }
	        FileInputStream fis=new FileInputStream(toRead);
	        ObjectInputStream ois=new ObjectInputStream(fis);

	        map =(Map<String,String>)ois.readObject();

	        ois.close();
	        fis.close();
	        LOGGER.info("Restoring from db, map size: " + map.size());
	    }catch(Exception e){
	    	map = new HashMap<String, String>();
	    }
	}

	public void storeMapData() {
		// write to file : "db"
		try {
			File fileOne = new File("db");
			fileOne.delete();
			FileOutputStream fos = new FileOutputStream(fileOne);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(this.map);
			oos.flush();
			oos.close();
			fos.close();
			LOGGER.info("map checkpoint, map size " + map.size());
		} catch (Exception e) {
		}
	}
	
	public String getCityNameIfInCache(String coord) {
		return map.get(coord);
	}
	
	public void putInCache(String coord, String city) {
		map.put(coord, city);
	}
}
