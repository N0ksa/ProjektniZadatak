package hr.java.project.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Predstavlja matematičko natjecanje.
 */
public class Competition extends NamedEntity {

    private String description;
    private Adress adress;
    private LocalDateTime timeOfCompetition;
    private List <CompetitionResult> competitionResults;

    /**
     * Konstruktor za stvaranje nove instance matematičkog natjecanja.
     * @param name Naziv natjecanja.
     * @param description Opis natjecanja.
     * @param adress Adresa održavanja natjecanja.
     * @param timeOfCompetition Vrijeme održavanja natjecanja.
     * @param competitionResults Rezultati natjecatelja.
     */
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


    /**
     * Dohvaća rezultat natjecanja za određenog sudionika.
     * @param participant Student za kojeg se dohvaća rezultat natjecanja.
     * @return {@code Optional} koji sadrži rezultat natjecanja za sudionika ili prazan optional ako sudionik nije bio na natjecanju.
     */
    public Optional <CompetitionResult> getCompetitionResultForParticipant(Student participant){

        return competitionResults.stream()
                .filter(result -> result.participant().equals(participant))
                .findFirst();
    }

    /**
     * Provjerava je li određeni student sudionik ovog natjecanja.
     * @param participantToCheck Student za provjeru sudjelovanja.
     * @return <code>true</code> ako je student sudionik ovog natjecanja, inače <code>false</code>.
     */
    public boolean hasParticipant(Student participantToCheck){
        for (CompetitionResult competition : competitionResults){
            if (competition.participant().equals(participantToCheck)){
                return true;
            }
        }
        return false;
    }

    /**
     * Vraća pobjednika natjecanja.
     * @return Student - pobjednik natjecanja.
     */
    public Optional <Student> findWinner(){

       return competitionResults.stream()
               .max(Comparator.comparing(CompetitionResult::score))
               .map(CompetitionResult::participant);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Competition that = (Competition) o;
        return Objects.equals(description.toLowerCase(), that.description.toLowerCase())
                && Objects.equals(adress, that.adress)
                && Objects.equals(timeOfCompetition, that.timeOfCompetition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description, adress, timeOfCompetition, competitionResults);
    }
}
