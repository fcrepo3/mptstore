package org.nsdl.mptstore.query;

public class RuntimeQueryException extends RuntimeException {

    public RuntimeQueryException(QueryException e) {
        super(e);
    }

}
