package com.example.zippy0001.classes;

public class MeusPedidosGetterSetter {
    private String status;
    private String nome;
    private String destino;
    private String data;
    private String pedido;
    String imgPedido;


    public MeusPedidosGetterSetter(String status, String pedido, String nome, String destino, String data, String imgPedido) {
        this.status = status;
        this.pedido = pedido;
        this.nome = nome;
        this.destino = destino;
        this.data = data;
        this.imgPedido = imgPedido;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImgPedido() {
        return imgPedido;
    }

    public void setImgPedido(String imgPedido) {
        this.imgPedido = imgPedido;
    }
}
