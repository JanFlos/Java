package cz.robotron.examples.xml;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import cz.robotron.examples.Utils;

public class TestXML {
    public static XPath xpath = XPathFactory.newInstance().newXPath();

    public static boolean validateXMLSchema(String xsdPath, String xmlPath) {

        try {
            SchemaFactory factory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (IOException | SAXException e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
        return true;
    }

    public Node[] loadNodes() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException,
        URISyntaxException
    {
        String xmls = "C:\\dev\\Java\\cz.robotron.examples\\src\\cz\\robotron\\examples\\xml\\GolfCountryClub.xml";
        String xsds = "C:\\dev\\Java\\cz.robotron.examples\\src\\cz\\robotron\\examples\\xml\\GolfCountryClub.xsd";
        System.out.println("EmployeeRequest.xml validates against Employee.xsd? " + validateXMLSchema(xsds, xmls));

        StreamSource schemaSource = new StreamSource(Utils.getResourceAsStream(this, "GolfCountryClub.xsd"));
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(schemaSource);
        Validator validator = schema.newValidator();

        // validate the DOM tree
        try {
            validator.validate(new StreamSource(Utils.getResourceAsStream(this, "GolfCountryClub.xml")));
        } catch (SAXException e) {
            System.out.println(e.toString());// instance document is invalid!
        }

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setSchema(schema);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);

        DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();

        Document document = docBuilder.parse(Utils.getResourceAsStream(this, "GolfCountryClub.xml"));
        NodeList nodes = document.getChildNodes();
        //NodeList nodes = (NodeList) xpath.evaluate("/tns:GolfCountryClub", document, XPathConstants.NODESET);
        return Utils.nodesToArray(nodes);
    }

    public static Node[] getTestNodes()
    {
        try {
            return new TestXML().loadNodes();
        } catch (Exception e) {
            Utils.showException("Unable to load Example XML File ", e);
        }
        return null;
    }

}
