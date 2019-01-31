package se.anyro.nfc_reader.setup;

public class VariableRepository {

    private String teacherName;
    private String teacherLogin;
    private String courseName;
    private String studentName;
    private String nfc;
    private String studentId;

    private VariableRepository() {
        this.teacherName = null;
        this.teacherLogin = null;
        this.courseName = null;
        this.studentName = "";
        this.nfc = null;

    }
    static VariableRepository instance = new VariableRepository();
    public static VariableRepository getInstance() {
        return instance;
    }

    public void setTeacherName(String name) {
        this.teacherName=name;
    }
    public String getTeacherName() {
        return this.teacherName;
    }
    public String getTeacherLogin() {
        return teacherLogin;
    }

    public void setTeacherLogin(String teacherLogin) {
        this.teacherLogin = teacherLogin;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getNfc() {
        return nfc;
    }

    public void setNfc(String nfc) {
        this.nfc = nfc;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
