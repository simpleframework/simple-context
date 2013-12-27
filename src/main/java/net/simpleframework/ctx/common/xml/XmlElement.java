package net.simpleframework.ctx.common.xml;

import java.util.Iterator;

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
	private final Element element;

	public XmlElement(final Element element) {
		this.element = element;
	}

	public Element getElement() {
		return element;
	}

	public String attributeValue(final String name) {
		return element.getAttribute(name);
	}

	public XmlElement addAttribute(final String name, final String value) {
		element.setAttribute(name, value);
		return this;
	}

	public String getName() {
		return element.getTagName();
	}

	public String getText() {
		final String txt = element.getTextContent();
		if (txt != null) {
			return txt.trim();
		}
		return "";
	}

	public void setText(final String text) {
		element.setTextContent(text);
	}

	public String elementText(final String elementName) {
		final XmlElement xmlElement = element(elementName);
		return xmlElement == null ? "" : xmlElement.getText();
	}

	public XmlElement getParent() {
		return new XmlElement((Element) element.getParentNode());
	}

	public void remove(final XmlElement removeElement) {
		if (removeElement != null) {
			element.removeChild(removeElement.getElement());
		}
	}

	public void clearContent() {
		final NodeList nl = element.getChildNodes();
		if (nl == null) {
			return;
		}
		while (nl.getLength() > 0) {
			element.removeChild(nl.item(0));
		}
	}

	public XmlElement addElement(final String elementName) {
		final Element nElement = element.getOwnerDocument().createElement(elementName);
		return new XmlElement((Element) element.appendChild(nElement));
	}

	public void add(final XmlElement xmlElement) {
		element.appendChild(xmlElement.getElement());
	}

	public void addCDATA(final String cdata) {
		element.appendChild(element.getOwnerDocument().createCDATASection(cdata));
	}

	public XmlElement element(final String elementName) {
		final Iterator<XmlElement> it = elementIterator(elementName);
		return it.hasNext() ? it.next() : null;
	}

	public Iterator<XmlElement> elementIterator() {
		return elementIterator(null);
	}

	public Iterator<XmlElement> elementIterator(final String elementName) {
		final NodeList nl = element.getChildNodes();
		final int length = nl != null ? nl.getLength() : 0;
		return new _Iterator<XmlElement>() {
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

	public Attri attribute(final String attributeName) {
		final Attr attr = element.getAttributeNode(attributeName);
		return attr == null ? null : new Attri(attr);
	}

	public void remove(final Attri attri) {
		element.removeAttribute(attri.getName());
	}

	public Iterator<Attri> attributeIterator() {
		final NamedNodeMap attributes = element.getAttributes();
		final int length = attributes != null ? attributes.getLength() : 0;
		return new _Iterator<Attri>() {
			@Override
			public boolean hasNext() {
				return ++i < length;
			}

			@Override
			public Attri next() {
				return new Attri((Attr) attributes.item(i));
			}
		};
	}

	public static class Attri {
		private final Attr attr;

		public Attr getAttr() {
			return attr;
		}

		public Attri(final Attr attr) {
			this.attr = attr;
		}

		public String getName() {
			return attr.getName();
		}

		public String getValue() {
			return attr.getValue();
		}

		public void setValue(final String value) {
			attr.setValue(value);
		}
	}

	abstract class _Iterator<T> implements Iterator<T> {
		int i = -1;

		@Override
		public void remove() {
		}
	}
}
