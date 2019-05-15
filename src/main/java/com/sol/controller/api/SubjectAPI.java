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
import com.sol.model.entities.Subject;
import com.sol.model.repositories.SubjectRepository;
import com.sol.model.utils.CheckValueAndSetDefault;
import com.sol.model.utils.CustomJsonFilter;
import com.sol.model.utils.CustomPageable;

@RestController
@CrossOrigin("*")
@RequestMapping("/manager")
public class SubjectAPI {

	@Autowired
	SubjectRepository subjectRepository;
	
	@Autowired
	CheckValueAndSetDefault checkValueAS;
	
	@Autowired
	CustomPageable cPageable;
	
	@Autowired
	CustomJsonFilter cJsonFilter;
	
	@GetMapping("/subject/{id}")
	public MappingJacksonValue getSubject(@PathVariable("id") Long id) {
		Optional<Subject> subject = subjectRepository.findById(id);
		if(!subject.isPresent()) {
			throw new StudentNotFoundException("Subject not found with id: "+id);
		}
	
		return cJsonFilter.getMappingJacksonValue(new String[] {"name","id","timeLimit","totalQuestion","pass","activities","quizQuestion"}, "quizFilter", subject.get());
	}
	
	@PostMapping("/subject")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSADMIN')")
	public ResponseEntity<Object> createSubject(@Validated @RequestBody Subject subject ) {
		if( subjectRepository.existsByName(subject.getName())) {
			throw new ObjectAlreadyExists("Subject name already exists"+subject.getName());
		}
		
		Subject _subject = new Subject(subject.getName());
		_subject = subjectRepository.save(_subject);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(_subject.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/subjects")
	public MappingJacksonValue searchSubjects(@RequestParam(name = "searchColumn", defaultValue = "id") String searchColumn,
							  		    @RequestParam(name = "searchText", 	 defaultValue = "") String searchText,
									    @RequestParam(name = "sortColumn", 	 defaultValue = "id") String sortColumn,
									    @RequestParam(name = "sortType", 	 defaultValue = "ASC") String sortType,
									    @RequestParam(name = "pageSize", 	 defaultValue = "5") Integer pageSize,
									    @RequestParam(name = "pageNumber",	 defaultValue = "0") Integer pageNumber) {
		searchColumn = checkValueAS.CheckValueAndSetDefaultWithCaseSensitive(searchColumn, new String[] {"id","name"}, "id");
		sortColumn = checkValueAS.CheckValueAndSetDefaultWithCaseSensitive(sortColumn, new String[] {"id","name"}, "id");
		sortType = checkValueAS.CheckValueAndSetDefaultWithoutCaseSensitive(sortType, new String[] {"ASC", "DESC"}, "ASC").toUpperCase();
		
		if(pageNumber < 0) pageNumber = 0;
		if(pageSize < 0) pageSize = 0;
		
		Pageable pageable = cPageable.pageableWithStringSortType(pageNumber, pageSize, sortColumn, sortType);
		Page<Subject> page;
		
		switch(searchColumn) {
		case "id":
			page = subjectRepository.findByIdContaining(searchText, pageable);
			break;
		case "name":
			page = subjectRepository.findByNameContaining(searchText, pageable);
			break;
		default :
			page = subjectRepository.findAll(pageable);
			break;
		}
		
		return cJsonFilter.getMappingJacksonValue(new String[] {"name","id","timeLimit","totalQuestion","pass","quizQuestion"}, "quizFilter", page);
		
	}
	
	@PutMapping("/subject")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSADMIN')")
	public ResponseEntity<Object> Subject(@RequestBody Subject subject) {
		Optional<Subject> _subject = subjectRepository.findById(subject.getId());
		if(!_subject.isPresent()) {
			throw new StudentNotFoundException("Subject not found with id: "+subject.getId());
		}
		
		_subject.get().setName(subject.getName());
		subjectRepository.save(_subject.get());
		
		return ResponseEntity.noContent().build();
		
	}
	
}
