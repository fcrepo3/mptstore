package org.nsdl.mptstore.query.lang;

import org.nsdl.mptstore.query.QueryException;

public class QuerySyntaxException extends QueryException {

    public QuerySyntaxException(String message) {
        super(message);
    }

    public QuerySyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

}
