package br.com.projetomobile.app;

/**
 * Created by 16254855 on 04/09/2017.
 */

public class Categoria {

    private int idCategoria;
    private String descricao;
    private int idUsuario;

    @Override
    public String toString(){
        return descricao;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
