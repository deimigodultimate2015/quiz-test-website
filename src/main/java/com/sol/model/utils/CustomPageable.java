package com.sol.model.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CustomPageable {
	
	//Replace Sort.of("sort column").sortType with Sort.of("sort column", "sort type")
	public Pageable pageableWithStringSortType(int pageNumber, int pageSize, String sortColumn ,String sortType) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortColumn).ascending());
		if(sortType.equalsIgnoreCase("ASC")) {
			pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortColumn).ascending());
		} else if(sortType.equalsIgnoreCase("DESC")) {
			pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortColumn).descending());
		}
		
		return pageable;
	}
}
