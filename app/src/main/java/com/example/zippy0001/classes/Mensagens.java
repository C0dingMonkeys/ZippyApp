package com.example.zippy0001.classes;

public class Mensagens {
    private String idMensagem;
    private String mensagem;
    private String usuarioOrigem;
    private String usuarioDestino;


    public Mensagens(String idMensagem, String mensagem, String usuarioOrigem, String usuarioDestino) {
        this.idMensagem = idMensagem;
        this.mensagem = mensagem;
        this.usuarioOrigem = usuarioOrigem;
        this.usuarioDestino = usuarioDestino;
    }

    public String getIdMensagem() {
        return idMensagem;
    }

    public void setIdMensagem(String idMensagem) {
        this.idMensagem = idMensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getUsuarioOrigem() {
        return usuarioOrigem;
    }

    public void setUsuarioOrigem(String usuarioOrigem) {
        this.usuarioOrigem = usuarioOrigem;
    }

    public String getUsuarioDestino() {
        return usuarioDestino;
    }

    public void setUsuarioDestino(String usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
    }

}
