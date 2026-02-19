// src/main/java/cv/gov/dge/paef/domain/geografia/model/GeografiaOption.java
package cv.gov.dge.paef.domain.geografia.model;

import lombok.Builder;

@Builder
public record GeografiaOption(
        String id,
        String nome
) {}
