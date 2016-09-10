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
import net.simpleframework.ctx.script.IScriptEval;
import net.simpleframework.ctx.script.ScriptEvalUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractElementBean extends ObjectEx {

	private XmlElement _element;

	public AbstractElementBean() {
	}

	public AbstractElementBean(final XmlElement element) {
		setElement(element);
	}

	public XmlElement getElement() {
		return _element;
	}

	public AbstractElementBean setElement(final XmlElement element) {
		_element = element;
		return this;
	}

	protected void removeElement(final AbstractElementBean bean) {
		XmlElement element;
		if (bean == null || (element = bean.getElement()) == null) {
			return;
		}
		final XmlElement parent = element.getParent();
		if (parent != null) {
			parent.remove(element);
		}
	}

	protected XmlElement addElement(final AbstractElementBean bean) {
		return addElement(getElement(), bean);
	}

	protected XmlElement addElement(final XmlElement parent, final AbstractElementBean bean) {
		if (parent == null || bean == null) {
			return null;
		}
		final XmlElement element = bean.getElement();
		if (element != null) {
			// element.setParent(null);
			parent.addElement(element);
		}
		return element;
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
		setElementAttribute(getElement(), name, object);
	}

	protected void setElementAttribute(final XmlElement element, final String name,
			final Object object) {
		if (element == null) {
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
		final XmlAttri attribute = element.attribute(name);
		if (StringUtils.hasText(value)) {
			if (attribute != null) {
				attribute.setValue(value);
			} else {
				element.addAttribute(name, value);
			}
		} else if (attribute != null) {
			element.remove(attribute);
		}
	}

	protected void setElementContent(final String name, final Object object) {
		final XmlElement element = getElement();
		if (element == null) {
			return;
		}
		final String value = Convert.toString(object);
		final XmlElement ele = element.element(name);
		if (StringUtils.hasText(value)) {
			if (ele != null) {
				ele.clearContent();
				ele.addCDATA(value);
			} else {
				element.addElement(name).addCDATA(value);
			}
		} else {
			element.remove(ele);
		}
	}

	protected String[] elementAttributes() {
		return null;
	}

	protected boolean syncElement_exclude(final Object o) {
		return o instanceof AbstractElementBean || o instanceof XmlElement || o instanceof Collection
				|| o instanceof Map;
	}

	public void syncElement() {
		final Map<String, Object> data = BeanUtils.toMap(this);
		for (final Map.Entry<?, ?> entry : data.entrySet()) {
			final Object o = entry.getValue();
			if (syncElement_exclude(o)) {
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

	public void parseElement(final IScriptEval scriptEval) {
		final XmlElement element = getElement();
		ArrayList<XmlAttri> removes = null;
		final Iterator<XmlAttri> it = element.attributeIterator();
		while (it.hasNext()) {
			final XmlAttri attribute = it.next();
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
				getLog().warn(e);
				if (isRemoveErrorAttribute()) {
					if (removes == null) {
						removes = new ArrayList<XmlAttri>();
					}
					removes.add(attribute);
				}
			}
		}
		if (removes != null) {
			for (final XmlAttri attribute : removes) {
				element.remove(attribute);
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

	public void setBeanFromElementAttributes(final IScriptEval scriptEval,
			final String[] attributes) {
		if (attributes == null) {
			return;
		}
		final XmlElement element = getElement();
		for (final String attribute : attributes) {
			final XmlElement element2 = element.element(attribute);
			String text;
			if (element2 == null || !StringUtils.hasText(text = element2.getText())) {
				continue;
			}
			text = I18n.replaceI18n(text);
			if (scriptEval != null) {
				text = ScriptEvalUtils.replaceExpr(scriptEval, text);
			}
			try {
				BeanUtils.setProperty(this, attribute, text);
			} catch (final Exception e) {
				getLog().warn(e);
				if (isRemoveErrorAttribute()) {
					element.remove(element2);
				}
			}
		}
	}

	@Override
	public AbstractElementBean clone() {
		return BeanUtils.clone(this);
	}

	protected boolean isRemoveErrorAttribute() {
		return false;
	}
}
