package it.unisa.pharmaweb.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final List<CartItemBean> items;

    public CartBean() {
        items = new ArrayList<>();
    }
    
    public void addItem(ProductBean product, int quantity) {
        for (CartItemBean item : items) {
            if (item.getProduct().getIdProdotto() == product.getIdProdotto()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItemBean(product, quantity));
    }

    public void removeItem(int productId) {
        items.removeIf(item -> item.getProduct().getIdProdotto() == productId);
    }
    
    public void updateQuantity(int productId, int newQuantity) {
        if (newQuantity <= 0) {
            removeItem(productId);
            return;
        }
        for (CartItemBean item : items) {
            if (item.getProduct().getIdProdotto() == productId) {
                item.setQuantity(newQuantity);
                return;
            }
        }
    }

    public void clear() {
        items.clear();
    }
    
    public int getQuantityOfProduct(int productId) {
        for (CartItemBean item : items) {
            if (item.getProduct().getIdProdotto() == productId) {
                return item.getQuantity();
            }
        }
        return 0;
    }

    public double getTotal() {
        double total = 0;
        for (CartItemBean item : items) {
            total += item.getSubtotal();
        }
        return total;
    }
    
    public List<CartItemBean> getItems() {
        return items;
    }
}