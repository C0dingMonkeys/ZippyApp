package com.example.zippy0001.classes;

public class AvaliacoesGetterSetter {
    private String Nome01;
    private String Nome02;
    private String Pais01;
    private String Pais02;
    private String Perfil01;
    private String Perfil02;


    public AvaliacoesGetterSetter() {
    }

    public AvaliacoesGetterSetter( String Nome01, String Nome02, String Pais01, String Pais02, String Perfil01, String Perfil02) {
        this.Nome01 = Nome01;
        this.Nome02 = Nome02;
        this.Pais01 = Pais01;
        this.Pais02 = Pais02;
        this.Perfil01 = Perfil01;
        this.Perfil02 = Perfil02;

    }

    public String getNome01() {
        return Nome01;
    }

    public void setNome01(String nome01) {
        Nome01 = nome01;
    }

    public String getNome02() {
        return Nome02;
    }

    public void setNome02(String nome02) {
        Nome02 = nome02;
    }

    public String getPais01() {
        return Pais01;
    }

    public void setPais01(String pais01) {
        Pais01 = pais01;
    }

    public String getPais02() {
        return Pais02;
    }

    public void setPais02(String pais02) {
        Pais02 = pais02;
    }

    public String getPerfil01() {
        return Perfil01;
    }

    public void setPerfil01(String perfil01) {
        Perfil01 = perfil01;
    }

    public String getPerfil02() {
        return Perfil02;
    }

    public void setPerfil02(String perfil02) {
        Perfil02 = perfil02;
    }
}
