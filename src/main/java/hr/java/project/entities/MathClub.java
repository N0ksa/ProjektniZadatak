package hr.java.project.entities;

import java.util.List;

public class MathClub {
    private String name;
    private Adress adress;
    private List<Student> students;

    public MathClub(String name, Adress adress, List<Student> students) {
        this.name = name;
        this.adress = adress;
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Adress getAdress() {
        return adress;
    }

    public void setAdress(Adress adress) {
        this.adress = adress;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
