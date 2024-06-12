package com.example.zippy0001.classes;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String usuario;
    private String nome;


    public Usuario(String usuario, String nome){
        this.usuario = usuario;
        this.nome = nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
