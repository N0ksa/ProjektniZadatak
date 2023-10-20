package hr.java.project.entities;

public class Professor {
    private String name;
    private String surname;
    private Adress adress;
    private String professorId;
    private String email;
    private ClubMembership clubMembership;

    public Professor(String name, String surname, Adress adress, String professorId, String email) {
        this.name = name;
        this.surname = surname;
        this.adress = adress;
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

    public Adress getAdress() {
        return adress;
    }

    public void setAdress(Adress adress) {
        this.adress = adress;
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

    public ClubMembership getClubMembership() {
        return clubMembership;
    }

    public void setClubMembership(ClubMembership clubMembership) {
        this.clubMembership = clubMembership;
    }
}
