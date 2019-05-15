package com.sol.model.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsernameCreator {
	
	@Autowired
	TextConverter textConvert ;
	
	//Create username with id and name will be like:
	//Thai xuan tho + id = 2090 + id lenght = 6 => thotx002090
	public String createUsernameFromNameAndID(String name, Long id, int id_size) {
		String _name = textConvert.vietnameseToUnicode(name);
		return fromNameToShorterName(_name) + fromIDToString(id, id_size);
	}
	
	//This method will put some zero as prefix to your string like 6 to 0006
	public String fromIDToString(Long id, int size) {
		String _id = id+"";
        while(_id.length() < size) {
        	_id = "0" + _id;
        }
        
        return _id;
	}
	
	//Your name will from "Thai Xuan Tho" to "thotx"
	public String fromNameToShorterName(String name) {
		String [] nameButInArray = name.split(" ");
        String _name = nameButInArray[nameButInArray.length-1];
        for(int i = 0 ; i < (nameButInArray.length - 1); i++) {
        	_name = _name + nameButInArray[i].charAt(0);
        }
        
        return _name.toLowerCase();
	}
}
