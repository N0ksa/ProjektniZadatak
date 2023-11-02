package hr.java.project.entities;

import java.math.BigDecimal;
import java.util.List;

public class MathClub extends NamedEntity implements Gradable {
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


    @Override
    public BigDecimal calculateScore(List<CompetitionResult> competitionsResults, Integer numberOfCollaborations){
        BigDecimal overallClubScore = BigDecimal.ZERO;

        BigDecimal numberOfStudentsWeight = new BigDecimal(0.2);
        BigDecimal numberOfCollaborationsWeight = new BigDecimal(0.5);
        BigDecimal competitionResultsWeight = new BigDecimal(0.3);

       Integer numberOfStudents = getNumberOfMembers();

        return overallClubScore.add(numberOfStudentsWeight.multiply(BigDecimal.valueOf(numberOfStudents)))
                .add(numberOfCollaborationsWeight.multiply(BigDecimal.valueOf(numberOfCollaborations)))
                .add(competitionResultsWeight.multiply(collectAllScoresFromCompetitions(competitionsResults)));


    }

    public Integer getNumberOfMembers (){
        return students.size();
    }

    private BigDecimal collectAllScoresFromCompetitions(List <CompetitionResult> studentsCompetitions){
        BigDecimal sumOfAllScores = BigDecimal.ZERO;
        for (CompetitionResult competition: studentsCompetitions){
            sumOfAllScores = sumOfAllScores.add(competition.score());
        }

        return sumOfAllScores;
    }


}
