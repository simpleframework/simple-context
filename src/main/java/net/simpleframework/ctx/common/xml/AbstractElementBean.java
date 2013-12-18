package net.simpleframework.ctx.common.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.Convert;
import net.simpleframework.common.I18n;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.common.object.ObjectEx;
import net.simpleframework.ctx.common.xml.XmlElement.Attri;
import net.simpleframework.ctx.script.IScriptEval;
import net.simpleframework.ctx.script.ScriptEvalUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractElementBean extends ObjectEx {

	private transient XmlElement beanElement;

	public AbstractElementBean() {
	}

	public AbstractElementBean(final XmlElement beanElement) {
		setBeanElement(beanElement);
	}

	public XmlElement getBeanElement() {
		return beanElement;
	}

	public AbstractElementBean setBeanElement(final XmlElement beanElement) {
		this.beanElement = beanElement;
		return this;
	}

	protected XmlElement child(final String name) {
		return child(getBeanElement(), name);
	}

	protected XmlElement child(final XmlElement parent, final String name) {
		if (parent == null) {
			return null;
		}
		return parent.element(name);
	}

	protected void removeChildren(final XmlElement parent, final String name) {
		if (parent == null) {
			return;
		}
		final Iterator<XmlElement> it = parent.elementIterator(name);
		while (it.hasNext()) {
			parent.remove(it.next());
		}
	}

	protected void removeChildren(final String name) {
		removeChildren(getBeanElement(), name);
	}

	protected void removeElement(final AbstractElementBean bean) {
		XmlElement xmlElement;
		if (bean == null || (xmlElement = bean.getBeanElement()) == null) {
			return;
		}
		final XmlElement parent = xmlElement.getParent();
		if (parent != null) {
			parent.remove(xmlElement);
		}
	}

	protected XmlElement addElement(final String name) {
		return addElement(getBeanElement(), name);
	}

	protected XmlElement addElement(final XmlElement parent, final String name) {
		return parent != null ? parent.addElement(name) : null;
	}

	protected XmlElement addElement(final AbstractElementBean bean) {
		return addElement(getBeanElement(), bean);
	}

	protected XmlElement addElement(final XmlElement parent, final AbstractElementBean bean) {
		if (parent == null || bean == null) {
			return null;
		}
		final XmlElement xmlElement = bean.getBeanElement();
		if (xmlElement != null) {
			// element.setParent(null);
			parent.add(xmlElement);
		}
		return xmlElement;
	}

	protected void setElementAttribute(final String[] names) {
		if (names == null) {
			return;
		}
		for (final String name : names) {
			setElementAttribute(name, BeanUtils.getProperty(this, name));
		}
	}

	protected void setElementAttribute(final String name, final Object object) {
		setElementAttribute(getBeanElement(), name, object);
	}

	protected void setElementAttribute(final XmlElement xmlElement, final String name,
			final Object object) {
		if (xmlElement == null) {
			return;
		}
		String value;
		if (object instanceof Enum) {
			value = ((Enum<?>) object).name();
		} else if (object != null) {
			value = String.valueOf(object);
		} else {
			value = null;
		}
		final Attri attribute = xmlElement.attribute(name);
		if (StringUtils.hasText(value)) {
			if (attribute != null) {
				attribute.setValue(value);
			} else {
				xmlElement.addAttribute(name, value);
			}
		} else if (attribute != null) {
			xmlElement.remove(attribute);
		}
	}

	protected void setElementContent(final String name, final Object object) {
		final XmlElement xmlElement = getBeanElement();
		if (xmlElement == null) {
			return;
		}
		final String value = Convert.toString(object);
		final XmlElement ele = xmlElement.element(name);
		if (StringUtils.hasText(value)) {
			if (ele != null) {
				ele.clearContent();
				ele.addCDATA(value);
			} else {
				xmlElement.addElement(name).addCDATA(value);
			}
		} else {
			xmlElement.remove(ele);
		}
	}

	protected String[] elementAttributes() {
		return null;
	}

	public void syncElement() {
		final Map<String, Object> data = BeanUtils.toMap(this);
		for (final Map.Entry<?, ?> entry : data.entrySet()) {
			final Object o = entry.getValue();
			if (o instanceof AbstractElementBean || o instanceof XmlElement || o instanceof Collection
					|| o instanceof Map) {
				continue;
			}
			final String key = (String) entry.getKey();
			final String[] arr = elementAttributes();
			if (arr != null && ArrayUtils.contains(arr, key)) {
				setElementContent(key, o);
			} else {
				setElementAttribute(key, o);
			}
		}
	}

	public void parseElement() {
		parseElement(null);
	}

	public void parseElement(final IScriptEval scriptEval) {
		final XmlElement xmlElement = getBeanElement();
		if (xmlElement == null) {
			return;
		}
		ArrayList<Attri> removes = null;
		final Iterator<Attri> it = xmlElement.attributeIterator();
		while (it.hasNext()) {
			final Attri attribute = it.next();
			final String name = attribute.getName();
			if (name.contains(":")) {
				continue;
			}
			String val = I18n.replaceI18n(attribute.getValue());
			if (scriptEval != null) {
				val = ScriptEvalUtils.replaceExpr(scriptEval, val);
			}
			try {
				BeanUtils.setProperty(this, name, val);
			} catch (final Exception e) {
				log.warn(e);
				if (isRemoveErrorAttribute()) {
					if (removes == null) {
						removes = new ArrayList<Attri>();
					}
					removes.add(attribute);
				}
			}
		}
		if (removes != null) {
			for (final Attri attribute : removes) {
				xmlElement.remove(attribute);
			}
		}
		final String[] arr = elementAttributes();
		if (arr != null && arr.length > 0) {
			setBeanFromElementAttributes(scriptEval, arr);
		}
	}

	public void setBeanFromElementAttributes(final String[] attributes) {
		setBeanFromElementAttributes(null, attributes);
	}

	public void setBeanFromElementAttributes(final IScriptEval scriptEval, final String[] attributes) {
		if (attributes == null) {
			return;
		}
		final XmlElement xmlElement = getBeanElement();
		for (final String attribute : attributes) {
			final XmlElement xmlElement2 = xmlElement.element(attribute);
			String text;
			if (xmlElement2 == null || !StringUtils.hasText(text = xmlElement2.getText())) {
				continue;
			}
			text = I18n.replaceI18n(text);
			if (scriptEval != null) {
				text = ScriptEvalUtils.replaceExpr(scriptEval, text);
			}
			try {
				BeanUtils.setProperty(this, attribute, text);
			} catch (final Exception e) {
				log.warn(e);
				if (isRemoveErrorAttribute()) {
					xmlElement.remove(xmlElement2);
				}
			}
		}
	}

	protected boolean isRemoveErrorAttribute() {
		return false;
	}

	{
		enableAttributes();
	}
}
