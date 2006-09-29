package org.nsdl.mptstore.util;

import java.text.ParseException;

import junit.framework.TestCase;
import junit.swingui.TestRunner;

public class NTriplesUtilTest extends TestCase {

    public NTriplesUtilTest(String name) { super (name); }

    public void setUp() {
    }
            
    public void tearDown() {
    }

    //
    // Parse Tests
    //

    private String checkTriple(String ntTriple) {
        try {
            NTriplesUtil.parseTriple(ntTriple);
            return null;
        } catch (ParseException e) {
            return e.getMessage() + " at character " + e.getErrorOffset() 
                    + " of input: " + ntTriple; 
        }
    }

    public void testParseGoodTriples() {

        String msg;

        msg = checkTriple("<urn:a> <urn:b> <urn:c> ."); 
        assertNull(msg, msg);

        msg = checkTriple("<urn:a>  <urn:b>  <urn:c>  .  ");
        assertNull(msg, msg);

        msg = checkTriple("<urn:a>\t<urn:b>\t<urn:c>\t."); 
        assertNull(msg, msg);

        msg = checkTriple("<urn:a>\t\t<urn:b>\t\t<urn:c>\t\t.\t\t"); 
        assertNull(msg, msg);

        msg = checkTriple("<urn:a> <urn:b> \"\" ."); 
        assertNull(msg, msg);

        msg = checkTriple("<urn:a> <urn:b> \"test\" ."); 
        assertNull(msg, msg);

        msg = checkTriple("<urn:a> <urn:b> \"test with spaces\" ."); 
        assertNull(msg, msg);

        msg = checkTriple("<urn:a> <urn:b> \"testin's fun\" ."); 
        assertNull(msg, msg);

        msg = checkTriple("<urn:a> <urn:b> \"test with escaped \\\"quotes\\\"\" ."); 
        assertNull(msg, msg);

        msg = checkTriple("<urn:a> <urn:b> \"test with escaped \\ttab\" ."); 
        assertNull(msg, msg);

        msg = checkTriple("<urn:a> <urn:b> \"test with escaped \\ncarriage return\" ."); 
        assertNull(msg, msg);

        msg = checkTriple("<urn:a> <urn:b> \"test with escaped \\rline feed\" ."); 
        assertNull(msg, msg);

        msg = checkTriple("<urn:a> <urn:b> \"test with escaped \\\\backslash\" ."); 
        assertNull(msg, msg);

        msg = checkTriple("<urn:a> <urn:b> \"test\"@en ."); 
        assertNull(msg, msg);

        msg = checkTriple("<urn:a> <urn:b> \"test\"@en-US ."); 
        assertNull(msg, msg);

        msg = checkTriple("<urn:a> <urn:b> \"test\"@good-lang-code ."); 
        assertNull(msg, msg);

        msg = checkTriple("<urn:a> <urn:b> \"test\"@en-US123 ."); 
        assertNull(msg, msg);

        msg = checkTriple("<urn:a> <urn:b> \"test\"^^<urn:someType> ."); 
        assertNull(msg, msg);

    }

    public void testParseBadTriples() {

        assertNotNull(checkTriple(""));
        assertNotNull(checkTriple("."));
        assertNotNull(checkTriple("<urn:a> ."));
        assertNotNull(checkTriple("<urn:a> <urn:b> ."));
        assertNotNull(checkTriple("<urn:a> <urn:b> <urn:c>"));
        assertNotNull(checkTriple(" <urn:a> <urn:b> <urn:c> ."));
        assertNotNull(checkTriple("\r<urn:a> <urn:b> <urn:c> ."));
        assertNotNull(checkTriple("\n<urn:a> <urn:b> <urn:c> ."));
        assertNotNull(checkTriple("\t<urn:a> <urn:b> <urn:c> ."));
        assertNotNull(checkTriple("<urn:a> <urn:b> <urn:c>.."));
        assertNotNull(checkTriple("<urn:a>\n<urn:b>\n<urn:c>."));
        assertNotNull(checkTriple("<urn:a>\r<urn:b>\r<urn:c>."));
        assertNotNull(checkTriple("<urn:a> <urn:b> 'test' ."));
        assertNotNull(checkTriple("<urn:a> <urn:b> \"test\" unescaped quote\" ."));
        assertNotNull(checkTriple("<urn:a> <urn:b> \"test\n unescaped cr\" ."));
        assertNotNull(checkTriple("<urn:a> <urn:b> \"test\r unescaped lf\" ."));
        assertNotNull(checkTriple("<urn:a> <urn:b> \"test\t unescaped tab\" ."));
        assertNotNull(checkTriple("<urn:a> <urn:b> \"test\"@bad-lang-codddddde ."));
        assertNotNull(checkTriple("<urn:a> <urn:b> \"test\"@bad1-lang-code ."));
        assertNotNull(checkTriple("<urn:a> <urn:b> \"test\"@bad.lang.code ."));
        assertNotNull(checkTriple("<urn:a> <urn:b> \"test\"^^<urn:bad typeuri> ."));
        assertNotNull(checkTriple("<urn:a> <urn:b> \"test\"^^<urn:someType>extraChars ."));
        assertNotNull(checkTriple("<urn:a> \"bad literal position\" <urn:c> ."));
        assertNotNull(checkTriple("\"bad literal position\" <urn:b> <urn:c> ."));
    }

    public static void main(String[] args) {
        TestRunner.run(NTriplesUtilTest.class);
    }   

}
