package hr.java.project.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.jar.Attributes;

public class Competition extends NamedEntity {

    private String description;
    private Adress adress;
    private LocalDateTime timeOfCompetition;
    private List <CompetitionResult> competitionResults;

    public Competition(String name, String description, Adress adress, LocalDateTime timeOfCompetition,
                       List<CompetitionResult> competitionResults) {
        super(name);
        this.description = description;
        this.adress = adress;
        this.timeOfCompetition = timeOfCompetition;
        this.competitionResults = competitionResults;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Adress getAdress() {
        return adress;
    }

    public void setAdress(Adress adress) {
        this.adress = adress;
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


    public CompetitionResult getCompetitionResultsForParticipant(Student participant){
        for (CompetitionResult competition: competitionResults){
            if (competition.participant().equals(participant)){
                return competition;
            }
        }

        return null;
    }

    public boolean hasParticipant(Student participantToCheck){
        for (CompetitionResult competition : competitionResults){
            if (competition.participant().equals(participantToCheck)){
                return true;
            }
        }
        return false;
    }

    public Student findWinner(){
        Student winner = null;
        BigDecimal winnerScore = null;
        for (CompetitionResult competition: competitionResults){
            BigDecimal score  = competition.score();
            if (winner == null || (winnerScore == null || score.compareTo(winnerScore) == 1)){
                winner = competition.participant();
            }
        }

        return winner;
    }
}
