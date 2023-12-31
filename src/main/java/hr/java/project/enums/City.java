package hr.java.project.enums;

import hr.java.project.exception.NoSuchCityException;

import java.io.Serializable;

/**
 *Predstavlja različite gradove u Hrvatskoj s njihovim odgovarajućim poštanskim brojevima.
 */
public enum City implements Serializable {
    ZAGREB("Zagreb", "10000"),
    SPLIT("Split", "21000"),
    RIJEKA("Rijeka", "51000"),
    OSIJEK("Osijek", "31000"),
    ZADAR("Zadar", "23000"),
    PULA("Pula", "52100"),
    VARAZDIN("Varaždin", "42000"),
    DUBROVNIK("Dubrovnik", "20000"),
    SISAK("Sisak", "44000"),
    KARLOVAC("Karlovac", "47000"),
    VIROVITICA("Virovitica", "33000"),
    SLAVONSKI_BROD("Slavonski Brod", "35000"),
    VUKOVAR("Vukovar", "32000"),
    POREC("Poreč", "52440"),
    SLATINA("Slatina", "33520"),
    KRIZEVCI("Križevci", "48260"),
    KOPRIVNICA("Koprivnica", "48000");


    private String name;
    private String postalCode;
    City(String name, String postalCode){
        this.name = name;
        this.postalCode = postalCode;
    }

    public String getName() {
        return name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public static City getCityFromStringName(String cityName){
        for (City city: City.values()){
            if (city.getName().equalsIgnoreCase(cityName)){
                return city;
            }
        }
        throw new NoSuchCityException("Ne postoji " + cityName + " grad u našoj datoteci");
    }
    @Override
    public String toString() {
        return name + " " + postalCode;
    }
}
