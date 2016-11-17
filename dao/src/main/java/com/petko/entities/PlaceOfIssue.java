package com.petko.entities;

public enum PlaceOfIssue {
    HOME,
    READING_ROOM;

    public static PlaceOfIssue getPlaceOfIssue(String mapping) {
        switch (mapping) {
            case "Абонемент":
                return HOME;
            case "Читальный зал":
                return READING_ROOM;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (ordinal()) {
            case 0:
                return "Абонемент";
            case 1:
                return "Читальный зал";
            default:
                return "Дом/ЧЗ";
        }
    }
}
