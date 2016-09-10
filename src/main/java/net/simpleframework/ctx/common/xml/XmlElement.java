package net.simpleframework.ctx.common.xml;

import java.util.Iterator;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.CollectionUtils.AbstractIterator;
import net.simpleframework.common.object.ObjectEx;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class XmlElement extends ObjectEx {
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

	public String getTagName() {
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
		final XmlElement element = element(elementName);
		return element == null ? "" : element.getText();
	}

	public XmlElement getParent() {
		return new XmlElement((Element) _element.getParentNode());
	}

	public XmlElement remove(final XmlElement removeElement) {
		if (removeElement != null) {
			final Element _element2 = removeElement.getElement();
			Node node;
			while ((node = _element2.getNextSibling()) instanceof Text) {
				if (!StringUtils.hasText(node.getTextContent())) {
					_element.removeChild(node);
				}
			}
			while ((node = _element2.getPreviousSibling()) instanceof Text) {
				if (!StringUtils.hasText(node.getTextContent())) {
					_element.removeChild(node);
				}
			}
			_element.removeChild(_element2);
		}
		return this;
	}

	public XmlElement remove(final String elementName) {
		final Iterator<XmlElement> it = elementIterator(elementName);
		while (it.hasNext()) {
			remove(it.next());
		}
		return this;
	}

	public XmlElement clearContent() {
		final NodeList nl = _element.getChildNodes();
		while (nl.getLength() > 0) {
			_element.removeChild(nl.item(0));
		}
		return this;
	}

	public XmlElement addElement(final String elementName) {
		final Element nElement = _element.getOwnerDocument().createElement(elementName);
		return new XmlElement((Element) _element.appendChild(nElement));
	}

	public XmlElement addElement(final XmlElement element) {
		_element.appendChild(element.getElement());
		return this;
	}

	public XmlElement addCDATA(final String cdata) {
		_element.appendChild(_element.getOwnerDocument().createCDATASection(cdata));
		return this;
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
				node = null;
				while (++i < length) {
					if ((node = nl.item(i)) instanceof Element) {
						if (elementName == null || elementName.equals(((Element) node).getTagName())) {
							return true;
						}
					} else {
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

	public XmlElement remove(final XmlAttri attri) {
		_element.removeAttribute(attri.getName());
		return this;
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

	@Override
	public String toString() {
		return XmlDocument.toString(getElement());
	}
}
