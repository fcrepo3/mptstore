package org.nsdl.mptstore.core;

import java.util.Iterator;

public interface ClosableIterator<E> extends Iterator<E> {

    public void close();

}
