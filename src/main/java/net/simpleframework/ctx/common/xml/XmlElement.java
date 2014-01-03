package net.simpleframework.ctx.common.xml;

import java.util.Iterator;

import net.simpleframework.common.coll.CollectionUtils.AbstractIterator;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class XmlElement {
	private final Element _element;

	public XmlElement(final Element element) {
		this._element = element;
	}

	public Element getElement() {
		return _element;
	}

	public String attributeValue(final String name) {
		return _element.getAttribute(name);
	}

	public XmlElement addAttribute(final String name, final String value) {
		_element.setAttribute(name, value);
		return this;
	}

	public String getName() {
		return _element.getTagName();
	}

	public String getText() {
		final String txt = _element.getTextContent();
		return txt != null ? txt.trim() : "";
	}

	public void setText(final String text) {
		_element.setTextContent(text);
	}

	public String elementText(final String elementName) {
		final XmlElement xmlElement = element(elementName);
		return xmlElement == null ? "" : xmlElement.getText();
	}

	public XmlElement getParent() {
		return new XmlElement((Element) _element.getParentNode());
	}

	public void remove(final XmlElement removeElement) {
		if (removeElement != null) {
			_element.removeChild(removeElement.getElement());
		}
	}

	public void clearContent() {
		final NodeList nl = _element.getChildNodes();
		if (nl == null) {
			return;
		}
		while (nl.getLength() > 0) {
			_element.removeChild(nl.item(0));
		}
	}

	public XmlElement addElement(final String elementName) {
		final Element nElement = _element.getOwnerDocument().createElement(elementName);
		return new XmlElement((Element) _element.appendChild(nElement));
	}

	public void add(final XmlElement xmlElement) {
		_element.appendChild(xmlElement.getElement());
	}

	public void addCDATA(final String cdata) {
		_element.appendChild(_element.getOwnerDocument().createCDATASection(cdata));
	}

	public XmlElement element(final String elementName) {
		final Iterator<XmlElement> it = elementIterator(elementName);
		return it.hasNext() ? it.next() : null;
	}

	public XmlElement element() {
		return element(null);
	}

	public Iterator<XmlElement> elementIterator() {
		return elementIterator(null);
	}

	public Iterator<XmlElement> elementIterator(final String elementName) {
		final NodeList nl = _element.getChildNodes();
		final int length = nl != null ? nl.getLength() : 0;
		return new AbstractIterator<XmlElement>() {
			private Node node;

			@Override
			public boolean hasNext() {
				while (++i < length) {
					if ((node = nl.item(i)) instanceof Element) {
						if (elementName == null || elementName.equals(((Element) node).getTagName())) {
							return true;
						}
					} else {
						node = null;
						continue;
					}
				}
				return false;
			}

			@Override
			public XmlElement next() {
				return node == null ? null : new XmlElement((Element) node);
			}
		};
	}

	public XmlAttri attribute(final String attributeName) {
		final Attr attr = _element.getAttributeNode(attributeName);
		return attr == null ? null : new XmlAttri(attr);
	}

	public void remove(final XmlAttri attri) {
		_element.removeAttribute(attri.getName());
	}

	public Iterator<XmlAttri> attributeIterator() {
		final NamedNodeMap attributes = _element.getAttributes();
		final int length = attributes != null ? attributes.getLength() : 0;
		return new AbstractIterator<XmlAttri>() {
			private Attr attr;

			@Override
			public boolean hasNext() {
				while (++i < length) {
					attr = (Attr) attributes.item(i);
					if (attr != null) {
						return true;
					}
				}
				return false;
			}

			@Override
			public XmlAttri next() {
				return attr == null ? null : new XmlAttri(attr);
			}
		};
	}
}
