package fr.iutfbleau.jbltool.documentation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class DocumentationParser {
    private static final DocumentationParser instance = new DocumentationParser();
    private static HashMap<String, InstructionDocumentation> instructionDocumentationHashMap = new HashMap<>();

    /**
     * Avoid instanciation.
     */
    private DocumentationParser() {
    }

    /**
     * Get the instance of documentation parser.
     * @return DocumentationParser
     * The instance of DocumentationParser.
     */
    public static DocumentationParser getInstance() {
        return instance;
    }

    public static void readFrom(File xmlSource) throws SAXException, ParserConfigurationException, IOException {
        Objects.requireNonNull(xmlSource, "XML file for documentation cannot be null.");

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        // disable external entities to avoid XXE
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        DocumentBuilder documentBuilder;
        Document document;

        // can throw errors
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        document = documentBuilder.parse(xmlSource);
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("instruction"); // get all instructions

        for (int itr = 0; itr < nodeList.getLength(); itr++) {
            Node node = nodeList.item(itr);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                InstructionDocumentation id = new InstructionDocumentation();

                String name = element.getElementsByTagName("name").item(0).getTextContent();
                id.setName(name);
                id.setDescription(element.getElementsByTagName("description").item(0).getTextContent());
                id.setExemple(element.getElementsByTagName("exemple").item(0).getTextContent());

                ArrayList<String> parameters = new ArrayList<>();
                NodeList parametersNodes = element.getElementsByTagName("parameter");
                for(int i = 0; i < parametersNodes.getLength(); i++) {
                    parameters.add(parametersNodes.item(i).getTextContent());
                }
                id.setParameters(parameters);
                id.setOpcode(Integer.parseInt(element.getElementsByTagName("opcode").item(0).getTextContent()));
                instructionDocumentationHashMap.put(name, id);
            }
        }
    }

    public static InstructionDocumentation getInstruction(String name) {
        return instructionDocumentationHashMap.get(name);
    }

    public static Collection<InstructionDocumentation> getInstructions() {
        return instructionDocumentationHashMap.values();
    }
}
