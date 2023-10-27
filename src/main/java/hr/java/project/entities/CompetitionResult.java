package hr.java.project.entities;

import java.math.BigDecimal;

public record CompetitionResult(NamedEntity participant, BigDecimal score) {
}
