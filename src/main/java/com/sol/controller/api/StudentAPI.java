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

import com.sol.controller.customeException.StudentNotFoundException;
import com.sol.controller.message.request.StudentCreator;
import com.sol.model.entities.Student;
import com.sol.model.repositories.StudentRepository;
import com.sol.model.utils.CheckValueAndSetDefault;
import com.sol.model.utils.CustomJsonFilter;
import com.sol.model.utils.CustomPageable;
import com.sol.model.utils.UsernameCreator;

@RestController
@CrossOrigin("*")
@RequestMapping("/manager")
public class StudentAPI {
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	UsernameCreator usernameCreator ;
	
	@Autowired
	CheckValueAndSetDefault checkValue;
	
	@Autowired
	CustomPageable cPageable;
	
	@Autowired
	CustomJsonFilter cJsonFilter;
	
	@GetMapping("/student/{id}")
	public MappingJacksonValue getStudent(@PathVariable Long id) {
		
		Optional<Student> student = studentRepository.findById(id);
		if(!student.isPresent()) {
			throw new StudentNotFoundException("Student id "+id);
		} else { }
		
		String[][] multiValueAllowed = {
				{"id","name"},
				{"id","username","name","gender","birthday"},
				{"name","id","timeLimit","totalQuestion","pass"}
		};
		
		return cJsonFilter.getMappingJacksonValueMultiFilter(multiValueAllowed, new String[] {"subjectFilter", "studentFilter","quizFilter"}, student);
		
	}
	
	@PostMapping("/student")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSADMIN')")
	public ResponseEntity<Object> createStudent(@Validated @RequestBody StudentCreator student) {
		Student _student = new Student(student.getName(), student.isGender(), student.getBirthday());
		Student _student2 = studentRepository.save(_student);
		_student2.setUsername(usernameCreator.createUsernameFromNameAndID(_student2.getName(), _student2.getId(), 6));
		
		studentRepository.save(_student2);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(_student2.getId()).toUri();
		
		return ResponseEntity.created(location).build();
		
	}
	
	@PutMapping("/student")
	@PreAuthorize("hasRole('ADMIN') or hasRole('SYSADMIN')")
	public ResponseEntity<Object> updateStudent(@Validated @RequestBody Student student) {
		
		Optional<Student> _student = studentRepository.findById(student.getId());
		
		if(!_student.isPresent()) {
			throw new StudentNotFoundException("Student id "+student.getId());
		}
		
		student.setUsername(_student.get().getUsername());
		studentRepository.save(student);
		
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/students")
	public MappingJacksonValue getStudents(@RequestParam(name = "searchColumn", defaultValue = "id") String searchColumn,
				  		    @RequestParam(name = "searchText", 	 defaultValue = "") String searchText,
						    @RequestParam(name = "sortColumn", 	 defaultValue = "id") String sortColumn,
						    @RequestParam(name = "sortType", 	 defaultValue = "ASC") String sortType,
						    @RequestParam(name = "pageSize", 	 defaultValue = "5") Integer pageSize,
						    @RequestParam(name = "pageNumber",	 defaultValue = "0") Integer pageNumber) {
		
		searchColumn = checkValue.CheckValueAndSetDefaultWithCaseSensitive(searchColumn,new String[] {"id","username","name","birthday"}, "id");		
		sortColumn = checkValue.CheckValueAndSetDefaultWithCaseSensitive(searchColumn,new String[] {"id","username","name","birthday"}, "id");	
		sortType = checkValue.CheckValueAndSetDefaultWithoutCaseSensitive(sortType, new String[] {"ASC", "DESC"}, "ASC").toUpperCase();
		
		if(pageNumber < 0) pageNumber = 0;
		if(pageSize < 0) pageSize = 0;
		
		String[][] multiValueAllowed = {
				{"id","name"},
				{"id","username","name","gender","birthday"},
				{"name","id","timeLimit","totalQuestion","pass"}
		};
		
		Pageable pageable = cPageable.pageableWithStringSortType(pageNumber, pageSize, sortColumn, sortType);
		Page<Student> page;
		
		switch ( searchColumn ) {
		
		case "id":
			page = studentRepository.findByIdContaining(searchText, pageable);
			break;
		case "username":
			page = studentRepository.findByUsernameContaining(searchText, pageable);
			break;
		case "name":
			page = studentRepository.findByNameContaining(searchText, pageable);
			break;
		case "birthday":
			page = studentRepository.findByBirthdayContaining(searchText, pageable);
			break;
		default:
			page = studentRepository.findAll(pageable);
			break;
		}
		
		return cJsonFilter.getMappingJacksonValueMultiFilter(multiValueAllowed, new String[] {"subjectFilter", "studentFilter","quizFilter"}, page);
		
	}
	
}
