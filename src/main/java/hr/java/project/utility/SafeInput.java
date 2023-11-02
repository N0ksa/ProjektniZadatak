package hr.java.project.utility;

import java.math.BigDecimal;
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
        Boolean validWebAdress = Boolean.TRUE;
        String webAdress;

        do {
            validWebAdress = Boolean.TRUE;
            webAdress = input.nextLine();
            matcher = pattern.matcher(webAdress);

            if (!matcher.matches()) {
                System.out.print("\tMolim unesite web adresu u ispravnom formatu (www.[A-Za-z0-9].[A-Za-z]+): ");
                validWebAdress = Boolean.FALSE;
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


    /**
     * Služi kao pomoćna metoda koja baca iznimku u slučaju kada korisnik ne unese broj u ispravnom rasponu.
     * @throws InputPredicateException  u slučaju kada korisnik unese broj koji nije u zadanom rasponu.
     */
    private static void throwInputPredicateException() throws InputPredicateException {
        throw new InputPredicateException("Nije unešen broj u ispravnom rasponu!");
    }

}
