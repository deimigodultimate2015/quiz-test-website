package com.sol.model.utils;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

//This class use to make json filter shorter
@Service
public class CustomJsonFilter {
	
	public<T> MappingJacksonValue getMappingJacksonValue(String[] valuesAllowed, String filterName, T objectToJson) {
		
		SimpleBeanPropertyFilter _filter = SimpleBeanPropertyFilter.
				filterOutAllExcept(new HashSet<>(Arrays.asList(valuesAllowed)));
		
		SimpleFilterProvider provider = new SimpleFilterProvider().addFilter(filterName, _filter);
		provider.setFailOnUnknownId(false);
		
		MappingJacksonValue mapping = new MappingJacksonValue(objectToJson);
		mapping.setFilters(provider);
		
		return mapping;
		
	}
	
	/*Custom Json filter parameter now support to help you take 2 filter and more
	With multivaluesAllowed each row is match with filter String same column in filter name
	Like multivaluesAllowed[0] will be valuesAllowed for filterName[0]
	and  multivaluesAllowed[1] will be valuesAllowed for filterName[1]*/
	public<T> MappingJacksonValue getMappingJacksonValueMultiFilter(String[][] multivaluesAllowed, String[] filterName, T objectToJson) {
		
		MappingJacksonValue mapping = new MappingJacksonValue(objectToJson);
		
		SimpleFilterProvider provider = new SimpleFilterProvider();
		provider.setFailOnUnknownId(false);
		
		for(int i = 0; i < multivaluesAllowed.length; i++) {
			SimpleBeanPropertyFilter _filter = SimpleBeanPropertyFilter.
					filterOutAllExcept(new HashSet<>(Arrays.asList(multivaluesAllowed[i])));
			
			provider.addFilter(filterName[i], _filter);
			
		}
		mapping.setFilters(provider);
		
		return mapping;
		
		
	}
	
}
