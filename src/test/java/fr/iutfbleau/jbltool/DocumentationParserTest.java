package fr.iutfbleau.jbltool;

import fr.iutfbleau.jbltool.documentation.DocumentationParser;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DocumentationParserTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void getInstanceIsNotNull() {
        assertNotNull(DocumentationParser.getInstance());
    }

    @Test
    public void readFromNullThrowNullPointerException() throws SAXException, ParserConfigurationException, IOException{
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage("XML file for documentation cannot be null.");
        DocumentationParser.readFrom(null);
    }

    @Test
    public void invalidInstructionReturnsNull() {
        Assert.assertNull(DocumentationParser.getInstruction("Does not exist"));
    }

    @Test
    public void noInstructionsNotReturnsNull() {
        assertNotNull(DocumentationParser.getInstructions());
    }

    @Test
    public void noInstructionsReturnsEmptyHasMapValues() {
        assertTrue(DocumentationParser.getInstructions().size() == 0);
    }
}
