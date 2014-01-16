package org.iti.jxkh.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Jxkh_WritingFile implements java.io.Serializable {

	/**
	 *  @author SongYu
	 */
	private static final long serialVersionUID = -9134065280580020320L;

	private Long id;// 序号
	private String name;// 文件名
	private String path;// 路径
	private String submitDate;// 上传日期
	private String belongType;// 所属类型
	private String submitName;//提交人姓名
	private Jxkh_Writing writing;// 所属著作
	
	public Jxkh_WritingFile() {
		super();
	}

	public Jxkh_WritingFile(Long id, String name, String path,
			String submitDate, String belongType, String submitName,
			Jxkh_Writing writing) {
		super();
		this.id = id;
		this.name = name;
		this.path = path;
		this.submitDate = submitDate;
		this.belongType = belongType;
		this.submitName = submitName;
		this.writing = writing;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length = 500)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 200)
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(length = 50)
	public String getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}

	@Column(length = 50)
	public String getBelongType() {
		return belongType;
	}

	public void setBelongType(String belongType) {
		this.belongType = belongType;
	}

	@Column(length = 50)
	public String getSubmitName() {
		return submitName;
	}

	public void setSubmitName(String submitName) {
		this.submitName = submitName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WRITING", nullable = true, insertable = true, updatable = true)
	public Jxkh_Writing getWriting() {
		return writing;
	}

	public void setWriting(Jxkh_Writing writing) {
		this.writing = writing;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		return result;
//	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jxkh_WritingFile other = (Jxkh_WritingFile) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
