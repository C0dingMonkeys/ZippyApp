package com.example.zippy0001.classes;

public class PostagemGetterSetter {
    private String idPedido;
    private String idClienteDest;
    private String NomeProduto;
    private String PrecoProduto;
    private String PaisDestino;
    private String CidadeDestino;
    private String CaixaProduto;
    private String ImgProduto;
    private String LinkProduto;


    public PostagemGetterSetter(String idPedido,String idClienteDest, String NomeProduto, String PrecoProduto, String PaisDestino, String CidadeDestino, String CaixaProduto, String ImgProduto, String LinkProduto) {
        this.idPedido = idPedido;
        this.idClienteDest = idClienteDest;
        this.NomeProduto = NomeProduto;
        this.LinkProduto = LinkProduto;
        this.PrecoProduto = PrecoProduto;
        this.PaisDestino = PaisDestino;
        this.CidadeDestino = CidadeDestino;
        this.CaixaProduto = CaixaProduto;
        this.ImgProduto = ImgProduto;


    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getIdClienteDest() {
        return idClienteDest;
    }

    public void setIdClienteDest(String idClienteDest) {
        this.idClienteDest = idClienteDest;
    }

    public String getNomeProduto() {
        return NomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        NomeProduto = nomeProduto;
    }

    public String getPrecoProduto() {
        return PrecoProduto;
    }

    public void setPrecoProduto(String precoProduto) {
        PrecoProduto = precoProduto;
    }

    public String getPaisDestino() {
        return PaisDestino;
    }

    public void setPaisDestino(String paisDestino) {
        PaisDestino = paisDestino;
    }

    public String getCidadeDestino() {
        return CidadeDestino;
    }

    public void setCidadeDestino(String cidadeDestino) {
        CidadeDestino = cidadeDestino;
    }

    public String getCaixaProduto() {
        return CaixaProduto;
    }

    public void setCaixaProduto(String caixaProduto) {
        CaixaProduto = caixaProduto;
    }

    public String getImgProduto() {
        return ImgProduto;
    }

    public void setImgProduto(String imgProduto) {
        ImgProduto = imgProduto;
    }

    public String getLinkProduto() {
        return LinkProduto;
    }

    public void setLinkProduto(String linkProduto) {
        LinkProduto = linkProduto;
    }
}
