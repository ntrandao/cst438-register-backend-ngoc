package com.cst438.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://registerf-cst438.herokuapp.com/"})
public class StudentController {
	
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	GradebookService gradebookService;
	
	
	@PostMapping("/student")
	@Transactional
	public StudentDTO addStudent( @RequestBody StudentDTO studentDTO) { 

		Student student = new Student();
		Student other = studentRepository.findByEmail(studentDTO.email);
		
//		String student_email = "test@csumb.edu";   // student's email 
		
//		Student student = studentRepository.findByEmail(student_email);
//		Course course  = courseRepository.findById(studentDTO.student_id).orElse(null);
		
		// student.status
		// = 0  ok to register
		// != 0 hold on registration.  student.status may have reason for hold.
		
		if (other == null) {
			// TODO check that today's date is not past add deadline for the course.
//			Enrollment enrollment = new Enrollment();
//			enrollment.setStudent(student);
//			Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
//			
//			gradebookService.enrollStudent(student_email, student.getName(), course.getCourse_id());
//			
//			StudentDTO result = addStudentDTO(savedEnrollment);
//			return result;
//			student.setStudent_id(studentDTO.student_id);
			student.setName(studentDTO.name);
			student.setEmail(studentDTO.email);
			student.setStatusCode(studentDTO.statusCode);
			student.setStatus(studentDTO.status);
			
			Student savedStudent = studentRepository.save(student);
			
			StudentDTO result = createStudentDTO(savedStudent);
			
			return result;
		} else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Course_id invalid or student not allowed to register for the course.  "+studentDTO.student_id);
		}
//		
		
	}
	
	//convert student object to studentDTO
	private StudentDTO createStudentDTO(Student s) {
		StudentDTO studentDTO = new StudentDTO();
//		Student c = e.getStudent();
//		studentDTO.id =e.getStudent_id();
		studentDTO.statusCode = s.getStatusCode();
		studentDTO.status = s.getStatus();
		studentDTO.email = s.getEmail();
		studentDTO.name = s.getName();
		studentDTO.student_id = s.getStudent_id();
		return studentDTO;
	}
	
}
//retry for commit