package it.unisa.pharmaweb.model.bean;

import java.io.Serializable;

public class CartItemBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private ProductBean product;
    private int quantity;

    public CartItemBean(ProductBean product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Metodo helper per calcolare il subtotale di questa riga
    public double getSubtotal() {
        return product.getPrezzoFinale() * quantity;
    }
}