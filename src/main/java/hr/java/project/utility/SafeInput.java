package hr.java.project.utility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.java.project.exception.InputPredicateException;
import hr.java.project.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SafeInput {
    private static final String VALID_WEB_ADDRESS_REGEX = "www\\.[A-Za-z0-9]+\\.[A-Za-z]+";
    private static final String VALID_POSTAL_CODE_REGEX = "[0-9]+";
    private static final String VALID_LOCAL_DATE_REGEX = "dd.MM.yyyy.";
    private static final String VALID_LOCAL_DATE_TIME_REGEX = "dd.MM.yyyy HH:mm:ss";

    private static final String VALID_MEMBER_ID_REGEX = "\\d{5}";

    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    /**
     * Sigurno i pouzdano čita cijele brojeve iz korisničkog unosa dok se ne unese broj unutar određenog raspona.
     * @param input Scanner objekt kojim se učitavaju novi podaci.
     * @param range Predicate koji definira raspon ispravnih vrijednosti.
     * @return Integer - uneseni cijeli broj unutar navedenog raspona.
     */
    public static Integer secureCorrectIntegerInterval(Scanner input, Predicate<Integer> range){
        boolean validInput = true;
        Integer enteredInteger = null;
        do{
            try{
                enteredInteger = input.nextInt();
                validInput = true;

                if(!range.test(enteredInteger)) {
                    throwInputPredicateException();
                }

            }catch (InputMismatchException e){
                System.out.println("Niste upisali broj. Molim pokušajte ponovno.");
                validInput = false;
                logger.info(e.getMessage(), e);

            }catch (InputPredicateException e){
                System.out.println(e.getMessage());
                validInput = false;
                logger.info(e.getMessage(), e);

            }finally {
                input.nextLine();
            }

        }while(!validInput);


        return enteredInteger;
    }


    /**
     * Sigurno i pouzdano čita decimalne brojeve iz korisničkog unosa dok se ne unese broj unutar određenog raspona.
     * @param input Scanner objekt kojim se učitavaju novi podaci.
     * @param range Predicate koji definira raspon ispravnih vrijednosti.
     * @return BigDecimal - uneseni decimalni broj unutar navedenog raspona.
     */
    public static BigDecimal secureCorrectBigDecimalInterval(Scanner input, Predicate<BigDecimal> range){
        boolean validInput = true;
        BigDecimal enteredBigDecimal = null;
        do{
            try {
                enteredBigDecimal = input.nextBigDecimal();
                validInput = true;

                if (!range.test(enteredBigDecimal)) {
                    throwInputPredicateException();
                }

            }catch (InputMismatchException e){
                System.out.println("Niste upisali broj. Molim pokušajte ponovno.");
                validInput = false;
                logger.info(e.getMessage(), e);

            }catch (InputPredicateException e){
                System.out.println(e.getMessage());
                validInput = false;

            }finally {
                input.nextLine();
            }

        }while(!validInput);


        return enteredBigDecimal;
    }


    /**
     * Omogućuje korisniku unos web adrese u ispravnom formatu (www.[A-Za-z0-9].[A-Za-z]+).
     * @param input Scanner objekt kojim se učitavaju novi podaci.
     * @return String - unesena web adresa u ispravnom formatu.
     */
    public static String enterValidWebAdress(Scanner input) {
        Pattern pattern = Pattern.compile(VALID_WEB_ADDRESS_REGEX);

        Matcher matcher;
        boolean validWebAdress = true;
        String webAdress;

        do {
            validWebAdress = true;
            webAdress = input.nextLine();
            matcher = pattern.matcher(webAdress);

            if (!matcher.matches()) {
                System.out.print("\tMolim unesite web adresu u ispravnom formatu (www.[A-Za-z0-9].[A-Za-z]+): ");
                validWebAdress = false;
            }
        } while (!validWebAdress);

        return webAdress;
    }


    /**
     * Omogućuje korisniku unos ispravnog poštanskog broja.
     * @param input Scanner objekt kojim se učitavaju novi podaci.
     * @return String -  uneseni poštanski broj u ispravnom formatu.
     */
    public static String enterValidPostalCode(Scanner input){
        Pattern pattern = Pattern.compile(VALID_POSTAL_CODE_REGEX);

        Matcher matcher;
        boolean validPostalCode = true;
        String postalCode;
        do{
            validPostalCode = true;
            postalCode = input.nextLine();
            matcher = pattern.matcher(postalCode);
            if (!matcher.matches()){
                System.out.print("\tMolim unesite poštanski broj u ispravnom formatu (samo su brojevi dopušteni): ");
                validPostalCode = false;
            }
        }while(!validPostalCode);

        return postalCode;
    }

    /**
     * Omogućuje korisniku unos ispravnog datuma.
     * @param input Scanner objekt kojim se učitavaju novi podaci.
     * @return LocalDate - uneseni datum u ispravnom formatu.
     */
    public static LocalDate secureCorrectLocalDate(Scanner input){
        boolean validInput = false;
        LocalDate joinDate = null;

        do{
            String dateString = input.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(VALID_LOCAL_DATE_REGEX);
            validInput = true;

            try{
                 joinDate = LocalDate.parse(dateString, formatter);
            }
            catch(DateTimeParseException e){
                System.out.println("Niste unijeli datum u ispravnom formatu. Molim pokušajte ponovno.");
                logger.info("Neispravan format", e);
                validInput = false;
            }


        }while(!validInput);

        return joinDate;
    }


    /**
     * Omogućuje korisniku unos ispravnog datuma i vremena.
     * @param input Scanner objekt kojim se učitavaju novi podaci.
     * @return LocalDate - uneseni datum i vrijeme u ispravnom formatu.
     */
    public static LocalDateTime secureCorrectLocalDateTime(Scanner input){
        boolean validInput = false;
        LocalDateTime joinDate = null;

        do{
            String dateString = input.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(VALID_LOCAL_DATE_TIME_REGEX);
            validInput = true;

            try{
                joinDate = LocalDateTime.parse(dateString, formatter);
            }
            catch(DateTimeParseException e){
                System.out.println("Niste unijeli datum i vrijeme u ispravnom formatu. Molim pokušajte ponovno.");
                logger.info("Neispravan format datuma i vremena.", e);
                validInput = false;
            }


        }while(!validInput);

        return joinDate;
    }


    /**
     * Omogućuje korisniku unos ispravne članske iskaznice.
     * @param input Scanner objekt kojim se učitavaju novi podaci.
     * @return String - ispravan broj članske iskaznice.
     */
    public static String secureCorrectMemberId(Scanner input){
        Pattern pattern = Pattern.compile(VALID_MEMBER_ID_REGEX);

        Matcher matcher;
        String memberId;
        boolean validMemberId = false;

        do{
            validMemberId = true;
            memberId = input.nextLine();
            matcher = pattern.matcher(memberId);

            if (!matcher.matches()){
                System.out.println("Molim unesite broj članske iskaznice u ispravnom formatu (točno 5 brojeva bez slova)");
                validMemberId = false;
                logger.info("Neispravan format članske iskaznice.");

            }


        }while(!validMemberId);

        return memberId;
    }


    /**
     * Služi kao pomoćna metoda koja baca iznimku u slučaju kada korisnik ne unese broj u ispravnom rasponu.
     * @throws InputPredicateException  u slučaju kada korisnik unese broj koji nije u zadanom rasponu.
     */
    private static void throwInputPredicateException() throws InputPredicateException {
        throw new InputPredicateException("Nije unešen broj u ispravnom rasponu!");
    }

}
