package net.simpleframework.ctx.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;

import net.simpleframework.ctx.common.xml.XmlDocument;
import net.simpleframework.ctx.common.xml.XmlElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class XmlContextSettings extends ContextSettings {

	protected XmlDocument document;

	public XmlContextSettings(final InputStream inputStream) {
		document = new XmlDocument(inputStream);
	}

	public XmlContextSettings(final Reader reader) {
		document = new XmlDocument(reader);
	}

	public XmlContextSettings(final File file) throws FileNotFoundException {
		document = new XmlDocument(file);
	}

	public XmlDocument getDocument() {
		return document;
	}

	public XmlElement getRoot() {
		return getDocument().getRoot(getRootTag());
	}

	protected String getRootTag() {
		return "root";
	}

	protected XmlElement element(final XmlElement p, final String tag) {
		XmlElement ele = p.element(tag);
		if (ele == null) {
			ele = p.addElement(tag);
		}
		return ele;
	}

	protected XmlElement element(final String tag) {
		return element(getRoot(), tag);
	}
}
