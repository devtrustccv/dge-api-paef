package cv.gov.dge.paef.integration.dto;

public final class DucParser {
    private DucParser(){}

    public static Duc parse(String response, String linkReportDuc) {
        Duc d = new Duc();
        d.setEstado(getValue(response, "CODIGO"));
        d.setEstadoDescricao(getValue(response, "DESCRICAO"));

        if ("ERROR".equalsIgnoreCase(d.getEstado())) {
            throw new RuntimeException(d.getEstado() + " " + d.getEstadoDescricao());
        }

        d.setEntidade(getValue(response, "ENTIDADE"));
        d.setReferencia(getValue(response, "REFERENCIA"));
        d.setDuc(getValue(response, "DUC"));

        String montante = getValue(response, "MONTANTE");
        d.setValor(montante == null || montante.isBlank() ? 0d : Double.parseDouble(montante));

        d.setLink(linkReportDuc + d.getDuc());
        return d;
    }

    private static String getValue(String s, String tag) {
        if (s == null) return null;
        String open = "<" + tag + ">";
        String close = "</" + tag + ">";
        int i = s.indexOf(open);
        int j = s.indexOf(close);
        if (i < 0 || j < 0 || j <= i) return null;
        return s.substring(i + open.length(), j).trim();
    }
}
