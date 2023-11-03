package hr.java.project.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Student extends NamedEntity implements Gradable{
    private String surname;
    private String studentId;
    private String email;
    private Integer yearOfStudy;
    private Map<String, Integer> grades;

    private ClubMembership clubMembership;

    public Student(String name, String surname, String studentId, String email, Integer yearOfStudy, Map<String,
            Integer> grades) {
        super(name);
        this.surname = surname;
        this.studentId = studentId;
        this.email = email;
        this.yearOfStudy = yearOfStudy;
        this.grades = grades;
        this.clubMembership = clubMembership;
    }


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(Integer yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public Map<String, Integer> getGrades() {
        return grades;
    }

    public void setGrades(Map<String, Integer> grades) {
        this.grades = grades;
    }

    public ClubMembership getClubMembership() {
        return clubMembership;
    }

    public void setClubMembership(ClubMembership clubMembership) {
        this.clubMembership = clubMembership;
    }


    public BigDecimal calculateAverageGrade(){
        int counter = 0;
        BigDecimal sumOfGrades = new BigDecimal(0);

        for (Integer grade : grades.values()){
            if (grade == 1){
                return BigDecimal.ONE;
            }
            sumOfGrades = sumOfGrades.add(BigDecimal.valueOf(grade));
            counter++;
        }

        if (counter == 0){
            return BigDecimal.ZERO;
        }

        return sumOfGrades.divide(BigDecimal.valueOf(counter), 2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateScore(List<CompetitionResult> competitionsResults, Integer numberOfCollaborations) {

        BigDecimal averageGradeWeight = new BigDecimal(0.2);
        BigDecimal numberOfCollaborationsWeight = new BigDecimal(0.3);
        BigDecimal competitionResultsWeight = new BigDecimal(0.5);

        BigDecimal normalizedAverageGrade = calculateAverageGrade().multiply(BigDecimal.TWO);
        BigDecimal scoreFromAllCompetitions = collectAllScoresFromCompetitions(competitionsResults);

        return scoreFromAllCompetitions.multiply(competitionResultsWeight)
                .add(normalizedAverageGrade.multiply(averageGradeWeight))
                .add(numberOfCollaborationsWeight.multiply(new BigDecimal(numberOfCollaborations)));


    }

    private BigDecimal collectAllScoresFromCompetitions(List <CompetitionResult> studentCompetitions){
        BigDecimal sumOfAllScores = BigDecimal.ZERO;
        for (CompetitionResult competition: studentCompetitions){
            sumOfAllScores = sumOfAllScores.add(competition.score());
        }

        return sumOfAllScores;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(surname.toLowerCase(), student.surname.toLowerCase())
                && Objects.equals(studentId, student.studentId)
                && Objects.equals(email.toLowerCase(), student.email.toLowerCase())
                && Objects.equals(yearOfStudy, student.yearOfStudy)
                && Objects.equals(grades, student.grades)
                && Objects.equals(clubMembership, student.clubMembership);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, studentId, email, yearOfStudy, grades, clubMembership);
    }
}
