package hr.java.project.entities;

public class Professor extends NamedEntity {
    private String name;
    private String surname;
    private String professorId;
    private String email;

    public Professor(String name, String surname, String professorId, String email) {
        super(name);
        this.surname = surname;
        this.professorId = professorId;
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
