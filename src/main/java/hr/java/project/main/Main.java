package hr.java.project.main;

import hr.java.project.entities.*;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static Integer MAX_NUMBER_OF_STUDENTS = 3;
    private static Integer MAX_NUMBER_OF_PROFFESSORS = 3;
    private static Integer  MAX_NUMBER_OF_STUDENT_CLUBS = 2;

    private static final String VALID_POSTAL_CODE_REGEX = "[0-9]+";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        List<Student> students = new ArrayList<>();
        List<Professor> professors = new ArrayList<>();
        List<StudentClub> studentClubs = new ArrayList<>();

        for (int i = 0; i < MAX_NUMBER_OF_STUDENTS; i++){
            System.out.printf("Molimo unesite %d. studenta:\n", i + 1);
            students.add(enterStudent(input));
        }

        for (int i = 0; i < MAX_NUMBER_OF_PROFFESSORS; i++){
            System.out.printf("Molimo unesite %d. profesora:\n", i + 1);
            professors.add(enterProfessor(input));
        }

        for (int i = 0; i < MAX_NUMBER_OF_STUDENT_CLUBS; i++){
            System.out.printf("Molimo unesite %d. matematički klub:\n", i + 1);
            studentClubs.add(enterStudentClub(input, students, professors));
        }



    }

    private static Student enterStudent(Scanner input){
        System.out.print("Unesi ime studenta: ");
        String studentName = input.nextLine();

        System.out.print("Unesi prezime studenta: ");
        String studentSurname = input.nextLine();

        System.out.println("Unesi adresu studenta: ");
        Adress studentAdress = enterAdress(input);

        System.out.print("Unesi JMBAG studenta: ");
        String studentID = input.nextLine();

        System.out.print("Unesi email studenta: ");
        String studentEmail = input.nextLine();

        System.out.print("Unesi godinu studija studenta: ");
        Integer yearOfStudy = input.nextInt();
        input.nextLine();

        Student newStudent = new Student(studentName, studentSurname, studentAdress, studentID, studentEmail, yearOfStudy);

        System.out.println("Da li je student član matematičkog kluba?");
        System.out.println("1-Da\n2-Ne");
        Integer choice = input.nextInt();
        input.nextLine();

        if (choice == 1){
            System.out.println("Upišite datum učlanjivanja (dd.MM.yyyy.): ");
            String dateString = input.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
            LocalDate  joinDate = LocalDate.parse(dateString, formatter);;

            System.out.println("Upišite broj članske iskaznice: ");
            String membershipId = input.nextLine();

            ClubMembership clubMembership = new ClubMembership(joinDate, membershipId);
            newStudent.setClubMembership(clubMembership);
        }

        return newStudent;
    }

    private static Professor enterProfessor(Scanner input){
        System.out.print("Unesi ime profesora: ");
        String professorName = input.nextLine();

        System.out.print("Unesi prezime profesora: ");
        String professorSurname = input.nextLine();

        System.out.println("Unesi adresu profesora: ");
        Adress professorAdress = enterAdress(input);

        System.out.print("Unesi JMBAG profesora: ");
        String professorId = input.nextLine();

        System.out.print("Unesi email studenta: ");
        String professorEmail = input.nextLine();

        Professor newProfessor = new Professor(professorName, professorSurname, professorAdress, professorId, professorEmail);

        System.out.println("Da li je profesor član matematičkog kluba?");
        System.out.println("1-Da\n2-Ne");
        Integer choice = input.nextInt();
        input.nextLine();

        if (choice == 1){
            System.out.println("Upišite datum učlanjivanja (dd.MM.yyyy.): ");
            String dateString = input.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
            LocalDate  joinDate = LocalDate.parse(dateString, formatter);;

            System.out.println("Upišite broj članske iskaznice: ");
            String membershipId = input.nextLine();

            ClubMembership clubMembership = new ClubMembership(joinDate, membershipId);
            newProfessor.setClubMembership(clubMembership);
        }

        return newProfessor;
    }


    private static StudentClub enterStudentClub(Scanner input, List<Student> students, List<Professor> professors){
        System.out.print("Upišite ime kluba: ");
        String clubName = input.nextLine();

        System.out.print("Upišite opis kluba: ");
        String clubDescription = input.nextLine();

        System.out.println("Odaberite studente članove kluba:");
        for (int i = 0; i < students.size(); i++){
            System.out.printf("%d. %s %s\n", i + 1, students.get(i).getName(), students.get(i).getSurname());
        }

        List<Student> selectedStudents = new ArrayList<>();

        while(true){
            System.out.print("Odaberite redni broj studenta (ili unesite 0 za završetak): ");
            Integer  studentIndex = input.nextInt() - 1;

            if (studentIndex == -1){
                break;
            }
            if (studentIndex >= 0 && studentIndex < students.size()){
                selectedStudents.add(students.get(studentIndex));
            }
            else{
                System.out.println("Unijeli ste krivi broj. Pokušajte ponovno!");
            }
        }

        List<Professor> selectedProfessors = new ArrayList<>();
        System.out.println("Odaberite profesore članove kluba:");
        for (int i = 0; i < professors.size(); i++){
            System.out.printf("%d. %s %s\n", i + 1, professors.get(i).getName(), professors.get(i).getSurname());
        }

        while(true){
            System.out.print("Odaberite redni broj profesora (ili unesite 0 za završetak): ");
            Integer  professorIndex = input.nextInt() - 1;

            if (professorIndex == -1){
                break;
            }
            if (professorIndex >= 0 && professorIndex < professors.size()){
                selectedProfessors.add(professors.get(professorIndex));
            }
            else{
                System.out.println("Unijeli ste krivi broj. Pokušajte ponovno!");
            }
        }

        return new StudentClub(clubName, clubDescription, selectedStudents, selectedProfessors);

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
