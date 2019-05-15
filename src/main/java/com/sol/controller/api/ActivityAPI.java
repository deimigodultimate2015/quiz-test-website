package com.sol.controller.api;

import java.net.URI;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sol.controller.customeException.StudentNotFoundException;
import com.sol.controller.message.request.ActivityCreator;
import com.sol.model.entities.Activity;
import com.sol.model.entities.Quiz;
import com.sol.model.entities.Student;
import com.sol.model.repositories.ActivityRepository;
import com.sol.model.repositories.QuizRepository;
import com.sol.model.repositories.StudentRepository;
import com.sol.model.utils.CheckValueAndSetDefault;
import com.sol.model.utils.CustomJsonFilter;
import com.sol.model.utils.CustomPageable;

@RestController
@RequestMapping("/manager")
@CrossOrigin("*")
public class ActivityAPI {
	
	@Autowired
	ActivityRepository activityRepository;
	
	@Autowired
	QuizRepository quizRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	CheckValueAndSetDefault checkValue;
	
	@Autowired
	CustomPageable cPageable;
	
	@Autowired
	CustomJsonFilter cJsonFilter;
	
	@PostMapping("/activity")
	public ResponseEntity<Object> createActitvy(@Validated @RequestBody ActivityCreator activity) {
		
		if(!quizRepository.existsById(activity.getQuizID())) {
			throw new StudentNotFoundException("Not found quiz with id: "+activity.getQuizID());
		} else if (!studentRepository.existsByUsername(activity.getStudentUsername())) {
			throw new StudentNotFoundException("Not found student with username: "+activity.getStudentUsername());
		}
		
		Activity _activity = new Activity();
		Quiz quiz = new Quiz(); quiz.setId(activity.getQuizID());
		Student student = new Student(); student.setId(studentRepository.findByUsername(activity.getStudentUsername()).get().getId());
		
		_activity.setQuiz(quiz);
		_activity.setStudent(student);
		_activity.setStatus(activity.isStatus());
		_activity.setPercentComplete(activity.getPercentComplete());
		_activity.setTestDate(new Date());
		activityRepository.save(_activity);
		
		URI location = ServletUriComponentsBuilder
							.fromCurrentRequest()
							.path("/{id}")
							.buildAndExpand(_activity.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/activities")
	public MappingJacksonValue searchActivity(@RequestParam(name = "searchColumn", defaultValue = "id") String searchColumn,
							  		    @RequestParam(name = "searchText", 	 defaultValue = "") String searchText,
									    @RequestParam(name = "sortColumn", 	 defaultValue = "id") String sortColumn,
									    @RequestParam(name = "sortType", 	 defaultValue = "ASC") String sortType,
									    @RequestParam(name = "pageSize", 	 defaultValue = "5") Integer pageSize,
									    @RequestParam(name = "pageNumber",	 defaultValue = "0") Integer pageNumber) {
		
		searchColumn = checkValue.CheckValueAndSetDefaultWithCaseSensitive(searchColumn, new String[] {"id","student","quiz", "testDate"}, "id");
		sortColumn = checkValue.CheckValueAndSetDefaultWithCaseSensitive(sortColumn, new String[] {"id", "testDate","percentComplete","status"}, "id");
		sortType = checkValue.CheckValueAndSetDefaultWithoutCaseSensitive(sortType, new String[] {"ASC","DESC"}, "ASC").toUpperCase();
		
		if (pageSize < 0) pageSize = 0;
		if (pageNumber < 0) pageNumber = 0;
		
		String[][] multiValueAllowed = {
				{"id","name"},
				{"id","username","gender","name","birthday"},
				{"name","id","timeLimit","totalQuestion","pass"}
		};
		
		Pageable pageable = cPageable.pageableWithStringSortType(pageNumber, pageSize, sortColumn, sortType);
		Page<Activity> page ;
		
		switch( searchColumn ) {
		case "id":
			page = activityRepository.findByIdContaining(searchText, pageable);
		case "student":
			page = activityRepository.findByStudentNameContaining(searchText, pageable);
		case "quiz":
			page = activityRepository.findByQuizNameContaining(searchText, pageable);
		case "testDate":
			page = activityRepository.findByTestDateContaining(searchText, pageable);
		default:
			page = activityRepository.findAll(pageable);
		}
		
		return cJsonFilter.getMappingJacksonValueMultiFilter(multiValueAllowed, new String[] {"subjectFilter", "studentFilter","quizFilter"}, page);
	}
	
	@GetMapping("/activity/{id}")
	public MappingJacksonValue getActivity(@PathVariable("id") Long id) {
		
		Optional<Activity> activity = activityRepository.findById(id);
		if(!activity.isPresent()) {
			throw new StudentNotFoundException("Can not found activity with id: "+id);
		} else {}
		
		String[][] multiValueAllowed = {
				{"id","name"},
				{"id","username","name","gender","birthday"},
				{"name","id","timeLimit","totalQuestion","pass", "quizQuestion"}
		};
		
		return cJsonFilter.getMappingJacksonValueMultiFilter(multiValueAllowed, new String[] {"subjectFilter", "studentFilter","quizFilter"}, activity.get());
	}
	
}
