package net.simpleframework.ctx.common.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import net.simpleframework.common.AlgorithmUtils;
import net.simpleframework.common.FileUtils;
import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.object.DescriptionObject;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AttachmentFile extends DescriptionObject<AttachmentFile> implements Serializable {
	private final File attachment;

	/* 摘要值 */
	private String md5;

	/* 标题 */
	private String topic;

	/* 类型 */
	private int type;

	/* 扩展名 */
	private String ext;

	/* 上传时间 */
	private Date createDate;

	/* 下载次数 */
	private int downloads;

	/* 文件大小 */
	private long size;

	/* 描述 */
	private String description;

	private String id;

	public AttachmentFile(final File attachment) throws IOException {
		this(attachment, null);
	}

	public AttachmentFile(final File attachment, final String md5) throws IOException {
		this.attachment = attachment;
		this.md5 = StringUtils.hasText(md5) ? md5 : AlgorithmUtils.md5Hex(new FileInputStream(
				attachment));
	}

	public File getAttachment() throws IOException {
		return attachment;
	}

	public String getTopic() {
		if (StringUtils.hasText(topic)) {
			return topic;
		}
		try {
			return getAttachment().getName();
		} catch (final IOException e) {
			getLog().warn(e);
			return e.getMessage();
		}
	}

	public AttachmentFile setTopic(final String topic) {
		this.topic = topic;
		return this;
	}

	public int getType() {
		return type;
	}

	public AttachmentFile setType(final int type) {
		this.type = type;
		return this;
	}

	public String getExt() {
		if (ext == null) {
			ext = FileUtils.getFilenameExtension(getTopic()).toLowerCase();
		}
		return ext;
	}

	public AttachmentFile setExt(final String ext) {
		this.ext = ext;
		return this;
	}

	public int getDownloads() {
		return downloads;
	}

	public AttachmentFile setDownloads(final int downloads) {
		this.downloads = downloads;
		return this;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public AttachmentFile setDescription(final String description) {
		this.description = description;
		return this;
	}

	public Date getCreateDate() {
		if (createDate == null) {
			createDate = new Date();
		}
		return createDate;
	}

	public AttachmentFile setCreateDate(final Date createDate) {
		this.createDate = createDate;
		return this;
	}

	public String getMd5() {
		return md5;
	}

	public AttachmentFile setMd5(final String md5) {
		this.md5 = md5;
		return this;
	}

	public String getId() {
		if (id == null) {
			id = ID.uuid().toString();
		}
		return id;
	}

	public AttachmentFile setId(final String id) {
		this.id = id;
		return this;
	}

	public long getSize() throws IOException {
		return size > 0 ? size : getAttachment().length();
	}

	public AttachmentFile setSize(final long size) {
		this.size = size;
		return this;
	}

	public String toFilename() {
		String topic = getTopic();
		final String ext = "." + getExt();
		if (!topic.toLowerCase().endsWith(ext)) {
			topic += ext;
		}
		return topic;
	}

	private static final long serialVersionUID = -7838133087121815920L;
}
