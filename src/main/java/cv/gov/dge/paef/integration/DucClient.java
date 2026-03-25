package cv.gov.dge.paef.integration;

import cv.gov.dge.paef.integration.dto.Duc;
import cv.gov.dge.paef.integration.dto.DucParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class DucClient {

    private final RestTemplate restTemplate;
    private final String estadoDucUrl;
    private final String linkReportDuc;
    private final String token;

    public DucClient(
            RestTemplate restTemplate,
            @Value("${duc.estado-url}") String estadoDucUrl,
            @Value("${duc.report-base-url}") String linkReportDuc,
            @Value("${authorization.integration.duc}") String token
    ) {
        this.restTemplate = restTemplate;
        this.estadoDucUrl = estadoDucUrl;
        this.linkReportDuc = linkReportDuc;
        this.token = token;
    }

    public Duc consultarEstadoDuc(String duc, String email) {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("p_duc", duc);
        form.add("p_email", email == null ? "" : email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(form, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(estadoDucUrl, request, String.class);
        System.out.println(response);
        return DucParser.parse(response.getBody(), linkReportDuc);
    }
}