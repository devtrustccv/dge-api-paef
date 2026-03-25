// src/main/java/cv/gov/dge/paef/domain/geografia/business/GeografiaBusiness.java
package cv.gov.dge.paef.domain.geografia.business;

import cv.gov.dge.paef.domain.geografia.model.GeografiaOption;
import cv.gov.dge.paef.infrastructure.mapper.GeografiaMapper;
import cv.gov.dge.paef.infrastructure.repository.GeografiaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeografiaBusiness {

    private final GeografiaRepository repo;
    private final GeografiaMapper mapper;

    public GeografiaBusiness(GeografiaRepository repo, GeografiaMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<GeografiaOption> getIlhas() {
        return repo.findIlhas().stream().map(mapper::toModel).toList();
    }

    public List<GeografiaOption> getConcelhosByIlha(String ilha) {
        return repo.findConcelhosByIlha(ilha).stream().map(mapper::toModel).toList();
    }

    public List<GeografiaOption> getFreguesiasByConcelho(String concelho) {
        return repo.findFreguesiasByConcelho(concelho).stream().map(mapper::toModel).toList();
    }

    public List<GeografiaOption> getZonasByFreguesia(String freguesia) {
        return repo.findZonasByFreguesia(freguesia).stream().map(mapper::toModel).toList();
    }

    @Component
    public class LocalizacaoResolver {

        private final GeografiaRepository repo;

        public LocalizacaoResolver(GeografiaRepository repo) {
            this.repo = repo;
        }

        /** Equivalente simples ao Utils.getFreguesia(geogLocalId) */
        public String resolveLocal(String geogLocalId) {
            if (geogLocalId == null || geogLocalId.isBlank()) return "";
            return repo.findById(geogLocalId).map(g -> g.getNome() == null ? "" : g.getNome()).orElse("");
        }
    }
    public GeoFields resolve(String geogLocalId) {
        if (geogLocalId == null || geogLocalId.isBlank()) return new GeoFields("", "", "", "");
        var g = repo.findById(geogLocalId).orElse(null);
        if (g == null) return new GeoFields("", "", "", "");
        return new GeoFields(
                nvl(g.getIlha()),
                nvl(g.getConcelho()),
                nvl(g.getFreguesia()),
                nvl(g.getZona())
        );
    }

    private String nvl(String s){ return s == null ? "" : s; }

    public record GeoFields(String ilha, String concelho, String freguesia, String zona) {}
}
