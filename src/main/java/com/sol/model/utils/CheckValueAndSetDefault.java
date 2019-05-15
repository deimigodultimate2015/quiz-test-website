package com.sol.model.utils;

import org.springframework.stereotype.Service;

//This utils help you check the input and fix value if it not what you wanted
@Service
public class CheckValueAndSetDefault {
	
	//Check if your current value not match any constraint you give and replace it with default
	public String CheckValueAndSetDefaultWithoutCaseSensitive(String currentValue, String[] constraintValues, String defaultValue) {
		for(int i = 0; i < constraintValues.length; i++) {
			if(currentValue.equalsIgnoreCase(constraintValues[i])) {
				return currentValue;
			} else {}
		}
		
		return defaultValue;
	}
	
	public String CheckValueAndSetDefaultWithCaseSensitive(String currentValue, String[] constraintValues, String defaultValue) {
		for(int i = 0; i < constraintValues.length; i++) {
			if(currentValue.equals(constraintValues[i])) {
				return currentValue;
			} else {}
		}
		
		return defaultValue;
	}
	
	//Format input from "3" to "%3%" to make contain in sql
	public String fromEqualToContain(String equal) {
		return "%"+equal+"%";
	}
}
