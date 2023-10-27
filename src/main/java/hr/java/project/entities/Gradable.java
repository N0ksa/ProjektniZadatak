package hr.java.project.entities;

import java.math.BigDecimal;
import java.util.List;

public interface Gradable {
    public BigDecimal calculateScore(List<CompetitionResult> competitionResults);
}
