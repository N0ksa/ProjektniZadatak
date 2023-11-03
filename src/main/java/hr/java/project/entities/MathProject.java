package hr.java.project.entities;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MathProject extends NamedEntity {
    private String description;
    private Map<MathClub, List<Student>> collaborators;

    public MathProject(String name, String description, Map<MathClub, List<Student>> collaborators) {
        super(name);
        this.description = description;
        this.collaborators = collaborators;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<MathClub, List<Student>> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(Map<MathClub, List<Student>> collaborators) {
        this.collaborators = collaborators;
    }

    public boolean hasStudentCollaborator(Student studentToCheck){
        for (List <Student> student : collaborators.values()){
            if (student.contains(studentToCheck)){
                return true;
            }
        }

        return false;
    }

    public boolean hasMathCollaborator(MathClub mathClubToCheck){
        return collaborators.containsKey(mathClubToCheck);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MathProject that = (MathProject) o;
        return Objects.equals(description.toLowerCase(), that.description.toLowerCase())
                && Objects.equals(collaborators, that.collaborators);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, collaborators);
    }
}
