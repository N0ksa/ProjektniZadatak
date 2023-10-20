package hr.java.project.entities;

import java.util.List;

public class StudentClub {
    private String name;
    private String description;
    private List<Student> members;
    private List<Professor> professors;

    public StudentClub(String name, String description, List <Student> members, List <Professor> professors) {
        this.name = name;
        this.description = description;
        this.members = members;
        this.professors = professors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Student> getMembers() {
        return members;
    }

    public void setMembers(List<Student> members) {
        this.members = members;
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(List <Professor> professors) {
        this.professors = professors;
    }
}
