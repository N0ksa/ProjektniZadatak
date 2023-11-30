package hr.java.project.main;

import hr.java.project.entities.*;
import hr.java.project.enums.MaxLimit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import hr.java.project.exception.*;
import hr.java.project.sort.StudentSorter;
import hr.java.project.utility.FileReaderUtil;
import hr.java.project.utility.SafeInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Glavna klasa s metodom main i pomoćnim metodama.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main. class);

    /**
     *Početak programa.
     * @param args Argumenti iz komandne linije.
     */
    public static void main(String[] args) {

        List<Student> students = FileReaderUtil.getStudentsFromFile();
        List<Professor> professors = FileReaderUtil.getProfessorsFromFile();
        List <Address> addresses = FileReaderUtil.getAddressesFromFile();
        List<MathClub> mathClubs = FileReaderUtil.getMathClubsFromFile(students, addresses);
        List <MathProject> mathProjects = FileReaderUtil.getMathProjectsFromFile(mathClubs, students);
        List<Competition> mathCompetitions  = FileReaderUtil.getMathCompetitionsFromFile(students, addresses);

        printStudentWithLongestMembership(students);
        printMathClubWithMostMembers(mathClubs);

        calculateAndPrintStudentResults(students, mathCompetitions, mathProjects);
        calculateAndPrintMathClubResults(mathClubs, mathCompetitions, mathProjects);

        sortStudentsBySurname(students);

    }

    /**
     *Ispisuje studenta koji ima najduže članstvo u matematičkom klubu.
     * @param students Lista studenata prema kojoj se odabire student s najdužim članstvom.
     */
    private static void printStudentWithLongestMembership(List<Student> students) {
        Optional <Student> studentWithLongestMembership = findStudentWithLongestMembership(students);
        if (studentWithLongestMembership.isPresent()) {
            System.out.println("Student koji ima najduže članstvo u studentskom klubu je:");
            System.out.printf("%s %s %s\n"
                    ,studentWithLongestMembership.get().getName()
                    ,studentWithLongestMembership.get().getSurname()
                    ,studentWithLongestMembership.get().getId());
        } else {
            System.out.println("Ne postoji registriran student koji je član studentskog kluba.");
        }
    }

    /**
     * Služi za pronalazak studenta koji ima najduže članstvo u matematičkom klubu.
     * @param students Lista studenata prema kojoj se odabire student s najdužim članstvom.
     * @return {@code Optional} koji sadrži studenta s najdužim članstvom ili je prazan ako nijedan student nema članstvo.
     */
    private static Optional <Student> findStudentWithLongestMembership(List<Student> students) {

        return students.stream()
                .filter(student -> student.getClubMembership().isPresent())
                .min(Comparator.comparing(student -> student.getClubMembership().get().getJoinDate()));
    }


    /**
     * Ispisuje matematički klub s najviše članova.
     * @param mathClubs Lista matematičkih klubova prema kojoj se odabire matematički klub s najviše studenata.
     */
    private static void printMathClubWithMostMembers(List<MathClub> mathClubs){
        Optional <MathClub> mathClubWithMostMembers = findMathClubWithMostMembers(mathClubs);
        if (mathClubWithMostMembers.isPresent()){
            System.out.println("Studentski klub koji ima najviše članova je:");
            System.out.printf("%s %s\n", mathClubWithMostMembers.get().getName(),
                    mathClubWithMostMembers.get().getAddress());
        }
    }

    /**
     * Služi za pronalazak matematičkog kluba s najviše članova.
     * @param mathClubs Lista matematičkih klubova prema kojoj se odabire matematički klub s najviše studenata.
     * @return {@code Optional} koji sadrži matematički klub s najviše studenata ili je prazan ako nijedan klub nema studenta.
     */
    private static Optional <MathClub> findMathClubWithMostMembers(List<MathClub> mathClubs) {

        return mathClubs.stream()
                .max(Comparator.comparing(mathClub -> mathClub.getNumberOfMembers()));

    }

    /**
     * Izračunava i ispisuje sveukupne rezultate svih studenata.
     * @param students Lista studenata.
     * @param mathCompetitions Lista matematičkih natjecanja.
     * @param mathProjects  Lista matematičkih projekata.
     */
    private static void calculateAndPrintStudentResults(List<Student> students, List<Competition> mathCompetitions, List<MathProject> mathProjects) {
        for (Student student : students){
            BigDecimal overallScore = student.calculateScore(getCompetitionResultsForStudent(student, mathCompetitions),
                    countParticipationsInProjectsForStudent(student, mathProjects));

            System.out.printf("Student %s %s skupio je sveukupno %.2f bodova\n", student.getName(), student.getSurname(),
                    overallScore);
        }
    }


    /**
     * Služi za dobivanje liste svih rezultata matematičkih natjecanja na kojemu je student sudjelovao.
     * @param participant Student za kojeg se želi dobiti lista svih rezultata svih natjecanja.
     * @param competitions Lista svih matematičkih natjecanja.
     * @return List - lista svih rezultata matematičkih natjecanja na kojima je student sudjelovao.
     */
    private static List<CompetitionResult> getCompetitionResultsForStudent(Student participant, List<Competition> competitions){

        return competitions.stream()
                .filter(competition -> competition.hasParticipant(participant))
                .map(competition -> competition.getCompetitionResultForParticipant(participant).get())
                .collect(Collectors.toList());
    }

    /**
     * Služi za izračun broja sudjelovanja studenta na matematičkim projektima.
     * @param participant Student za kojeg se želi dobiti broj sudjelovanja na matematičkim projektima.
     * @param projects Lista svih matematičkih projekata.
     * @return Integer - broj sudjelovanja studenta na matematičkim projektima.
     */
    private static Integer countParticipationsInProjectsForStudent(Student participant, List<MathProject> projects){

        long numberOfParticipations = projects.stream()
                .filter(mathProject -> mathProject.hasStudentCollaborator(participant))
                .count();

        return (int) numberOfParticipations;
    }


    /**
     * Služi za dobivanje liste svih rezultata matematičkih natjecanja za sve članove matematičkog kluba.
     * @param mathClub Matematički klub za koji se želi dobiti lista svih rezultata svih natjecanja.
     * @param competitions Lista svih matematičkih natjecanja.
     * @return List - lista svih rezultata matematičkih natjecanja za sve članove matematičkog kluba.
     */
    private static List<CompetitionResult> getCompetitionResultsForMathClub(MathClub mathClub, List<Competition> competitions){

        return mathClub.getStudents().stream()
                .flatMap(student -> competitions.stream()
                        .filter(competition -> competition.hasParticipant(student))
                        .map(competition -> competition.getCompetitionResultForParticipant(student).get())
                )
                .collect(Collectors.toList());
    }


    /**
     * Služi za izračun broja sudjelovanja matematičkog kluba na matematičkim projektima.
     * @param participant Matematički klub za kojeg se želi dobiti broj sudjelovanja na matematičkim projektima.
     * @param projects Lista svih matematičkih projekata.
     * @return Integer - broj sudjelovanja matematičkog kluba na matematičkim projektima.
     */
    private static Integer countParticipationsInProjectsForMathClub(MathClub participant, List<MathProject> projects){

        long numberOfParticipations =  projects.stream()
                .filter(mathProject -> mathProject.hasMathCollaborator(participant))
                .count();

        return (int) numberOfParticipations;

    }


    /**
     * Izračunava i ispisuje sveukupne rezultate svih matematičkih klubova.
     * @param clubs Lista matematičkih klubova.
     * @param mathCompetitions Lista matematičkih natjecanja.
     * @param mathProjects Lista matematičkih projekata.
     */

    private static void calculateAndPrintMathClubResults(List<MathClub> clubs, List<Competition> mathCompetitions, List<MathProject> mathProjects) {

        for (MathClub club : clubs){
            BigDecimal overallScore = club.calculateScore(getCompetitionResultsForMathClub(club, mathCompetitions),
                    countParticipationsInProjectsForMathClub(club, mathProjects));

            System.out.printf("Klub %s skupio je sveukupno %.2f bodova\n", club.getName(),
                    overallScore);
        }
    }

    /**
     * Služi za sortiranje svih studenata prema njihovom prezimenu  i ispis na konzolu.
     * @param students Lista studenata koji se sortiraju prema prezimenu.
     */
    private static void sortStudentsBySurname(List <Student> students){
        students.sort(new StudentSorter(true));

        System.out.println("Popis svih studenata sortiranih prema prezimenu:");
        students.forEach(student -> System.out.printf("%s %s %s\n", student.getSurname(), student.getName(), student.getId()));


    }

}
