package cv.gov.dge.paef.helpers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Utils {
    public static boolean blank(String s) {
        return s == null || s.trim().isEmpty();
    }
    public static String normalizeLinks(String html) {

        if (html == null) return null;

        return html
                .replaceAll("<a\\s+(?![^>]*target=)", "<a target=\"_blank\" ");
    }
    public static String nullToEmpty(String s) { return s == null ? "" : s; }

    public static String decorateLinks(String html) {
        if (html == null || html.isBlank()) return html;

        Document doc = Jsoup.parseBodyFragment(html);

        for (Element a : doc.select("a[href]")) {
            // abrir nova janela
            a.attr("target", "_blank");

            // segurança (boa prática)
            a.attr("rel", "noopener noreferrer");

            // estilo inline: cor + sublinhado
            String style = a.hasAttr("style") ? a.attr("style") : "";
            if (!style.endsWith(";") && !style.isBlank()) style += ";";

            // escolhe a cor que quiseres
            style += "color:#1a73e8;text-decoration:underline;font-weight:500;";

            a.attr("style", style);
        }

        return doc.body().html();
    }
}
