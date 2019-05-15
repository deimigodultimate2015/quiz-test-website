package com.sol.controller.api;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.sol.model.entities.Subject;

public class SubjectAPITest {

	@Test
	public void testGetSubject() {
		Subject subject = new Subject();
		subject.setId(1L);
		subject.setName("Interview 2.9.1998");
//		JSONAssert
	}

}
