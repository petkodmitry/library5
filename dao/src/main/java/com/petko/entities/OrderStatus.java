package com.petko.entities;

public enum OrderStatus {
    ORDERED,
    ON_HAND,
    CLOSED;

    public static OrderStatus getOrderStatus(String mapping) {
        switch (mapping) {
            case "Открыт":
                return ORDERED;
            case "На руках":
                return ON_HAND;
            case "Закрыт":
                return CLOSED;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (ordinal()) {
            case 0:
                return "Открыт";
            case 1:
                return "На руках";
            case 2:
                return "Закрыт";
            default:
                return "Заказ ???";
        }
    }
}
