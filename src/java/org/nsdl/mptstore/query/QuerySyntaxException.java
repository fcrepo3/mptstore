package org.nsdl.mptstore.query;

public class QuerySyntaxException extends QueryException {

    public QuerySyntaxException(String message) {
        super(message);
    }

    public QuerySyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

}
