package com.taotao.cloud.sys.biz.tools.soap.utils;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.naming.ConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.List;
import java.util.Vector;

/** 
 * DOM utility 
 *  
 * Thanks to Tom Fennelly from Jboss Group 
 *  
 */  
public class DOMUtil {  
      
    /** 
     * Create a new W3C Document. 
     * <p/> 
     * Handles exceptions etc. 
     * @return The new Document instance. 
     * @throws ConfigurationException  
     */  
    public static Document createDocument() throws ConfigurationException {  
        Document doc = null;  
          
        try {  
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();  
        } catch (ParserConfigurationException e) {  
            throw new ConfigurationException("Failed to create ESB Configuration Document instance.");  
        }  
          
        return doc;  
    }  
  
    /** 
     * Parse the supplied XML String and return the associated W3C Document object. 
     * 
     * @param xml XML String. 
     * @return The W3C Document object associated with the input stream. 
     */  
    public static Document parse(String xml) throws SAXException, IOException {  
        return parseStream(new ByteArrayInputStream(xml.getBytes()), false, false);  
    }  
  
    /** 
     * Parse the XML stream and return the associated W3C Document object. 
     * <p/> 
     * Performs a namespace unaware parse. 
     * 
     * @param stream 
     *            The stream to be parsed. 
     * @param validate 
     *            True if the document is to be validated, otherwise false. 
     * @param expandEntityRefs 
     *            Expand entity References as per 
     *            {@link DocumentBuilderFactory#setExpandEntityReferences(boolean)}. 
     * @return The W3C Document object associated with the input stream. 
     */  
    public static Document parseStream(InputStream stream, boolean validate,boolean expandEntityRefs) throws SAXException, IOException {  
        return parseStream(stream, validate, expandEntityRefs, false);  
    }  
  
    /** 
     * Parse the XML stream and return the associated W3C Document object. 
     * 
     * @param stream 
     *            The stream to be parsed. 
     * @param validate 
     *            True if the document is to be validated, otherwise false. 
     * @param expandEntityRefs 
     *            Expand entity References as per 
     *            {@link DocumentBuilderFactory#setExpandEntityReferences(boolean)}. 
     * @param namespaceAware 
     *            True if the document parse is to be namespace aware, 
     *            otherwise false. 
     * @return The W3C Document object associated with the input stream. 
     */  
    public static Document parseStream(InputStream stream, boolean validate,  
            boolean expandEntityRefs, boolean namespaceAware) throws SAXException, IOException {  
        if (stream == null) {  
            throw new IllegalArgumentException(  
                    "null 'stream' arg in method call.");  
        }  
        try {  
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
            DocumentBuilder docBuilder = null;  
  
            factory.setValidating(validate);  
            factory.setExpandEntityReferences(expandEntityRefs);  
            factory.setNamespaceAware(namespaceAware);  
            docBuilder = factory.newDocumentBuilder();  
  
            return docBuilder.parse(stream);  
        } catch (ParserConfigurationException e) {  
            IllegalStateException state = new IllegalStateException(  
                    "Unable to parse XML stream - XML Parser not configured correctly.");  
            state.initCause(e);  
            throw state;  
        } catch (FactoryConfigurationError e) {  
            IllegalStateException state = new IllegalStateException(  
                    "Unable to parse XML stream - DocumentBuilderFactory not configured correctly.");  
            state.initCause(e);  
            throw state;  
        }  
    }  
  
    public static String getAttribute(Element element, String name, String defaultVal) {  
        if(element.hasAttribute(name)) {  
            return element.getAttribute(name);  
        } else {  
            return defaultVal;  
        }  
    }  
  
    /** 
     * Add an Element node to the supplied parent name. 
     * @param parent The parent to to which the new Element node is to be added. 
     * @param elementName The name of the Element to be added. 
     * @return The new Element. 
     */  
    public static Element addElement(Node parent, String elementName) {  
        Element element = null;  
          
        if(parent instanceof Document) {  
            element = ((Document)parent).createElement(elementName);  
        } else {  
            element = parent.getOwnerDocument().createElement(elementName);  
        }  
        parent.appendChild(element);  
          
        return element;  
    }  
      
    /** 
     * Remove all attributes having an empty value. 
     * @param element The element to be processed. 
     */  
    public static void removeEmptyAttributes(Element element) {  
        NamedNodeMap attributes = element.getAttributes();  
        int attribCount = attributes.getLength();  
          
        for(int i = attribCount - 1; i >= 0; i--) {  
            Attr attribute = (Attr) attributes.item(i);  
              
            // Note - doesn't account for namespaces.  Not needed here !  
            if("".equals(attribute.getValue())) {
                attributes.removeNamedItem(attribute.getName());  
            }  
        }  
    }  
  
    /** 
     * Serialize the supplied DOM node to the specified file in the specified output directory. 
     * @param node The DOM node to be serialised. 
     * @param outdir The directory into which the file is to be serialised. 
     * @param fileName The name of the file. 
     * @throws ConfigurationException Unable to serialise the node. 
     */  
    public static void serialize(Node node, File outdir, String fileName) throws ConfigurationException {  
        serialize(node, new StreamResult(new File(outdir, fileName)));  
    }  
  
    public static void serialize(Node node, OutputStream out) throws ConfigurationException {  
      serialize(node, new StreamResult(out));  
    }  
  
    /** 
      * Serialize the supplied DOM node to the supplied DOM StreamResult instance. 
      * @param node The DOM node to be serialised. 
      * @param streamRes The StreamResult into which the node is to be serialised. 
      * @throws ConfigurationException Unable to serialise the node. 
      */  
    public static void serialize(Node node, StreamResult streamRes) throws ConfigurationException {  
        serialize(node, streamRes, false);  
    }  
  
   /** 
     * Serialize the supplied DOM node to the supplied DOM StreamResult instance. 
     * @param node The DOM node to be serialised. 
     * @param streamRes The StreamResult into which the node is to be serialised. 
     * @param omitXmlDecl Omit the XML declaration. 
     * @throws ConfigurationException Unable to serialise the node. 
     */  
    public static void serialize(Node node, StreamResult streamRes, boolean omitXmlDecl) throws ConfigurationException {  
        DOMSource domSource = new DOMSource(node);  
          
        try {  
            Transformer transformer = TransformerFactory.newInstance().newTransformer();  
  
            // There's a bug in Java 5 re this code (formatting).  
            // See http://forum.java.sun.com/thread.jspa?threadID=562510&start=0 and it explains the  
            // whys of the following code.  
            // transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");  
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");  
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, (omitXmlDecl?"yes":"no"));  
            transformer.transform(domSource, streamRes);  
        } catch (Exception e) {  
            throw new ConfigurationException("Failed to serialize ESB Configuration Document instance.");  
        }  
    }  
  
    /** 
     * Count the DOM element nodes before the supplied node, having the specified 
     * tag name, not including the node itself. 
     * <p/> 
     * Counts the sibling nodes. 
     * 
     * @param node    Node whose element siblings are to be counted. 
     * @param tagName The tag name of the sibling elements to be counted. 
     * @return The number of siblings elements before the supplied node with the 
     *         specified tag name. 
     */  
    public static int countElementsBefore(Node node, String tagName) {  
        Node parent = node.getParentNode();  
  
        NodeList siblings = parent.getChildNodes();  
        int count = 0;  
        int siblingCount = siblings.getLength();  
  
        for (int i = 0; i < siblingCount; i++) {  
            Node sibling = siblings.item(i);  
  
            if (sibling == node) {  
                break;  
            }  
            if (sibling.getNodeType() == Node.ELEMENT_NODE && ((Element) sibling).getTagName().equals(tagName)) {  
                count++;  
            }  
        }  
  
        return count;  
    }  
  
    /** 
     * Copy the nodes of a NodeList into the supplied list. 
     * <p/> 
     * This is not a cloneCollectionTemplateElement.  It's just a copy of the node references. 
     * <p/> 
     * Allows iteration over the Nodelist using the copy in the knowledge that 
     * the list will remain the same length, even if we modify the underlying NodeList. 
     * Using the NodeList can result in problems because elements can get removed from 
     * the list while we're iterating over it. 
     * <p/> 
     * <i>This code was acquired donated by the Milyn Smooks project.</i> 
     * 
     * @param nodeList Nodelist to copy. 
     * @return List copy. 
     */  
    public static List<Node> copyNodeList(NodeList nodeList) {  
        List<Node> copy = new Vector<Node>();  
  
        if (nodeList != null) {  
            int nodeCount = nodeList.getLength();  
  
            for (int i = 0; i < nodeCount; i++) {  
                copy.add(nodeList.item(i));  
            }  
        }  
  
        return copy;  
    }  
      
    public static Element getNextSiblingElement(Node node) {  
        Node nextSibling = node.getNextSibling();  
  
        while (nextSibling != null) {  
            if (nextSibling.getNodeType() == Node.ELEMENT_NODE) {  
                return (Element) nextSibling;  
            }  
            nextSibling = nextSibling.getNextSibling();  
        }  
  
        return null;  
    }  
  
    public static Node getFirstChildByType(Element element, int nodeType) {  
        NodeList children = element.getChildNodes();  
        int childCount = children.getLength();  
  
        for(int i = 0; i < childCount; i++) {  
            Node child = children.item(i);  
            if (child.getNodeType() == nodeType) {  
                return child;  
            }  
        }  
  
        return null;  
    }  
  
    private static String ELEMENT_NAME_FUNC = "/name()";  
  
    private static XPathFactory xPathFactory = XPathFactory.newInstance();  
  
    /** 
     * Get the W3C NodeList instance associated with the XPath selection 
     * supplied. 
     * <p/> 
     * <b>NOTE</b>: Taken from Milyn Commons. 
     * 
     * @param node  The document node to be searched. 
     * @param xpath The XPath String to be used in the selection. 
     * @return The W3C NodeList instance at the specified location in the 
     *         document, or null. 
     */  
    public static NodeList getNodeList(Node node, String xpath) {  
        if (node == null) {  
            throw new IllegalArgumentException(  
                    "null 'document' arg in method call.");  
        } else if (xpath == null) {  
            throw new IllegalArgumentException(  
                    "null 'xpath' arg in method call.");  
        }  
        try {  
            XPath xpathEvaluater = xPathFactory.newXPath();  
  
            if (xpath.endsWith(ELEMENT_NAME_FUNC)) {  
                return (NodeList) xpathEvaluater.evaluate(xpath.substring(0,  
                        xpath.length() - ELEMENT_NAME_FUNC.length()), node,  
                        XPathConstants.NODESET);  
            } else {  
                return (NodeList) xpathEvaluater.evaluate(xpath, node,  
                        XPathConstants.NODESET);  
            }  
        } catch (XPathExpressionException e) {  
            throw new IllegalArgumentException("bad 'xpath' expression ["  
                    + xpath + "].");  
        }  
    }  
  
    /** 
     * Get the W3C Node instance associated with the XPath selection supplied. 
     * <p/> 
     * <b>NOTE</b>: Taken from Milyn Commons. 
     * 
     * @param node  The document node to be searched. 
     * @param xpath The XPath String to be used in the selection. 
     * @return The W3C Node instance at the specified location in the document, 
     *         or null. 
     */  
    public static Node getNode(Node node, String xpath) {  
        NodeList nodeList = getNodeList(node, xpath);  
  
        if (nodeList == null || nodeList.getLength() == 0) {  
            return null;  
        } else {  
            return nodeList.item(0);  
        }  
    }  
  
    /** 
     * Get the name from the supplied element. 
     * <p/> 
     * Returns the {@link Node#getLocalName() localName} of the element 
     * if set (namespaced element), otherwise the 
     * element's {@link Element#getTagName() tagName} is returned. 
     * <p/> 
     * <b>NOTE</b>: Taken from Milyn Smooks. 
     * 
     * @param element The element. 
     * @return The element name. 
     */  
    public static String getName(Element element) {  
  
        String name = element.getLocalName();  
  
        if(name != null) {  
            return name;  
        } else {  
            return element.getTagName();  
        }  
    }  
    /** 
     * Copy child node references from source to target. 
     * @param source Source Node. 
     * @param target Target Node. 
     */  
    public static void copyChildNodes(Node source, Node target) {  
          
        List nodeList = copyNodeList(source.getChildNodes());  
        int childCount = nodeList.size();  
          
        for(int i = 0; i < childCount; i++) {  
            target.appendChild((Node)nodeList.get(i));  
        }  
    }  
    
    /**
     * 
     * 功能:把dom文件转换为xml字符串   <br/>
     * @param document w3c document
     * @return
     */
    public static String toStringFromDoc(Document document) {  
        String result = null;  
  
        if (document != null) {  
            StringWriter strWtr = new StringWriter();  
            StreamResult strResult = new StreamResult(strWtr);  
            TransformerFactory tfac = TransformerFactory.newInstance();  
            try {  
                Transformer t = tfac.newTransformer();
                t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                t.setOutputProperty(OutputKeys.INDENT, "yes");
                t.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,
                // text
                t.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "4");
                t.transform(new DOMSource(document.getDocumentElement()),
                        strResult);
            } catch (Exception e) {
                System.err.println("XML.toString(Document): " + e);
            }
            result = strResult.getWriter().toString();
            try {
                strWtr.close();
            } catch (IOException e) {}
        }

        return result;
    }


    /**
     *
     * 功能:将 w3c document 转换为 dom4j document  <br/>
     * @param document
     * @return
     */
	public org.dom4j.Document transfer2Dom4jDoc(Document document) {
		if (document == null) {
			return (null);
		}
		org.dom4j.io.DOMReader xmlReader = new org.dom4j.io.DOMReader();
		return (xmlReader.read(document));
	}
     /**
     *
     * 功能: dom4j Document 转换成 w3c document <br/>
     */
	public Document transfer2w3cDoc(org.dom4j.Document document) throws ParserConfigurationException, SAXException, IOException {
		if (document == null) {
			return (null);
		}
		StringReader reader = new StringReader(document.asXML());
		org.xml.sax.InputSource source = new org.xml.sax.InputSource(reader);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		return (documentBuilder.parse(source));
	}

	public static String elementAttrValue(org.dom4j.Element element, String string) {
		
		return null;
	}
	
}  
