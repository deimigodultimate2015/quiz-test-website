package com.sol.model.utils;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class CalendarAction {
	
	@SuppressWarnings("static-access")
	public Date editDate(Date date, int dencrease) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendar.DATE, dencrease);
		return calendar.getTime();
	}
	
}
