package org.nsdl.mptstore.impl.postgres;

import java.util.ArrayList;
import java.util.List;

import org.nsdl.mptstore.core.AbstractDDLGenerator;

public class PostgresDDLGenerator extends AbstractDDLGenerator {

    public PostgresDDLGenerator() { 
    }

    // Implements DDLGenerator.getCreateMapTableDDL(String)
    public List<String> getCreateMapTableDDL(String table) {

        List<String> cmds = new ArrayList<String>();

        cmds.add("CREATE TABLE " + table + " (\n"
               + "  pKey SERIAL,\n"
               + "  p TEXT NOT NULL\n"
               + ")");
        cmds.add("CREATE INDEX " + table + "_pKey "
               + " on " + table + " (pKey)");
        cmds.add("CREATE INDEX " + table + "_p "
               + " on " + table + " (p)");

        return cmds;
    }

    // Implements DDLGenerator.getCreateSOTableDDL(String)
    public List<String> getCreateSOTableDDL(String table) {

        List<String> cmds = new ArrayList<String>();

        cmds.add("CREATE TABLE " + table + " (\n"
               + "  s TEXT NOT NULL,\n"
               + "  o TEXT NOT NULL\n"
               + ")");
        cmds.add("CREATE INDEX " + table + "_s "
               + " on " + table + " (s)");
        cmds.add("CREATE INDEX " + table + "_o "
               + " on " + table + " (o)");

        return cmds;
    }

}