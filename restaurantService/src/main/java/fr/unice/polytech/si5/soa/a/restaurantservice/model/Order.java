package fr.unice.polytech.si5.soa.a.restaurantservice.model;

public class Order {

    private int restoId;
    private String [] items;

    public Order(int restoId, String[] items) {
        this.restoId = restoId;
        this.items = items;
    }

    public Order() {
        String [] items = {};
        new Order(0, items);
    }

    public int getRestoId() {
        return restoId;
    }

    public String [] getItems() {
        return items;
    }

    @Override
    public String toString() {
        String out = "Resto id: " + this.restoId + "\n" +
                "Items :\n";
        for (String i : this.items) {
            out += i+"\n";
        }
        return out;
    }

}
