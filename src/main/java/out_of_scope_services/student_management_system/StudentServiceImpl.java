package out_of_scope_services.student_management_system;

import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Override
    public Student getStudentById(Long studentId) {
        int grade = (int) (studentId % 10) + 1;

        String avatarUrl = "https://robohash.org/" + studentId;

        return new Student(studentId, "Mocked Student " + studentId, grade, avatarUrl);
    }
}