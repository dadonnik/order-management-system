package out_of_scope_services.student_management_system;

public class Student {
    private Long id;
    private String name;
    private int grade;
    private String avatarUrl;

    public Student(Long id, String name, int grade, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.avatarUrl = avatarUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
