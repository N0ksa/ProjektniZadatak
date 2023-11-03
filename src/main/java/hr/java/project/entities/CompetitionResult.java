package hr.java.project.entities;

import java.math.BigDecimal;

/**
 * Predstavlja rezultat sudionika na natjecanju.
 */
public record CompetitionResult(Student participant, BigDecimal score) {
}
