package com.petko.entities;

public enum OrderStatus {
    ORDERED,
    ON_HAND,
    CLOSED;

    /**
     * gives OrderStatus by its String mapping
     * @param mapping of OrderStatus elements
     * @return OrderStatus by its String mapping
     */
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

    /**
     * gives String mapping of OrderStatus
     * @return String mapping of OrderStatus
     */
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
