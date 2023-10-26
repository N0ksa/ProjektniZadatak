package hr.java.project.entities;

import java.util.List;

public class MathProject {
    private String name;
    private String description;
    private List<MathClub> collaborators;

    public MathProject(String name, String description, List<MathClub> collaborators) {
        this.name = name;
        this.description = description;
        this.collaborators = collaborators;
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

    public List<MathClub> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<MathClub> collaborators) {
        this.collaborators = collaborators;
    }
}
