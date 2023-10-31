package hr.java.project.enums;

import java.util.Arrays;
import java.util.List;

public enum YearOfStudy {
    FIRST_YEAR(1, Arrays.asList("Elementarna matematika 1", "Linearna algebra 1",
                                    "Matematička analiza 1", "Programiranje 1",
                                    "Elementarna matematika 2", "Linearna algebra 2",
                                    "Matematička analiza 2", "Programiranje 2")),

    SECOND_YEAR(2, Arrays.asList("Diferencijalni račun funkcija više varijabli", "Diskretna matematika",
                                        "Strukture podataka i algoritmi", "Vjerojatnost", "Uvod u analizu podataka",
                                       "Integrali funkcija više varijabli", "Numerička matematika", "Teorija brojeva",
                                        "Računarski praktikum 1")),

    THIRD_YEAR (3, Arrays.asList("Algebarske strukture", "Obične diferencijalne jednadžbe", "Teorija skuppova",
            "Vektorski prostori", "Matematička logika", "Kompleksna analiza", "Mjera i integral", "Statistika",
            "Metode matematičke fizike"));


    Integer year;
    private List<String> availableSubjects;
    YearOfStudy(Integer year, List <String> availableSubjects){
        this.year = year;
        this.availableSubjects = availableSubjects;
    }
}