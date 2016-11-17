package com.petko;

import java.io.IOException;

public class DaoException extends IOException{
    public DaoException(String message) {
        super(message);
    }
}
