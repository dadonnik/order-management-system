package out_of_scope_services.student_management_system;

import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Override
    public Student getStudentById(Long studentId) {
        return new Student(studentId, "Mocked Student " + studentId);
    }
}