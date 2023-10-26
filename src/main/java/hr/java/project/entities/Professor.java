package hr.java.project.entities;

public class Professor {
    private String name;
    private String surname;
    private String professorId;
    private String email;

    public Professor(String name, String surname, String professorId, String email) {
        this.name = name;
        this.surname = surname;
        this.professorId = professorId;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
