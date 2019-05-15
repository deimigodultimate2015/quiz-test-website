package com.sol.controller.api;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sol.controller.customeException.ObjectAlreadyExists;
import com.sol.controller.customeException.StudentNotFoundException;
import com.sol.controller.message.request.QuizCreator;
import com.sol.controller.message.request.QuizUpdate;
import com.sol.model.entities.Quiz;
import com.sol.model.entities.Subject;
import com.sol.model.repositories.QuizRepository;
import com.sol.model.repositories.SubjectRepository;
import com.sol.model.utils.CheckValueAndSetDefault;
import com.sol.model.utils.CustomJsonFilter;
import com.sol.model.utils.CustomPageable;

@RestController
@CrossOrigin("*")
@RequestMapping("/manager")
public class QuizAPI {
	
	@Autowired
	QuizRepository quizRepository ;
	
	@Autowired
	SubjectRepository subjectRepository;
	
	@Autowired
	CheckValueAndSetDefault checkValue;
	
	@Autowired
	CustomPageable cPageable;
	
	@Autowired
	CustomJsonFilter cJsonFilter;	
	
	@PostMapping("/quiz")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSADMIN')")
	public ResponseEntity<Object> createQuiz(@Validated @RequestBody QuizCreator quiz) {
		
		if(!subjectRepository.existsById(quiz.getSubjectID())) {
			throw new StudentNotFoundException("Not found subject with ID: "+quiz.getSubjectID());
		} else if (quizRepository.existsByName(quiz.getName())) {
			throw new ObjectAlreadyExists("Quiz name already exists: "+quiz.getName());
		}
		
		Quiz _quiz = new Quiz();
		Subject subject = new Subject();
		subject.setId(quiz.getSubjectID());
		_quiz.setName(quiz.getName());
		_quiz.setSubject(subject);
		_quiz.setTimeLimit(quiz.getTimeLimit());
		_quiz.setTotalQuestion(quiz.getTotalQuestion());
		_quiz.setPass(quiz.getPass());
		_quiz.setQuizQuestion(quiz.getQuizQuestion());
		
		quizRepository.save(_quiz);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(_quiz.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/quiz")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSADMIN')")
	public void createQuiz(@Validated @RequestBody QuizUpdate quizUpdate) {
		
		Optional<Quiz> _quiz = quizRepository.findById(quizUpdate.getId());
		if(!_quiz.isPresent()) {
			throw new StudentNotFoundException("Not found quiz with ID: "+quizUpdate.getId());
		} else {}
		
		if(_quiz.get().getName().equals(quizUpdate.getName())) {
			
		} else {
			if(quizRepository.existsByName(quizUpdate.getName())) {
				throw new ObjectAlreadyExists("Quiz name already exists: "+quizUpdate.getName());
			}
		}
		
		if(!subjectRepository.existsById(quizUpdate.getSubjectID())) {
			throw new StudentNotFoundException("Not found subject with ID: "+quizUpdate.getSubjectID());
		}
		
		Subject subject = new Subject(); subject.setId(quizUpdate.getSubjectID());
		_quiz.get().setName(quizUpdate.getName());
		_quiz.get().setSubject(subject);
		_quiz.get().setTotalQuestion(quizUpdate.getTotalQuestion());
		_quiz.get().setPass(quizUpdate.getPass());
		_quiz.get().setTimeLimit(quizUpdate.getTimeLimit());
		_quiz.get().setQuizQuestion(quizUpdate.getQuizQuestion());
		
		quizRepository.save(_quiz.get());
	
	}
	
	@GetMapping("/quiz/{id}")
	public MappingJacksonValue getQuiz(@PathVariable("id") Long id) {
		Optional<Quiz> quiz = quizRepository.findById(id);
		
		String[][] multiValueAllowed = {
				{"id","student","testDate","percentComplete","status"},
				{"id","username","name","gender","birthday"},
				{"id", "name"}
		};
		
		if(quiz.isPresent()) {
			
			return cJsonFilter
					.getMappingJacksonValueMultiFilter(multiValueAllowed, new String[] {"activityFilter", "studentFilter", "subjectFilter"}, quiz.get());
			
		} else {	
			throw new StudentNotFoundException("Not found quiz with id: "+id);
		}
	}
	
	@GetMapping("/quizs")
	public MappingJacksonValue searchQuizs(@RequestParam(name = "searchColumn",  defaultValue = "id") String searchColumn,
							  		       @RequestParam(name = "searchText", 	 defaultValue = "") String searchText,
									       @RequestParam(name = "sortColumn", 	 defaultValue = "id") String sortColumn,
									       @RequestParam(name = "sortType", 	 defaultValue = "ASC") String sortType,
									       @RequestParam(name = "pageSize", 	 defaultValue = "5") Integer pageSize,
									       @RequestParam(name = "pageNumber",	 defaultValue = "0") Integer pageNumber) {
		
		searchColumn = checkValue.CheckValueAndSetDefaultWithCaseSensitive(searchColumn,new String[] {"id","name","subject"}, "id");		
		sortColumn = checkValue.CheckValueAndSetDefaultWithCaseSensitive(sortColumn,new String[] {"id","name","timeLimit","totalQuestion","pass"}, "id");	
		sortType = checkValue.CheckValueAndSetDefaultWithoutCaseSensitive(sortType, new String[] {"ASC", "DESC"}, "ASC").toUpperCase();
		
		if(pageNumber < 0) pageNumber = 0;
		if(pageSize < 0) pageSize = 0;
		
		String[][] multiValueAllowed = {
				{"id","student","testDate","percentComplete","status"},
				{"id","username","name","gender","birthday"},
				{"id", "name"}
		};
		
		Pageable pageable = cPageable.pageableWithStringSortType(pageNumber, pageSize, sortColumn, sortType);
		Page<Quiz> quizsPage ;
		
		switch (searchColumn) {
		case "id":
			quizsPage = quizRepository.findByIdContaining(searchText, pageable);
			break;
		case "name":
			quizsPage = quizRepository.findByNameContaining(searchText, pageable);
			break;
		case "subject":
			quizsPage = quizRepository.findBySubjectNameContaining(searchText, pageable);
			break;
		default:
			quizsPage = quizRepository.findAll(pageable);
			break;
		}
		
		return cJsonFilter.getMappingJacksonValueMultiFilter(multiValueAllowed, new String[] {"activityFilter", "studentFilter", "subjectFilter"}, quizsPage);
		
	}
	
}
