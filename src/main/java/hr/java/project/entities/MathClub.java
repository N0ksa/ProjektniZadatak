package hr.java.project.entities;

import java.util.List;

public class MathClub extends NamedEntity {
    private String name;
    private Adress adress;
    private List<Student> students;

    public MathClub(String name, Adress adress, List<Student> students) {
        super(name);
        this.adress = adress;
        this.students = students;
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
