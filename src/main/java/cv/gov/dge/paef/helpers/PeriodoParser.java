package cv.gov.dge.paef.helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class PeriodoParser {

    private static final DateTimeFormatter PT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private PeriodoParser() {}

    public static LocalDate[] parse(String periodo) {
        if (periodo == null || periodo.isBlank()) return new LocalDate[]{null, null};

        // ex: "01-01-2025 a 31-01-2025"
        String[] parts = periodo.split("\\s+a\\s+");
        if (parts.length != 2) return new LocalDate[]{null, null};

        LocalDate start = LocalDate.parse(parts[0].trim(), PT);
        LocalDate end   = LocalDate.parse(parts[1].trim(), PT);
        return new LocalDate[]{start, end};
    }
}
