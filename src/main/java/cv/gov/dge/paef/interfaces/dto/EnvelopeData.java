// src/main/java/cv/gov/dge/paef/interfaces/dto/EnvelopeData.java
package cv.gov.dge.paef.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Envelope que devolve apenas {"data": ...} */
@Getter
@AllArgsConstructor
public class EnvelopeData<T> {
    private final T data;
}
