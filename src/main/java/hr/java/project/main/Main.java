package hr.java.project.main;

import hr.java.project.entities.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static Integer MAX_NUMBER_OF_STUDENTS = 3;
    private static Integer MAX_NUMBER_OF_PROFESSORS = 2;
    private static Integer MAX_NUMBER_OF_MATH_CLUBS = 2;
    private static Integer MAX_NUMBER_OF_MATH_PROJECTS = 2;

    private static final String VALID_POSTAL_CODE_REGEX = "[0-9]+";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        List<Student> students = collectStudentsFromUser(input);
        List<Professor> professors = collectProfessorsFromUser(input);
        List<MathClub> mathClubs = collectMathClubsFromUser(input, students);
        List <MathProject> mathProjects = collectMathProjectsFromUser(input, mathClubs);

        printStudentWithLongestMembership(students);


    }


    private static List<Student> collectStudentsFromUser(Scanner input) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < MAX_NUMBER_OF_STUDENTS; i++) {
            System.out.printf("Molimo unesite %d. studenta:\n", i + 1);
            students.add(createStudent(input));
        }
        return students;
    }

    private static Student createStudent (Scanner input) {
        System.out.print("Unesi ime studenta: ");
        String studentName = input.nextLine();

        System.out.print("Unesi prezime studenta: ");
        String studentSurname = input.nextLine();

        System.out.print("Unesi JMBAG studenta: ");
        String studentID = input.nextLine();

        System.out.print("Unesi email studenta: ");
        String studentEmail = input.nextLine();

        System.out.print("Unesi godinu studija studenta: ");
        int yearOfStudy = input.nextInt();
        input.nextLine();

        Student newStudent = new Student(studentName, studentSurname, studentID, studentEmail, yearOfStudy);

        int choice;
        do {
            System.out.println("Da li je student član matematičkog kluba?");
            System.out.println("1-Da\n2-Ne");
            choice = input.nextInt();
            input.nextLine();

            if (choice == 1) {

                ClubMembership clubMembership = createClubMembership(input);
                newStudent.setClubMembership(clubMembership);

            }
            else if (choice != 2) {
                System.out.println("Molimo unesite 1 za 'Da' ili 2 za 'Ne'.");
            }
        } while (choice != 1 && choice != 2);

        return newStudent;
    }

    private static ClubMembership createClubMembership(Scanner input) {
        System.out.println("Upišite datum učlanjivanja (dd.MM.yyyy.): ");
        String dateString = input.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        LocalDate joinDate = LocalDate.parse(dateString, formatter);

        System.out.println("Upišite broj članske iskaznice: ");
        String membershipId = input.nextLine();

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
        String professorEmail = input.nextLine();

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

    private static List<Student> selectStudents(Scanner input, List<Student> students) {
        List<Student> selectedStudents = new ArrayList<>();

        while (true) {
            List<Student> unselectedStudents = getUnselectedStudents(students, selectedStudents);

            System.out.println("Odaberite studente članove kluba:");

            for (int i = 0; i < unselectedStudents.size(); i++) {
                System.out.printf("%d. %s %s\n", i + 1, unselectedStudents.get(i).getName(),
                        unselectedStudents.get(i).getSurname());
            }

            System.out.print("Odaberite redni broj studenta (ili unesite 0 za završetak): ");
            int studentIndex = input.nextInt();

            if (studentIndex == 0) {
                if (selectedStudents.isEmpty()) {
                    System.out.println("Molimo odaberite barem jednog studenta.");
                } else {
                    break;
                }
            } else if (studentIndex > 0 && studentIndex <= unselectedStudents.size()) {
                selectedStudents.add(unselectedStudents.get(studentIndex - 1));
            } else {
                System.out.println("Unijeli ste krivi broj. Pokušajte ponovno!");
            }
        }

        return selectedStudents;
    }

    private static List<Student> getUnselectedStudents(List<Student> allStudents, List<Student> selectedStudents) {
        List<Student> unselectedStudents = new ArrayList<>();
        for (Student student : allStudents) {
            if (!selectedStudents.contains(student)) {
                unselectedStudents.add(student);
            }
        }
        return unselectedStudents;
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

        System.out.println("Odaberite klubove koji sudjeluju u projektu:");

        List<MathClub> selectedMathClubs = selectMathClubs(input, mathClubs);

        return new MathProject(projectName, projectDescription, selectedMathClubs);
    }


    private static List<MathClub> selectMathClubs(Scanner input, List<MathClub> mathClubs) {
        List<MathClub> selectedMathClubs = new ArrayList<>();
        int totalClubs = mathClubs.size();

        while (selectedMathClubs.size() < totalClubs) {
            List<MathClub> unselectedClubs = getUnselectedMathClubs(selectedMathClubs, mathClubs);

           for (int i = 0; i < unselectedClubs.size(); i++){
               System.out.printf("%d. %s\n", i + 1, unselectedClubs.get(i).getName());
           }

           int indexOfMathClub = input.nextInt();
           input.nextLine();

            if (indexOfMathClub == 0) {
                if (selectedMathClubs.isEmpty()) {
                    System.out.println("Molimo odaberite barem jedan matematički klub.");
                }
                else {
                    break;
                }
            }
            else if (indexOfMathClub > 0 && indexOfMathClub <= unselectedClubs.size()) {
                MathClub selectedClub = unselectedClubs.get(indexOfMathClub - 1);
                selectedMathClubs.add(selectedClub);
            }
            else {
                System.out.println("Neispravan odabir.");
            }
        }

        return selectedMathClubs;
    }

    private static List<MathClub> getUnselectedMathClubs(List<MathClub> selectedClubs, List<MathClub> allMathClubs) {
        List<MathClub> unselectedClubs = new ArrayList<>();

        for (MathClub club : allMathClubs) {
            if (!selectedClubs.contains(club)) {
                unselectedClubs.add(club);
            }
        }
        return unselectedClubs;
    }


    private static void printStudentWithLongestMembership(List<Student> students) {
        Student studentWithLongestMembership = findStudentWithLongestMembership(students);
        if (studentWithLongestMembership != null) {
            System.out.println("Student koji ima najduže članstvo u studentskom klubu je:");
            System.out.printf("%s %s %s"
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
        String postalCode = enterValidPostalCode(input);


        return adressBuilder.setHouseNumber(houseNumber)
                .setCity(cityName)
                .setPostalCode(postalCode)
                .build();

    }

    private static String enterValidPostalCode(Scanner input){
        Pattern pattern = Pattern.compile(VALID_POSTAL_CODE_REGEX);

        Matcher matcher;
        Boolean validPostalCode = Boolean.TRUE;
        String postalCode;
        do{
            validPostalCode = Boolean.TRUE;
            postalCode = input.nextLine();
            matcher = pattern.matcher(postalCode);
            if (!matcher.matches()){
                System.out.print("\tMolim unesite poštanski broj u ispravnom formatu (samo su brojevi dopušteni): ");
                validPostalCode = Boolean.FALSE;
            }
        }while(!validPostalCode);

        return postalCode;
    }
}
