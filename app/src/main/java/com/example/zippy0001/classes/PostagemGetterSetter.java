package com.example.zippy0001.classes;

public class PostagemGetterSetter {
    private String NomeProduto;
    private String LinkProduto;
    private String PrecoProduto;
    private String PaisDestino;
    private String CidadeDestino;
    private String CaixaProduto;
    private String ImgProduto;



    public PostagemGetterSetter(String NomeProduto, String LinkProduto, String PrecoProduto, String PaisDestino, String CidadeDestino, String CaixaProduto, String ImgProduto) {

        this.NomeProduto = NomeProduto;
        this.LinkProduto = LinkProduto;
        this.PrecoProduto = PrecoProduto;
        this.PaisDestino = PaisDestino;
        this.CidadeDestino = CidadeDestino;
        this.CaixaProduto = CaixaProduto;
        this.ImgProduto = ImgProduto;


    }

    public String getNomeProduto() {
        return NomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        NomeProduto = nomeProduto;
    }

    public String getLinkProduto() {
        return LinkProduto;
    }

    public void setLinkProduto(String linkProduto) {
        LinkProduto = linkProduto;
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
    public String getImgProduto() { return ImgProduto;}

    public void setImgProduto(String imgProduto) { ImgProduto = imgProduto; }
}
