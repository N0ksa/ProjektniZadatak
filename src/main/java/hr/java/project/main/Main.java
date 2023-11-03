package hr.java.project.main;

import hr.java.project.entities.*;
import hr.java.project.enums.YearOfStudy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.java.project.exception.DuplicateStudentException;
import hr.java.project.utility.SafeInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Integer MAX_NUMBER_OF_STUDENTS = 3;
    private static final Integer MAX_NUMBER_OF_PROFESSORS = 0;
    private static final Integer MAX_NUMBER_OF_MATH_CLUBS = 2;
    private static final Integer MAX_NUMBER_OF_MATH_PROJECTS = 2;
    private static final Integer MAX_NUMBER_OF_MATH_COMPETITIONS = 2;

    private static final Logger logger = LoggerFactory.getLogger(Main. class);

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        List<Student> students = collectStudentsFromUser(input);
        List<Professor> professors = collectProfessorsFromUser(input);
        List<MathClub> mathClubs = collectMathClubsFromUser(input, students);
        List <MathProject> mathProjects = collectMathProjectsFromUser(input, mathClubs);
        List<Competition> mathCompetitions  = collectMathCompetitionsFromUser(input, students);

        printStudentWithLongestMembership(students);
        printMathClubWithMostMembers(mathClubs);

        calculateAndPrintStudentResults(students, mathCompetitions, mathProjects);
        calculateAndPrintMathClubResults(mathClubs, mathCompetitions, mathProjects);


    }



    private static List<Student> collectStudentsFromUser(Scanner input) {
        List<Student> students = new ArrayList<>();
        boolean duplicateStudent = true;

        for (int i = 0; i < MAX_NUMBER_OF_STUDENTS; i++) {
            System.out.printf("Molimo unesite %d. studenta:\n", i + 1);

            do{
                duplicateStudent = true;
                try{
                    Student newStudent = createStudent(input, students);
                    students.add(newStudent);
                    duplicateStudent = false;
                }
                catch(DuplicateStudentException e){
                    System.out.println("Već postoji taj student! Molim pokušajte ponovno.");
                }
            }while(duplicateStudent);

        }
        return students;
    }

    private static Student createStudent (Scanner input, List<Student> existingStudents) throws DuplicateStudentException{
        System.out.print("Unesi ime studenta: ");
        String studentName = input.nextLine();

        System.out.print("Unesi prezime studenta: ");
        String studentSurname = input.nextLine();

        System.out.print("Unesi JMBAG studenta: ");
        String studentID = input.nextLine();

        System.out.print("Unesi email studenta: ");
        String studentEmail = SafeInput.enterValidWebAdress(input);

        System.out.print("Unesi godinu studija studenta: ");
        int yearOfStudy = SafeInput.secureCorrectIntegerInterval(input, x -> x >= 1 && x <= 3);

        Map<String, Integer> grades = collectSubjectsAndGrades(input, yearOfStudy);

        Student newStudent = new Student(studentName, studentSurname, studentID, studentEmail, yearOfStudy, grades);

        int choice;
        System.out.println("Da li je student član matematičkog kluba?");
        System.out.println("1-Da\n2-Ne");

        choice = SafeInput.secureCorrectIntegerInterval(input, x -> x >= 1 && x <= 2);
        ClubMembership clubMembership = (choice == 1) ? createClubMembership(input) : null;
        newStudent.setClubMembership(clubMembership);

        if (existingStudents.contains(newStudent)){
            throw new DuplicateStudentException("Unešen već postojeći student");
        }

        return newStudent;
    }

    private static Map<String, Integer> collectSubjectsAndGrades(Scanner input, int yearOfStudy){
        Map <String, Integer> grades = new HashMap<>();

        for (int currentYear = 1; currentYear <= yearOfStudy; currentYear++){
            System.out.printf("%d. godina:\n", currentYear);
            YearOfStudy year = collectYearOfStudy(currentYear);

            if (year != null){
                List<String> availableSubjects = year.getAvailableSubjects();
                for (String subject: availableSubjects){
                    System.out.printf("Unesite ocijenu iz '%s':", subject);
                    Integer subjectGrade = SafeInput.secureCorrectIntegerInterval(input, x -> x >= 1 && x <= 5);

                    grades.put(subject, subjectGrade);
                }
            }

        }

        return grades;
    }

    private static YearOfStudy collectYearOfStudy(int yearOfStudy){
        for (YearOfStudy year : YearOfStudy.values()){
            if (year.getYear() == yearOfStudy){
                return year;
            }
        }
        return null;
    }

    private static ClubMembership createClubMembership(Scanner input) {
        System.out.println("Upišite datum učlanjivanja (dd.MM.yyyy.): ");
        LocalDate joinDate = SafeInput.secureCorrectLocalDate(input);

        System.out.println("Upišite broj članske iskaznice: ");
        String membershipId = SafeInput.secureCorrectMemberId(input);

        return new ClubMembership(joinDate, membershipId);
    }

    private static List<Professor> collectProfessorsFromUser (Scanner input) {
        List<Professor> professors = new ArrayList<>();
        for (int i = 0; i < MAX_NUMBER_OF_PROFESSORS; i++) {
            System.out.printf("Molimo unesite %d. profesora:\n", i + 1);
            professors.add(createProfessor(input));
        }
        return professors;
    }

    private static Professor createProfessor(Scanner input){
        System.out.print("Unesi ime profesora: ");
        String professorName = input.nextLine();

        System.out.print("Unesi prezime profesora: ");
        String professorSurname = input.nextLine();

        System.out.print("Unesi JMBAG profesora: ");
        String professorId = input.nextLine();

        System.out.print("Unesi email profesora: ");
        String professorEmail = SafeInput.enterValidWebAdress(input);

        return new Professor(professorName, professorSurname, professorId, professorEmail);

    }


    private static List<MathClub> collectMathClubsFromUser(Scanner input, List<Student> students) {
        List<MathClub> mathClubs = new ArrayList<>();
        for (int i = 0; i < MAX_NUMBER_OF_MATH_CLUBS; i++) {
            System.out.printf("Molimo unesite %d. matematički klub:\n", i + 1);
            mathClubs.add(createMathClub(input, students));
        }
        return mathClubs;
    }

    private static MathClub createMathClub(Scanner input, List<Student> students){
        System.out.print("Upišite ime kluba: ");
        String clubName = input.nextLine();

        System.out.println("Upišite informacije o adresi kluba");
        Adress adress = enterAdress(input);

        List<Student> selectedStudents = selectStudents(input, students);

        return new MathClub(clubName, adress, selectedStudents);

    }

    private static List<Competition> collectMathCompetitionsFromUser(Scanner input, List<Student> students) {
        List<Competition> mathCompetitions = new ArrayList<>();
        for (int i = 0; i < MAX_NUMBER_OF_MATH_COMPETITIONS; i++) {
            System.out.printf("Molimo unesite %d. matematičko natjecanje:\n", i + 1);
            mathCompetitions.add(createMathCompetition(input, students));
        }
        return mathCompetitions;
    }

    private static Competition createMathCompetition(Scanner input, List<Student> students) {
        System.out.print("Upišite ime natjecanja: ");
        String competitionName = input.nextLine();

        System.out.print("Upišite opis natjecanja: ");
        String competitionDescription = input.nextLine();

        System.out.print("Upišite vrijeme natjecanja (dd.MM.yyyy HH:mm:ss): ");
        LocalDateTime timeOfCompetition = SafeInput.secureCorrectLocalDateTime(input);

        System.out.println("Upišite informacije o adresi natjecanja");
        Adress competitionAdress = enterAdress(input);

        List<CompetitionResult> competitionResults = selectCompetitionResults(input, students);

        return new Competition(competitionName, competitionDescription, competitionAdress, timeOfCompetition, competitionResults);
    }

    private static List<CompetitionResult> selectCompetitionResults(Scanner input, List<Student> students) {
        List<CompetitionResult> competitionResults = new ArrayList<>();

        List<Student> unselectedStudents = new ArrayList<>(students);

        System.out.println("Odaberite studente:");

        while (!unselectedStudents.isEmpty()) {
            for (int i = 0; i < unselectedStudents.size(); i++) {
                System.out.printf("%d. %s %s\n", i + 1, unselectedStudents.get(i).getName(),
                        unselectedStudents.get(i).getSurname());
            }

            System.out.print("Odaberite redni broj studenta (ili unesite 0 za završetak): ");
            int studentIndex = SafeInput.secureCorrectIntegerInterval(input, x -> x >= 0 && x <= unselectedStudents.size());

            if (studentIndex == 0) {
                if (competitionResults.isEmpty()) {
                    System.out.println("Morate odabrati barem jednog studenta.");
                }
                else {
                    break;
                }
            }
            else if (studentIndex > 0 && studentIndex <= unselectedStudents.size()) {
                Student student = unselectedStudents.remove(studentIndex - 1);

                System.out.print("Unesite rezultat natjecatelja: ");
                BigDecimal participantScore = SafeInput.secureCorrectBigDecimalInterval(input,
                        (BigDecimal x) -> (x.compareTo(BigDecimal.ZERO) > 0) &&
                                (x.compareTo(BigDecimal.valueOf(100)) <= 0));

                CompetitionResult participantResult = new CompetitionResult(student, participantScore);
                competitionResults.add(participantResult);
            }
        }

        return competitionResults;
    }

    private static List<Student> selectStudents(Scanner input, List<Student> students) {
        List<Student> unselectedStudents = new ArrayList<>(students);
        List<Student> selectedStudents = new ArrayList<>();

        System.out.println("Odaberite studente članove kluba:");

        while (!unselectedStudents.isEmpty()) {
            for (int i = 0; i < unselectedStudents.size(); i++) {
                System.out.printf("%d. %s %s\n", i + 1, unselectedStudents.get(i).getName(), unselectedStudents.get(i).getSurname());
            }

            System.out.print("Odaberite redni broj studenta (ili unesite 0 za završetak): ");
            int studentIndex = SafeInput.secureCorrectIntegerInterval(input, x -> x >= 0 && x <= unselectedStudents.size());

            if (studentIndex == 0) {
                if (selectedStudents.isEmpty()) {
                    System.out.println("Molimo odaberite barem jednog studenta.");
                }
                else {
                    break;
                }
            }
            else {
                selectedStudents.add(unselectedStudents.remove(studentIndex - 1));
            }
        }

        return selectedStudents;
    }

    private static List<MathProject> collectMathProjectsFromUser(Scanner input, List<MathClub> mathClubs) {
        List<MathProject> mathProjects = new ArrayList<>();
        for (int i = 0; i < MAX_NUMBER_OF_MATH_PROJECTS; i++) {
            System.out.printf("Molimo unesite %d. projekt\n", i + 1);
            mathProjects.add(createProject(input, mathClubs));
        }
        return mathProjects;
    }


    private static MathProject createProject(Scanner input, List<MathClub> mathClubs) {
        System.out.print("Unesite ime projekta: ");
        String projectName = input.nextLine();

        System.out.print("Unesite opis projekta: ");
        String projectDescription = input.nextLine();

        System.out.println("Odaberite klubove koji sudjeluju u projektu (ili unesite 0 za završetak):");

        Map<MathClub, List <Student>> collaborators = selectCollaborators(input, mathClubs);

        return new MathProject(projectName, projectDescription, collaborators);
    }



    private static Map<MathClub, List<Student>> selectCollaborators(Scanner input, List<MathClub> mathClubs) {
        Map<MathClub, List<Student>> collaborators = new HashMap<>();
        List<MathClub> unselectedClubs = new ArrayList<>(mathClubs);

        while (!unselectedClubs.isEmpty()) {
            System.out.println("Dostupni matematički klubovi:");
            for (int i = 0; i < unselectedClubs.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, unselectedClubs.get(i).getName());
            }

            System.out.print("Odaberi matematički klub (0 za završetak): ");
            int indexOfMathClub = SafeInput.secureCorrectIntegerInterval(input, x -> x >= 0 && x <= unselectedClubs.size());

            if (indexOfMathClub == 0) {
                if (collaborators.isEmpty()) {
                    System.out.println("Mora biti odabran barem jedan matematički klub.");
                }
                else {
                    break;
                }
            }
            else{
                MathClub selectedClub = unselectedClubs.remove(indexOfMathClub - 1);
                List<Student> collaboratingStudents = selectCollaboratingStudents(input, selectedClub);
                collaborators.put(selectedClub, collaboratingStudents);
            }
        }

        return collaborators;
    }

    private static List<Student> selectCollaboratingStudents(Scanner input, MathClub mathClub) {
        List<Student> collaboratingStudents = new ArrayList<>();
        List<Student> unselectedStudents = new ArrayList<>(mathClub.getStudents());

        System.out.println("Dostupni studenti:");

        while (!unselectedStudents.isEmpty()) {
            for (int i = 0; i < unselectedStudents.size(); i++) {
                System.out.printf("%d. %s %s\n", i + 1, unselectedStudents.get(i).getName(), unselectedStudents.get(i).getSurname());
            }

            System.out.print("Odaberite studenta (0 za završetak unosa): ");
            int selectedStudentNumber = SafeInput.secureCorrectIntegerInterval(input, x -> x >= 0 && x <= unselectedStudents.size());

            if (selectedStudentNumber == 0) {
                if (collaboratingStudents.isEmpty()) {
                    System.out.println("Morate odabrati barem jednog studenta.");
                }
                else {
                    break;
                }
            } else {
                int studentIndex = selectedStudentNumber - 1;
                Student selectedStudent = unselectedStudents.remove(studentIndex);
                collaboratingStudents.add(selectedStudent);
            }
        }

        return collaboratingStudents;
    }

    private static Adress enterAdress(Scanner input) {
        System.out.println("\tInformacije o adresi:");
        System.out.print("\tUnesite ulicu: ");
        String streetName = input.nextLine();
        Adress.AdressBuilder adressBuilder = new Adress.AdressBuilder(streetName);

        System.out.print("\tUnesite kućni broj: ");
        String houseNumber = input.nextLine();

        System.out.print("\tUnesite grad: ");
        String cityName = input.nextLine();

        System.out.print("\tUnesite poštanski broj: ");
        String postalCode = SafeInput.enterValidPostalCode(input);


        return adressBuilder.setHouseNumber(houseNumber)
                .setCity(cityName)
                .setPostalCode(postalCode)
                .build();

    }

    private static void printStudentWithLongestMembership(List<Student> students) {
        Student studentWithLongestMembership = findStudentWithLongestMembership(students);
        if (studentWithLongestMembership != null) {
            System.out.println("Student koji ima najduže članstvo u studentskom klubu je:");
            System.out.printf("%s %s %s\n"
                    ,studentWithLongestMembership.getName()
                    ,studentWithLongestMembership.getSurname()
                    ,studentWithLongestMembership.getStudentId());
        } else {
            System.out.println("Ne postoji registriran student koji je član studentskog kluba.");
        }
    }

    private static Student findStudentWithLongestMembership(List<Student> students) {
        LocalDate longestJoinDate = null;
        Student studentWithLongestMembership = null;

        for (Student student : students) {
            ClubMembership clubMembership = student.getClubMembership();

            if (clubMembership != null) {
                LocalDate joinDate = clubMembership.getJoinDate();

                if (longestJoinDate == null || joinDate.isAfter(longestJoinDate)) {
                    longestJoinDate = joinDate;
                    studentWithLongestMembership = student;
                }
            }
        }

        return studentWithLongestMembership;
    }

    private static void printMathClubWithMostMembers(List<MathClub> mathClubs){
        MathClub mathClubWithMostMembers = findMathClubWithMostMembers(mathClubs);
        if (mathClubWithMostMembers != null){
            System.out.println("Studentski klub koji ima najviše članova je:");
            System.out.printf("%s %s\n", mathClubWithMostMembers.getName(),
                    mathClubWithMostMembers.getAdress());
        }
    }

    private static MathClub findMathClubWithMostMembers(List<MathClub> mathClubs){
        MathClub mathClubWithMostMembers = null;
        Integer maxNumberOfMembers = Integer.MIN_VALUE;
        for (MathClub mathClub : mathClubs){
            if(maxNumberOfMembers < mathClub.getStudents().size()){
                maxNumberOfMembers = mathClub.getStudents().size();
                mathClubWithMostMembers = mathClub;
            }

        }

        return mathClubWithMostMembers;
    }

    private static void calculateAndPrintStudentResults(List<Student> students, List<Competition> mathCompetitions, List<MathProject> mathProjects) {
        for (Student student : students){
            BigDecimal overallScore = student.calculateScore(getCompetitionResultsForStudent(student, mathCompetitions),
                    countParticipationsInProjectsForStudent(student, mathProjects));

            System.out.printf("Student %s %s skupio je sveukupno %.2f bodova\n", student.getName(), student.getSurname(),
                    overallScore);
        }
    }

    private static List<CompetitionResult> getCompetitionResultsForStudent(Student participant, List<Competition> competitions){
        List<CompetitionResult> competitionsResults = new ArrayList<>();
        for (Competition competition: competitions){
            if (competition.hasParticipant(participant)){
                competitionsResults.add(competition.getCompetitionResultsForParticipant(participant));
            }
        }

        return competitionsResults;
    }

    private static Integer countParticipationsInProjectsForStudent(Student participant, List<MathProject> projects){
        int numberOfParticipations = 0;
        for (MathProject project : projects){
            if(project.hasStudentCollaborator(participant)){
                numberOfParticipations++;
            }

        }

        return numberOfParticipations;
    }

    private static List<CompetitionResult> getCompetitionResultsForMathClub(MathClub mathClub, List<Competition> competitions){
        List <Student> mathClubStudents = mathClub.getStudents();
        List<CompetitionResult> competitionsResults = new ArrayList<>();

        for (Student student : mathClubStudents){
            for (Competition competition: competitions){
                if (competition.hasParticipant(student)){
                    competitionsResults.add(competition.getCompetitionResultsForParticipant(student));
                }
            }
        }


        return competitionsResults;
    }

    private static Integer countParticipationsInProjectsForMathClub(MathClub participant, List<MathProject> projects){
        int numberOfParticipations = 0;
        for (MathProject project : projects){
            if(project.hasMathCollaborator(participant)){
                numberOfParticipations++;
            }

        }

        return numberOfParticipations;
    }

    private static void calculateAndPrintMathClubResults(List<MathClub> clubs, List<Competition> mathCompetitions, List<MathProject> mathProjects) {
        for (MathClub club : clubs){
            BigDecimal overallScore = club.calculateScore(getCompetitionResultsForMathClub(club, mathCompetitions),
                    countParticipationsInProjectsForMathClub(club, mathProjects));

            System.out.printf("Klub %s skupio je sveukupno %.2f bodova\n", club.getName(),
                    overallScore);
        }
    }

}
