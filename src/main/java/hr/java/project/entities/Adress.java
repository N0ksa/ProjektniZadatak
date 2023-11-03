package hr.java.project.entities;

import java.util.Objects;

/**
 * Predstavlja adresu koja se sastoji od ulice, broja, grada i poštanskog broja.
 */
public class Adress {
    private String street;
    private String houseNumber;
    private String city;
    private String postalCode;


    /**
     * Privatni konstruktor za stvaranje instance klase Adress.
     * @param street  Ulica
     * @param houseNumber Kućni broj
     * @param city Grad
     * @param postalCode Poštanski broj
     */
    private Adress(String street, String houseNumber, String city, String postalCode) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postalCode = postalCode;
    }


    /**
     * Unutarnja klasa za izgradnju objekata tipa {@link Adress}.
     */
    public static class AdressBuilder{
        private String street;
        private String houseNumber;
        private String city;
        private String postalCode;

        /**
         * Konstruktor koji postavlja ulicu.
         * @param street Ulica.
         */
        public AdressBuilder(String street){
            this.street = street;
        }

        /**
         * Postavlja kućni broj.
         * @param houseNumber Kućni broj.
         * @return AdressBuilder - trenutni AdresBuilder za daljnje postavljanje atributa.
         */
        public AdressBuilder setHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        /**
         * Postavlja grad.
         * @param city Grad.
         * @return AdressBuilder - trenutni AdresBuilder za daljnje postavljanje atributa.
         */
        public AdressBuilder setCity(String city) {
            this.city = city;
            return this;
        }


        /**
         * Postavlja poštanski broj.
         * @param postalCode Poštanski broj.
         * @return AdressBuilder - trenutni AdresBuilder za daljnje postavljanje atributa.
         */
        public AdressBuilder setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        /**
         * Generira objekt tipa {@link Adress}.
         * @return Generiran objekt tipa {@link Adress}.
         */
        public Adress build(){
            return new Adress(street, houseNumber, city, postalCode);
        }


    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adress adress = (Adress) o;
        return Objects.equals(street.toLowerCase(), adress.street.toLowerCase())
                && Objects.equals(houseNumber.toLowerCase(), adress.houseNumber.toLowerCase())
                && Objects.equals(city.toLowerCase(), adress.city.toLowerCase())
                && Objects.equals(postalCode.toLowerCase(), adress.postalCode.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, houseNumber, city, postalCode);
    }

    @Override
    public String toString() {
        return getStreet() + " " + getHouseNumber() + ", " + getCity() + " " + getPostalCode();
    }
}
