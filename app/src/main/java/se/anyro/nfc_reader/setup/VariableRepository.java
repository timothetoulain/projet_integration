package se.anyro.nfc_reader.setup;

/**
 * Store useful variables so they can be reach at any time from any class/activity
 */
public class VariableRepository {

    private String teacherName;
    private String teacherLogin;
    private String courseName;
    private String studentName;
    private String nfc;
    private String studentId;
    private int onResumeCounter;
    private String url;


    private VariableRepository() {
        this.url="http://3.120.246.93/checkpresence/controller/queries.php";
        this.teacherName = null;
        this.teacherLogin = null;
        this.courseName = null;
        this.studentName = "";
        this.nfc = null;
        this.onResumeCounter=0;
    }
    static VariableRepository instance = new VariableRepository();
    public static VariableRepository getInstance() {
        return instance;
    }

    public String getUrl(){return this.url; }

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

    public int getOnResumeCounter(){ return this.onResumeCounter; }
    public void incrementOnResumeCounter(){
        this.onResumeCounter++;
    }
    public void resetOnResumeCounter(){
        this.onResumeCounter=0;
    }
}
