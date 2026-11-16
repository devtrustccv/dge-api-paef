package cv.gov.dge.paef.application.generic.service;

public record EmailAnexo(String nome, String contentType, byte[] conteudo) {
    public EmailAnexo { conteudo = conteudo.clone(); }
    @Override public byte[] conteudo() { return conteudo.clone(); }
}