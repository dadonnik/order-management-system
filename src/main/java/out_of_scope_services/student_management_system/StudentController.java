package out_of_scope_services.student_management_system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping("/students/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }
}
