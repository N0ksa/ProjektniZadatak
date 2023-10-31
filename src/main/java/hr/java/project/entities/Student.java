package hr.java.project.entities;

import java.util.Map;

public class Student extends NamedEntity{
    private String surname;
    private String studentId;
    private String email;
    private Integer yearOfStudy;
    private Map<String, Integer> grades;

    private ClubMembership clubMembership;

    public Student(String name, String surname, String studentId, String email, Integer yearOfStudy, Map<String,
            Integer> grades) {
        super(name);
        this.surname = surname;
        this.studentId = studentId;
        this.email = email;
        this.yearOfStudy = yearOfStudy;
        this.grades = grades;
        this.clubMembership = clubMembership;
    }


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(Integer yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public Map<String, Integer> getGrades() {
        return grades;
    }

    public void setGrades(Map<String, Integer> grades) {
        this.grades = grades;
    }

    public ClubMembership getClubMembership() {
        return clubMembership;
    }

    public void setClubMembership(ClubMembership clubMembership) {
        this.clubMembership = clubMembership;
    }
}
