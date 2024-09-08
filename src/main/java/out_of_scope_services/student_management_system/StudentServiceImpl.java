package out_of_scope_services.student_management_system;

import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
    }
}