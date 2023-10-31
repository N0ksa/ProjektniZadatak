package hr.java.project.entities;

import java.util.List;
import java.util.Map;

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
}
