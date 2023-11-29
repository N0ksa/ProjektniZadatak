package hr.java.project.utility;

import hr.java.project.entities.*;
import hr.java.project.enums.City;
import hr.java.project.enums.ValidationRegex;
import hr.java.project.enums.YearOfStudy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Pomoćna klasa koja služi za učitavanje podataka iz datoteka.
 */
public class FileReaderUtil {

    private static final String STUDENTS_FILE_NAME = "dat/students.txt";
    private static final String PROFESSORS_FILE_NAME = "dat/professors.txt";
    private static final String ADDRESSES_FILE_NAME = "dat/addresses.txt";
    private static final String STORES_FILE_NAME = "dat/stores.txt";
    private static final String MATH_CLUBS_FILE_NAME = "dat/math-clubs.txt";
    private static final Logger logger = LoggerFactory.getLogger(FileReaderUtil.class);


    /**
     * Čita studente iz datoteke i vraća ih kao listu objekata klase Student.
     *
     * @return Lista studenata učitanih iz datoteke.
     * @throws IOException Ako dođe do pogreške prilikom čitanja datoteke.
     */
    public static List<Student> getStudentsFromFile() {
        List<Student> students = new ArrayList<>();
        File studentsFile = new File(STUDENTS_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new FileReader(studentsFile))) {

            String line;
            while ((Optional.ofNullable(line = reader.readLine()).isPresent())) {

                String studentName = line;
                String studentSurname = reader.readLine();
                Long studentId = Long.parseLong(reader.readLine());
                String studentWebAddress = reader.readLine();
                Integer studentYearOfStudy = Integer.parseInt(reader.readLine());
                List<Integer> studentGrades = Stream.of(reader.readLine().split(","))
                        .map(grade -> Integer.parseInt(grade))
                        .collect(Collectors.toList());

                Map<String, Integer> grades = new HashMap<>();
                List<String> subjects = new ArrayList<>();

                switch (studentYearOfStudy) {
                    case 1 -> subjects = YearOfStudy.FIRST_YEAR.getAvailableSubjects();
                    case 2 -> subjects = YearOfStudy.SECOND_YEAR.getAvailableSubjects();
                    case 3 -> subjects = YearOfStudy.THIRD_YEAR.getAvailableSubjects();
                }

                for (int i = 0; i < subjects.size(); i++) {
                    grades.put(subjects.get(i), studentGrades.get(i));
                }

                String isMember = reader.readLine();
                String memberId;
                LocalDate joinDate;
                ClubMembership membership = null;

                if (isMember.equalsIgnoreCase("member")) {
                    memberId = reader.readLine();
                    joinDate = LocalDate.parse(reader.readLine(),
                            DateTimeFormatter.ofPattern(ValidationRegex.VALID_LOCAL_DATE_REGEX.getRegex()));
                    membership = new ClubMembership(memberId, joinDate);

                    reader.readLine();

                } else {
                    reader.readLine();
                }

                students.add(new Student(studentName, studentSurname, studentId, studentWebAddress,
                        studentYearOfStudy, grades, Optional.ofNullable(membership)));

            }

        } catch (IOException ex) {
            String message = "Dogodila se pogreška kod čitanja datoteke - + " + STORES_FILE_NAME;
            logger.error(message, ex);
            System.out.println(message);
        }

        return students;

    }


    /**
     * Čita artikle iz datoteke i vraća ih kao listu objekata klase Item.
     *
     * @return Lista artikala učitanih iz datoteke.
     * @throws IOException Ako dođe do pogreške prilikom čitanja datoteke.
     */
    public static List<Professor> getProfessorsFromFile() {
        File itemsFile = new File(PROFESSORS_FILE_NAME);
        List<Professor> professors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(itemsFile))) {
            String line;
            while ((Optional.ofNullable(line = reader.readLine()).isPresent())) {

                String professorName = line;
                String professorSurname = reader.readLine();
                Long professorId = Long.parseLong(reader.readLine());
                String professorWebAddress = reader.readLine();
                reader.readLine();


                professors.add(new Professor(professorId, professorName, professorSurname, professorWebAddress));
            }

        } catch (IOException ex) {
            String message = "Dogodila se pogreška kod čitanja datoteke - + " + PROFESSORS_FILE_NAME;
            logger.error(message, ex);
            System.out.println(message);
        }

        return professors;
    }


    /**
     * Čita adrese iz datoteke i vraća ih kao listu objekata klase Address.
     *
     * @return Lista adresa učitanih iz datoteke.
     */
    public static List<Address> getAddressesFromFile() {

        File categoriesFile = new File(ADDRESSES_FILE_NAME);
        List<Address> addresses = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(categoriesFile))) {

            String line;
            while ((Optional.ofNullable(line = reader.readLine()).isPresent())) {

                Long addressId = Long.parseLong(line);
                String streetName = reader.readLine();
                String houseNumber = reader.readLine();
                String cityName = reader.readLine();

                City city = City.getCityFromStringName(cityName);

                Address.AdressBuilder adressBuilder = new Address.AdressBuilder(city).setId(addressId)
                        .setStreet(streetName).setHouseNumber(houseNumber);


                addresses.add(adressBuilder.build());


            }

        } catch (IOException ex) {
            String message = "Dogodila se pogreška kod čitanja datoteke - + " + ADDRESSES_FILE_NAME;
            logger.error(message, ex);
            System.out.println(message);
        }

        return addresses;
    }

    /**
     * Čita studentske matematičke klubove iz datoteke i vraća ih kao listu objekata klase MathClub.
     *
     * @return Lista matematičkih klubova učitanih iz datoteke.
     */

    public static List<MathClub> getMathClubsFromFile(List <Student> students, List <Address> addresses) {
        List<MathClub> mathClubs = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(MATH_CLUBS_FILE_NAME))) {

            String line;
            while ((Optional.ofNullable(line = reader.readLine()).isPresent())) {

                Long mathClubId = Long.parseLong(line);
                String mathClubName = reader.readLine();
                Long addressId = Long.parseLong(reader.readLine());
                Optional <Address> mathClubAddress = addresses.stream()
                        .filter(address -> address.getAddressId().compareTo(addressId) == 0)
                        .findFirst();

                List<String> membersIdString = Arrays.asList(reader.readLine().trim().split(","));

                List <Long> membersId = membersIdString.stream()
                        .map(stringId -> Long.parseLong(stringId))
                        .collect(Collectors.toList());

                Set <Student> mathClubMembers = membersId.stream()
                        .map(id -> findStudentById(id, students))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet());

                mathClubAddress.ifPresent(address -> mathClubs.add(new MathClub(mathClubId, mathClubName, address, mathClubMembers)));

            }


        } catch (IOException ex) {
            String message = "Dogodila se pogreška kod čitanja datoteke - + " + MATH_CLUBS_FILE_NAME;
            logger.error(message, ex);
            System.out.println(message);
        }

        return mathClubs;
    }


    /**
     * Pronalazi studenta s određenim identifikacijskim brojem u zadanoj listi.
     * @param id Identifikacijski broj studenta kojeg treba pronaći.
     * @param students Lista studenata u kojoj treba tražiti.
     * @return {@code Optional} koji sadrži pronađeni artikl ili prazan {@code Optional} ako artikl nije pronađen.
     */
    public static Optional<Student> findStudentById(Long id, List<Student> students){
        return students.stream()
                .filter(student -> student.getId().compareTo(id) == 0)
                .findFirst();
    }

}