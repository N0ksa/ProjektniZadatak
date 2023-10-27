package hr.java.project.entities;

import java.util.List;

public class MathProject extends NamedEntity {
    private String description;
    private List<MathClub> collaborators;

    public MathProject(String name, String description, List<MathClub> collaborators) {
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

    public List<MathClub> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<MathClub> collaborators) {
        this.collaborators = collaborators;
    }
}
