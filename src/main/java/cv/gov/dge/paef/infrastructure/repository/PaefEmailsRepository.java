package cv.gov.dge.paef.infrastructure.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Reads only the email column, without assuming the configuration table's ID type. */
@Repository
public class PaefEmailsRepository {
    private final JdbcTemplate jdbc;

    public PaefEmailsRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<String> findEmails() {
        return jdbc.queryForList("select email from paef.paef_t_emails", String.class);
    }
}
