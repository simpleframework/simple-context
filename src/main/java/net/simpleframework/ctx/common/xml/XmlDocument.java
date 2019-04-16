package net.simpleframework.ctx.common.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import net.simpleframework.common.FileUtils;
import net.simpleframework.common.IoUtils;
import net.simpleframework.common.object.ObjectEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class XmlDocument extends ObjectEx implements java.io.Serializable {

	protected Document document;

	public XmlDocument(final Document document) {
		this.document = document;
	}

	public XmlDocument(final Reader reader) {
		try {
			this.document = parse(reader == null ? null : new InputSource(reader));
		} catch (final Throwable e) {
			throw XmlDocumentException.of(e);
		}
	}

	public XmlDocument(final String xml) {
		this(toReader(xml));
	}

	public XmlDocument(final InputStream inputStream) {
		try {
			if (inputStream == null) {
				this.document = parse(null);
			} else {
				try {
					this.document = parse(new InputSource(inputStream));
				} catch (final Exception e) {
					getLog().warn(e);
					// 按utf-8再尝试一次
					this.document = parse(new InputSource(
							toReader(IoUtils.getStringFromInputStream(inputStream, "UTF-8"))));
				}
			}
		} catch (final Throwable e) {
			throw XmlDocumentException.of(e);
		}
	}

	public XmlDocument(final URL url) throws IOException {
		this(url.openStream());
	}

	public XmlDocument(final File file) throws FileNotFoundException {
		this(file.exists() ? new FileInputStream(file) : (InputStream) null);
	}

	private Document parse(final InputSource inputSource)
			throws ParserConfigurationException, SAXException, IOException {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder builder = factory.newDocumentBuilder();
		if (inputSource == null) {
			return builder.newDocument();
		}

		factory.setNamespaceAware(isNamespaceAware());
		factory.setValidating(isValidating());
		factory.setExpandEntityReferences(false);
		final EntityResolver resolver = getEntityResolver();
		if (resolver != null) {
			builder.setEntityResolver(resolver);
		}
		return builder.parse(inputSource);
	}

	public Document getDocument() {
		return document;
	}

	public XmlElement getRoot() {
		return getRoot(getRootTag());
	}

	public XmlElement getRoot(final String tag) {
		Element root = document.getDocumentElement();
		if (root == null) {
			root = document.createElement(tag);
			document.appendChild(root);
		}
		return new XmlElement(root);
	}

	protected String getRootTag() {
		return "root";
	}

	protected boolean isValidating() {
		return false;
	}

	protected boolean isNamespaceAware() {
		return false;
	}

	protected EntityResolver getEntityResolver() {
		return null;
	}

	@Override
	public String toString() {
		return toString(document);
	}

	static String toString(final Node node) {
		try {
			final TransformerFactory tf = TransformerFactory.newInstance();
			// tf.setAttribute("indent-number", new Integer(2));

			final Transformer trans = tf.newTransformer();
			trans.setOutputProperty(OutputKeys.METHOD, "xml");
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			final StringWriter sWriter = new StringWriter();
			trans.transform(new DOMSource(node), new StreamResult(sWriter));
			return sWriter.toString();
		} catch (final Throwable e) {
			throw XmlDocumentException.of(e);
		}
	}

	public void saveToFile(final File targetFile) throws IOException {
		FileUtils.copyFile(new ByteArrayInputStream(toString().getBytes()), targetFile);
		// FileWriter fWriter = null;
		// try {
		// fWriter = new FileWriter(targetFile);
		// fWriter.write(toString());
		// } finally {
		// if (fWriter != null) {
		// fWriter.close();
		// }
		// }
	}

	private static Reader toReader(final String xString) {
		return new StringReader(stripNonValidXMLCharacters(xString));
	}

	private static String stripNonValidXMLCharacters(final String in) {
		final StringBuilder out = new StringBuilder();
		char current;
		if (in == null || ("".equals(in))) {
			return "";
		}
		for (int i = 0; i < in.length(); i++) {
			current = in.charAt(i);
			if ((current == 0x9) || (current == 0xA) || (current == 0xD)
					|| ((current >= 0x20) && (current <= 0xD7FF))
					|| ((current >= 0xE000) && (current <= 0xFFFD))
					|| ((current >= 0x10000) && (current <= 0x10FFFF))) {
				out.append(current);
			}
		}
		return out.toString();
	}

	private static final long serialVersionUID = 4369298335478898560L;
}
