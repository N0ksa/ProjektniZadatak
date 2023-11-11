package hr.java.project.main;

import hr.java.project.entities.*;
import hr.java.project.enums.MaxLimit;
import hr.java.project.enums.YearOfStudy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import hr.java.project.exception.*;
import hr.java.project.sort.StudentSorter;
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
        Scanner input = new Scanner(System.in);

        List<Student> students = collectAndValidateStudentsFromUser(input);
        List<Professor> professors = collectAndValidateProfessorsFromUser(input);
        List<MathClub> mathClubs = collectAndValidateMathClubsFromUser(input, students);
        List <MathProject> mathProjects = collectAndValidateMathProjectsFromUser(input, mathClubs);
        List<Competition> mathCompetitions  = collectAndValidateMathCompetitionsFromUser(input, students);

        printStudentWithLongestMembership(students);
        printMathClubWithMostMembers(mathClubs);

        calculateAndPrintStudentResults(students, mathCompetitions, mathProjects);
        calculateAndPrintMathClubResults(mathClubs, mathCompetitions, mathProjects);

        sortStudentsBySurname(students);

    }


    /**
     *Učitava nove studente od korisnika i provjerava njihovu jedinstvenost.
     * @param input Scanner objekt kojim se učitavaju podaci.
     * @return List - lista studenata koji su uneseni i validirani.
     */
    private static List<Student> collectAndValidateStudentsFromUser(Scanner input) {
        List<Student> students = new ArrayList<>();
        boolean duplicateStudent = true;

        for (int i = 0; i < MaxLimit.MAX_NUMBER_OF_STUDENTS.getMaxNumber(); i++) {
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
                    logger.info(e.getMessage(), e);
                }
            }while(duplicateStudent);

        }
        return students;
    }

    /**
     *Služi za kreiranje novog studenta na temelju korisnikovog unosa.
     * @param input Scanner objekt kojim se učitavaju podaci.
     * @param existingStudents Lista studenata prema kojima se određuje da li je uneseni student duplikat.
     * @return Student -nova instanca studenta.
     * @throws DuplicateStudentException Baca iznimku ako je uneseni student duplikat već unesenog studenta.
     */
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

        System.out.println("Da li je student član matematičkog kluba?");
        System.out.println("1-Da\n2-Ne");

        int choice = SafeInput.secureCorrectIntegerInterval(input, x -> x >= 1 && x <= 2);
        ClubMembership clubMembership = (choice == 1) ? createClubMembership(input) : null;
        newStudent.setClubMembership(clubMembership);

        if (existingStudents.contains(newStudent)){
            throw new DuplicateStudentException("Unesen već postojeći student");
        }

        return newStudent;
    }

    /**
     * Služi za prikupljanje studentovih ocjena, u ovisnosti o njegovoj godini studija.
     * @param input Scanner objekt kojim se učitavaju podaci.
     * @param yearOfStudy Godina studija.
     * @return Map - sadrži predmete i njihove ocjene.
     */
    private static Map<String, Integer> collectSubjectsAndGrades(Scanner input, int yearOfStudy){
        Map <String, Integer> grades = new HashMap<>();

        for (int currentYear = 1; currentYear <= yearOfStudy; currentYear++){
            System.out.printf("%d. godina:\n", currentYear);
            Optional <YearOfStudy> year = collectYearOfStudy(currentYear);

            if (year.isPresent()){
                List<String> availableSubjects = year.get().getAvailableSubjects();
                for (String subject: availableSubjects){
                    System.out.printf("Unesite ocijenu iz '%s':", subject);
                    Integer subjectGrade = SafeInput.secureCorrectIntegerInterval(input, x -> x >= 1 && x <= 5);

                    grades.put(subject, subjectGrade);
                }
            }

        }

        return grades;
    }

    /**
     *Služi za dohvaćanje objekta godine studija na temelju broja godine.
     * @param yearOfStudy Godina studija koja se želi dohvatiti.
     * @return Optional koji sadrži objekt {@code YearOfStudy} ako postoji navedena godina, inače prazan {@code Optional}.
     */
    private static Optional <YearOfStudy> collectYearOfStudy(int yearOfStudy){
        for (YearOfStudy year : YearOfStudy.values()){
            if (year.getYear() == yearOfStudy){
                return Optional.of(year);
            }
        }

       return Optional.empty();
    }


    /**
     * Služi za kreiranje klubskog članstva.
     * @param input Scanner objekt kojim se učitavaju podaci.
     * @return ClubMembership - klubsko članstvo.
     */
    private static ClubMembership createClubMembership(Scanner input) {
        System.out.println("Upišite datum učlanjivanja (dd.MM.yyyy.): ");
        LocalDate joinDate = SafeInput.secureCorrectLocalDate(input);

        System.out.println("Upišite broj članske iskaznice: ");
        String membershipId = SafeInput.secureCorrectMemberId(input);

        return new ClubMembership(joinDate, membershipId);
    }


    /**
     *Učitava nove profesore od korisnika i provjerava njihovu jedinstvenost.
     * @param input Scanner objekt kojim se učitavaju podaci.
     * @return List - lista profesora koji su uneseni i validirani.
     */
    private static List<Professor> collectAndValidateProfessorsFromUser(Scanner input) {
        List<Professor> professors = new ArrayList<>();
        boolean duplicateProfessor = true;

        for (int i = 0; i < MaxLimit.MAX_NUMBER_OF_PROFESSORS.getMaxNumber(); i++) {
            System.out.printf("Molimo unesite %d. profesora:\n", i + 1);

            do{
                duplicateProfessor = true;

                try{
                    Professor newProfessor = createProfessor(input, professors);
                    professors.add(newProfessor);
                    duplicateProfessor = false;
                }
                catch (DuplicateProfessorException e){
                    System.out.println("Već postoji taj profesor! Molim pokušajte ponovno.");
                    logger.info(e.getMessage(), e);
                }

            }while(duplicateProfessor);
        }
        return professors;
    }


    /**
     * Služi za kreiranje novog profesora na temelju korisnikovog unosa.
     * @param input  Scanner objekt kojim se učitavaju podaci.
     * @param existingProfessors Lista profesora prema kojima se određuje da li je uneseni profesor duplikat.
     * @return nova instanca profesora.
     * @throws DuplicateProfessorException Baca iznimku ako je uneseni profesor duplikat već unesenog profesora.
     */
    private static Professor createProfessor(Scanner input, List <Professor> existingProfessors)
            throws DuplicateProfessorException {

        System.out.print("Unesi ime profesora: ");
        String professorName = input.nextLine();

        System.out.print("Unesi prezime profesora: ");
        String professorSurname = input.nextLine();

        System.out.print("Unesi JMBAG profesora: ");
        String professorId = input.nextLine();

        System.out.print("Unesi email profesora: ");
        String professorEmail = SafeInput.enterValidWebAdress(input);

        Professor newProfessor = new Professor(professorName, professorSurname, professorId, professorEmail);

        if (existingProfessors.contains(newProfessor)){
            throw new DuplicateProfessorException("Unesen već postojeći profesor");
        }

        return newProfessor;


    }


    /**
     *Učitava nove matematičke klubove od korisnika i provjerava njihovu jedinstvenost.
     * @param input Scanner objekt kojim se učitavaju podaci.
     * @param students Lista studenata iz koje se biraju članovi matematičkog kluba.
     * @return List - lista matematičkih klubova koji su uneseni i validirani.
     */
    private static List<MathClub> collectAndValidateMathClubsFromUser(Scanner input, List<Student> students) {
        List<MathClub> mathClubs = new ArrayList<>();
        boolean duplicateMathClub = true;

        for (int i = 0; i < MaxLimit.MAX_NUMBER_OF_MATH_CLUBS.getMaxNumber(); i++) {
            System.out.printf("Molimo unesite %d. matematički klub:\n", i + 1);
            do {
                duplicateMathClub = true;
                try{
                    MathClub newMathClub = createMathClub(input, students, mathClubs);
                    mathClubs.add(newMathClub);
                    duplicateMathClub = false;
                }
                catch (DuplicateMathClubException e){
                    System.out.println("Već postoji taj matematički klub! Molim pokušajte ponovno.");
                    logger.info(e.getMessage(), e);
                }

            }while(duplicateMathClub);

            mathClubs.add(createMathClub(input, students, mathClubs));
        }
        return mathClubs;
    }


    /**
     * Služi za kreiranje novog matematičkog kluba na temelju korisnikovog unosa.
     * @param input Scanner objekt kojim se učitavaju podaci.
     * @param students Lista studenata iz koje se biraju članovi matematičkog kluba.
     * @param existingMathClubs Lista matematičkih klubova prema kojima se određuje da li je uneseni klub duplikat.
     * @return MathClub - novi matematički klub
     * @throws DuplicateMathClubException Baca iznimku ako je uneseni matematički klub duplikat već unesenog kluba.
     */
    private static MathClub createMathClub(Scanner input, List<Student> students, List<MathClub> existingMathClubs)
    throws DuplicateMathClubException{
        System.out.print("Upišite ime kluba: ");
        String clubName = input.nextLine();

        System.out.println("Upišite informacije o adresi kluba");
        Adress adress = enterAdress(input);

        List<Student> selectedStudents = selectStudents(input, students);


        MathClub newMathClub = new MathClub(clubName, adress, selectedStudents);

        if(existingMathClubs.contains(newMathClub)){
            throw new DuplicateMathClubException("Unesen već postojeći matematički klub");
        }

        return newMathClub;

    }

    /**
     * Učitava nova matematička natjecanja od korisnika i provjerava njihovu jedinstvenost.
     * @param input Scanner objekt kojim se učitavaju podaci.
     * @param students Lista studenata iz koje se biraju sudionici natjecanja.
     * @return List - lista matematičkih natjecanja koja su unešena i validirana.
     */

    private static List<Competition> collectAndValidateMathCompetitionsFromUser(Scanner input, List<Student> students) {
        List<Competition> mathCompetitions = new ArrayList<>();
        boolean duplicateMathCompetition = true;

        for (int i = 0; i < MaxLimit.MAX_NUMBER_OF_MATH_COMPETITIONS.getMaxNumber(); i++) {
            System.out.printf("Molimo unesite %d. matematičko natjecanje:\n", i + 1);
            do {
                duplicateMathCompetition = true;
                try{
                    Competition newMathCompetition = createMathCompetition(input, students, mathCompetitions);
                    duplicateMathCompetition = false;
                }
                catch(DuplicateMathCompetitionException e){
                    System.out.println("Uneseno već postojeće matematičko natjecanje! Molimo pokušajte ponovno.");
                    logger.info(e.getMessage(), e);
                }
            }while(duplicateMathCompetition);
        }
        return mathCompetitions;
    }

    /**
     * Služi za kreiranje novog matematičkog natjecanja na temelju korisnikovog unosa.
     * @param input Scanner objekt kojim se učitavaju podaci.
     * @param students Lista studenata iz koje se biraju sudionici natjecanja.
     * @param existingCompetitions Lista matematičkih natjecanja prema kojima se određuje da li je uneseno natjecanje duplikat.
     * @return Competition - novo matematičko natjecanje.
     * @throws DuplicateMathCompetitionException Baca iznimku ako je uneseno matematičko natjecanje duplikat već unesenog natjecanja.
     */
    private static Competition createMathCompetition(Scanner input, List<Student> students, List<Competition> existingCompetitions)
            throws  DuplicateMathCompetitionException {

        System.out.print("Upišite ime natjecanja: ");
        String competitionName = input.nextLine();

        System.out.print("Upišite opis natjecanja: ");
        String competitionDescription = input.nextLine();

        System.out.print("Upišite vrijeme natjecanja (dd.MM.yyyy HH:mm:ss): ");
        LocalDateTime timeOfCompetition = SafeInput.secureCorrectLocalDateTime(input);

        System.out.println("Upišite informacije o adresi natjecanja");
        Adress competitionAdress = enterAdress(input);

        List<CompetitionResult> competitionResults = selectCompetitionResults(input, students);

        Competition newMathCompetition = new Competition(competitionName, competitionDescription, competitionAdress,
                timeOfCompetition, competitionResults);

        if (existingCompetitions.contains(newMathCompetition)){
            throw new DuplicateMathCompetitionException("Uneseno već postojeće matematičko natjecanje");
        }

        return newMathCompetition;
    }


    /**
     * Omogućuje odabir studenata za natjecanje i unos njihovih rezultata.
     * @param input Scanner objekt kojim se učitavaju podaci.
     * @param students Lista studenata iz koje se biraju sudionici natjecanja.
     * @return List - lista rezultata natjecanja.
     */
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

    /**
     *Služi za odabir studenata.
     * @param input Scanner objekt kojim se učitavaju podaci.
     * @param students Lista studenata koje je moguće odabrati.
     * @return List - lista odabranih studenata.
     */
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

    /**
     * Učitava nove matematičke projekte od korisnika i provjerava njihovu jedinstvenost.
     * @param input Scanner objekt kojim se učitavaju podaci.
     * @param mathClubs  Lista matematičkih klubova prema kojoj se biraju sudionici u projektu.
     * @return List - lista matematičkih projekata koji su unešeni i validirani.
     */
    private static List<MathProject> collectAndValidateMathProjectsFromUser(Scanner input, List<MathClub> mathClubs) {
        List<MathProject> mathProjects = new ArrayList<>();
        boolean duplicateMathProject = true;

        for (int i = 0; i < MaxLimit.MAX_NUMBER_OF_MATH_PROJECTS.getMaxNumber(); i++) {
            System.out.printf("Molimo unesite %d. projekt\n", i + 1);

            do {
                duplicateMathProject = true;

                try{
                    MathProject newMathProject = createProject(input, mathClubs, mathProjects);
                    duplicateMathProject = false;
                }
                catch(DuplicateMathProjectException e){
                    System.out.println("Unesen već postojeći matematički projekt! Molim pokušajte ponovno.");
                    logger.info(e.getMessage(), e);
                }

            }while(duplicateMathProject);
        }
        return mathProjects;
    }

    /**
     * Služi za kreiranje novog matematičkog projekta na temelju korisnikovog unosa.
     * @param input Scanner objekt kojim se učitavaju podaci.
     * @param mathClubs Lista matematičkih klubova prema kojoj se biraju sudionici u projektu.
     * @param existingMathProjects Lista matematičkih projekata prema kojima se određuje da li je uneseni projekt duplikat.
     * @return MathProject - novi matematički projekt.
     * @throws DuplicateMathProjectException Baca iznimku ako je uneseni matematički projekt duplikat već unesenog projekta.
     */

    private static MathProject createProject(Scanner input, List<MathClub> mathClubs, List<MathProject> existingMathProjects)
    throws DuplicateMathProjectException{

        System.out.print("Unesite ime projekta: ");
        String projectName = input.nextLine();

        System.out.print("Unesite opis projekta: ");
        String projectDescription = input.nextLine();

        System.out.println("Odaberite klubove koji sudjeluju u projektu (ili unesite 0 za završetak):");

        Map<MathClub, List <Student>> collaborators = selectCollaborators(input, mathClubs);

        MathProject newMathProject = new MathProject(projectName, projectDescription, collaborators);

        if (existingMathProjects.contains(newMathProject)){
            throw new DuplicateMathProjectException("Unesen već postojeći matematički klub");
        }

        return newMathProject;
    }


    /**
     * Služi za odabir sudionika u matematičkom projektu.
     * @param input Scanner objekt kojim se učitavaju podaci.
     * @param mathClubs Lista matematičkih klubova prema kojoj se biraju sudionici u projektu.
     * @return Map - mapa koja sadrži odabrane klubove i njihove studente koji su sudjelovali u projektu.
     */
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

    /**
     * Služi za odabir studenata matematičkog kluba koji su sudjelovali na projektu.
     * @param input Scanner objekt kojim se učitavaju podaci.
     * @param mathClub Matematički klub koji sudjeluje u projektu.
     * @return List - lista studenata matematičkog kluba koji sudjeluju u projektu.
     */

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

    /**
     *Služi za unost ispravne adrese od strane korisnika.
     * @param input Scanner objekt kojim se učitavaju podaci.
     * @return Adress - nova adresa.
     */
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
                    ,studentWithLongestMembership.get().getStudentId());
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
                .filter(student -> student.getClubMembership() != null)
                .min(Comparator.comparing(student -> student.getClubMembership().getJoinDate()));
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
        students.forEach(student -> System.out.printf("%s %s %s\n", student.getSurname(), student.getName(), student.getStudentId()));


    }

}
