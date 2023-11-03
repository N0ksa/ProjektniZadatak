package hr.java.project.entities;

import java.util.Objects;

/**
 * Predstavlja profesora.
 */

public class Professor extends NamedEntity {
    private String surname;
    private String professorId;
    private String email;

    /**
     * Konstruktor za stvaranje nove instance profesora".
     * @param name  Ime profesora.
     * @param surname  Prezime profesora.
     * @param professorId Identifikacijski broj profesora.
     * @param email Adresa elektroničke pošte profesora.
     */
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
        if (!super.equals(o)) return false;
        Professor professor = (Professor) o;
        return Objects.equals(surname, professor.surname) && Objects.equals(professorId, professor.professorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, professorId, email);
    }
}
