package it.unisa.pharmaweb.model.bean;

import java.io.Serializable;

public class CategoriaBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idCategoria;
    private String nome;

    public CategoriaBean() {
    }

    public CategoriaBean(int idCategoria, String nome) {
        this.idCategoria = idCategoria;
        this.nome = nome;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    @Override
    public String toString() {
        return nome;
    }
}