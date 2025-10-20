package it.unisa.pharmaweb.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<CartItemBean> items;

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
    
    public double getTotal() {
        double total = 0;
        for (CartItemBean item : items) {
            total += item.getSubtotal();
        }
        return total;
    }
    
    // Getter per la lista di item
    public List<CartItemBean> getItems() {
        return items;
    }
}