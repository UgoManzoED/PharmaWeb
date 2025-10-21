package it.unisa.pharmaweb.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WishlistBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final List<ProductBean> items;

    public WishlistBean() {
        items = new ArrayList<>();
    }
    
    public void addItem(ProductBean product) {
        for (ProductBean item : items) {
            if (item.getIdProdotto() == product.getIdProdotto()) {
                return;
            }
        }
        items.add(product);
    }

    public void removeItem(int productId) {
        items.removeIf(item -> item.getIdProdotto() == productId);
    }
    
    public List<ProductBean> getItems() {
        return items;
    }

    public int getSize() {
        return items.size();
    }
}