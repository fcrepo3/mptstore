package org.nsdl.mptstore.util;

import java.text.ParseException;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.nsdl.mptstore.TestConfig;

public class NTriplesUtilUnitTest {

    @BeforeClass
    public static void setUpClass() {
        TestConfig.init();
    }

    @Test
    public void testParseGoodLanguages() {
        assertTrue(isValidLanguage("en"));
        assertTrue(isValidLanguage("en-US"));
        assertTrue(isValidLanguage("en-US2"));
        assertTrue(isValidLanguage("en-US-XYZ"));
        assertTrue(isValidLanguage("a-2-c-4-e-f-g-h-i-jklmnopq"));
    }

    @Test
    public void testParseBadLanguages() {
        assertFalse(isValidLanguage(""));
        assertFalse(isValidLanguage("-"));
        assertFalse(isValidLanguage("en-"));
        assertFalse(isValidLanguage("-en"));
        assertFalse(isValidLanguage("en2-US"));
        assertFalse(isValidLanguage("abcdefghi"));
        assertFalse(isValidLanguage("en-abcdefghi"));
    }

    @Test
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

    @Test
    public void testUnicodeUnescaping() throws Exception {

        String notEscaped = "\u00BFHabla espa\u00F1ol?";
        String escaped    = "\\u00BFHabla espa\\u00F1ol?";

        assertEquals(notEscaped, NTriplesUtil.unescapeLiteralValue(escaped));
    }

    @Test
    public void testAsciiUnescaping() throws Exception {
        checkUnescapingCombos("\t", "\\t");
        checkUnescapingCombos("\r", "\\r");
        checkUnescapingCombos("\n", "\\n");
        checkUnescapingCombos("\"", "\\\"");
        checkUnescapingCombos("\\", "\\\\");
    }

    @Test
    public void testUnicodeEscaping() throws Exception {

        String notEscaped = "\u00BFHabla espa\u00F1ol?";
        String escaped    = "\\u00BFHabla espa\\u00F1ol?";

        assertEquals(escaped, NTriplesUtil.escapeLiteralValue(notEscaped));
    }

    @Test
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

    private boolean isValidLanguage(String lang) {
        try {
            NTriplesUtil.validateLanguage(lang);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private String checkTriple(String ntTriple) {
        try {
            NTriplesUtil.parseTriple(ntTriple);
            return null;
        } catch (ParseException e) {
            return e.getMessage() + " at character " + e.getErrorOffset() 
                    + " of input: " + ntTriple; 
        }
    }

    private void checkUnescapingCombos(String unescaped, String escaped) throws Exception {
        checkUnescaping(unescaped, escaped);
        checkUnescaping(" " + unescaped, " " + escaped);
        checkUnescaping("a" + unescaped, "a" + escaped);
        checkUnescaping(unescaped + " ", escaped + " ");
        checkUnescaping(unescaped + "a", escaped + "a");
    }

    private void checkUnescaping(String unescaped, String escaped) throws Exception {
        assertEquals(unescaped, NTriplesUtil.unescapeLiteralValue(escaped));
    }

}
