package org.nsdl.mptstore.core;

public class ModificationException extends Exception {

    public ModificationException(String message) {
        super(message);
    }

    public ModificationException(String message, Throwable cause) {
        super(message, cause);
    }

}
