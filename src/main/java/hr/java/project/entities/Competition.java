package hr.java.project.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.jar.Attributes;

public class Competition extends NamedEntity {

    private String description;
    private LocalDateTime timeOfCompetition;
    private List <CompetitionResult> competitionResults;

    public Competition(String name, String description, LocalDateTime timeOfCompetition, List<CompetitionResult> competitionResults) {
        super(name);
        this.description = description;
        this.timeOfCompetition = timeOfCompetition;
        this.competitionResults = competitionResults;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimeOfCompetition() {
        return timeOfCompetition;
    }

    public void setTimeOfCompetition(LocalDateTime timeOfCompetition) {
        this.timeOfCompetition = timeOfCompetition;
    }

    public List<CompetitionResult> getCompetitionResults() {
        return competitionResults;
    }

    public void setCompetitionResults(List<CompetitionResult> competitionResults) {
        this.competitionResults = competitionResults;
    }
}
