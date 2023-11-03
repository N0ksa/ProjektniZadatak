package hr.java.project.entities;

import java.util.Objects;

public class Professor extends NamedEntity {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return Objects.equals(surname.toLowerCase(), professor.surname.toLowerCase())
                && Objects.equals(professorId, professor.professorId)
                && Objects.equals(email.toLowerCase(), professor.email.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, professorId, email);
    }
}
