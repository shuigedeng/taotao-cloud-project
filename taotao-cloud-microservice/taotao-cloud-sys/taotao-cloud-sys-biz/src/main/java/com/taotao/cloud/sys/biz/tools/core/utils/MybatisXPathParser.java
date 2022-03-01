package com.taotao.cloud.sys.biz.tools.core.utils;

import com.taotao.cloud.sys.biz.tools.core.exception.ToolException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MybatisXPathParser {

    private final Document document;
    private boolean validation;
    private EntityResolver entityResolver;
    private Properties variables;
    private XPath xpath;

    public MybatisXPathParser(String xml) {
        commonConstructor(false, null, null);
        this.document = createDocument(new InputSource(new StringReader(xml)));
    }

    public MybatisXPathParser(Reader reader) {
        commonConstructor(false, null, null);
        this.document = createDocument(new InputSource(reader));
    }

    public MybatisXPathParser(InputStream inputStream) {
        commonConstructor(false, null, null);
        this.document = createDocument(new InputSource(inputStream));
    }

    public MybatisXPathParser(Document document) {
        commonConstructor(false, null, null);
        this.document = document;
    }

    public MybatisXPathParser(String xml, boolean validation) {
        commonConstructor(validation, null, null);
        this.document = createDocument(new InputSource(new StringReader(xml)));
    }

    public MybatisXPathParser(Reader reader, boolean validation) {
        commonConstructor(validation, null, null);
        this.document = createDocument(new InputSource(reader));
    }

    public MybatisXPathParser(InputStream inputStream, boolean validation) {
        commonConstructor(validation, null, null);
        this.document = createDocument(new InputSource(inputStream));
    }

    public MybatisXPathParser(Document document, boolean validation) {
        commonConstructor(validation, null, null);
        this.document = document;
    }

    public MybatisXPathParser(String xml, boolean validation, Properties variables) {
        commonConstructor(validation, variables, null);
        this.document = createDocument(new InputSource(new StringReader(xml)));
    }

    public MybatisXPathParser(Reader reader, boolean validation, Properties variables) {
        commonConstructor(validation, variables, null);
        this.document = createDocument(new InputSource(reader));
    }

    public MybatisXPathParser(InputStream inputStream, boolean validation, Properties variables) {
        commonConstructor(validation, variables, null);
        this.document = createDocument(new InputSource(inputStream));
    }

    public MybatisXPathParser(Document document, boolean validation, Properties variables) {
        commonConstructor(validation, variables, null);
        this.document = document;
    }

    public MybatisXPathParser(String xml, boolean validation, Properties variables, EntityResolver entityResolver) {
        commonConstructor(validation, variables, entityResolver);
        this.document = createDocument(new InputSource(new StringReader(xml)));
    }

    public MybatisXPathParser(Reader reader, boolean validation, Properties variables, EntityResolver entityResolver) {
        commonConstructor(validation, variables, entityResolver);
        this.document = createDocument(new InputSource(reader));
    }

    public MybatisXPathParser(InputStream inputStream, boolean validation, Properties variables, EntityResolver entityResolver) {
        commonConstructor(validation, variables, entityResolver);
        this.document = createDocument(new InputSource(inputStream));
    }

    public MybatisXPathParser(Document document, boolean validation, Properties variables, EntityResolver entityResolver) {
        commonConstructor(validation, variables, entityResolver);
        this.document = document;
    }

    public void setVariables(Properties variables) {
        this.variables = variables;
    }

    public String evalString(String expression) {
        return evalString(document, expression);
    }

    public String evalString(Object root, String expression) {
        String result = (String) evaluate(expression, root, XPathConstants.STRING);
//        result = PropertyParser.parse(result, variables);     // 占位符功能没有搬过来
        return result;
    }

    public Boolean evalBoolean(String expression) {
        return evalBoolean(document, expression);
    }

    public Boolean evalBoolean(Object root, String expression) {
        return (Boolean) evaluate(expression, root, XPathConstants.BOOLEAN);
    }

    public Short evalShort(String expression) {
        return evalShort(document, expression);
    }

    public Short evalShort(Object root, String expression) {
        return Short.valueOf(evalString(root, expression));
    }

    public Integer evalInteger(String expression) {
        return evalInteger(document, expression);
    }

    public Integer evalInteger(Object root, String expression) {
        return Integer.valueOf(evalString(root, expression));
    }

    public Long evalLong(String expression) {
        return evalLong(document, expression);
    }

    public Long evalLong(Object root, String expression) {
        return Long.valueOf(evalString(root, expression));
    }

    public Float evalFloat(String expression) {
        return evalFloat(document, expression);
    }

    public Float evalFloat(Object root, String expression) {
        return Float.valueOf(evalString(root, expression));
    }

    public Double evalDouble(String expression) {
        return evalDouble(document, expression);
    }

    public Double evalDouble(Object root, String expression) {
        return (Double) evaluate(expression, root, XPathConstants.NUMBER);
    }

    public List<MybatisXNode> evalNodes(String expression) {
        return evalNodes(document, expression);
    }

    public List<MybatisXNode> evalNodes(Object root, String expression) {
        List<MybatisXNode> xnodes = new ArrayList<MybatisXNode>();
        NodeList nodes = (NodeList) evaluate(expression, root, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            xnodes.add(new MybatisXNode(this, nodes.item(i), variables));
        }
        return xnodes;
    }

    public MybatisXNode evalNode(String expression) {
        return evalNode(document, expression);
    }

    public MybatisXNode evalNode(Object root, String expression) {
        Node node = (Node) evaluate(expression, root, XPathConstants.NODE);
        if (node == null) {
            return null;
        }
        return new MybatisXNode(this, node, variables);
    }

    private Object evaluate(String expression, Object root, QName returnType) {
        try {
            return xpath.evaluate(expression, root, returnType);
        } catch (Exception e) {
            throw new ToolException("Error evaluating XPath.  Cause: " + e, e);
        }
    }

    private Document createDocument(InputSource inputSource) {
        // important: this must only be called AFTER common constructor
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(validation);

            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(false);
            factory.setCoalescing(false);
            factory.setExpandEntityReferences(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(entityResolver);
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(SAXParseException exception) throws SAXException {
                }
            });
            return builder.parse(inputSource);
        } catch (Exception e) {
            throw new ToolException("Error creating document instance.  Cause: " + e, e);
        }
    }

    private void commonConstructor(boolean validation, Properties variables, EntityResolver entityResolver) {
        this.validation = validation;
        this.entityResolver = entityResolver;
        this.variables = variables;
        XPathFactory factory = XPathFactory.newInstance();
        this.xpath = factory.newXPath();
    }

}
