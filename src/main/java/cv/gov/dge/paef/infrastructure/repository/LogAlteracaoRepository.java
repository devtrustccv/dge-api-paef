// src/main/java/cv/gov/dge/paef/infrastructure/repository/LogAlteracaoRepository.java
package cv.gov.dge.paef.infrastructure.repository;

import cv.gov.dge.paef.infrastructure.LogAlteracaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogAlteracaoRepository extends JpaRepository<LogAlteracaoEntity, String> {}
