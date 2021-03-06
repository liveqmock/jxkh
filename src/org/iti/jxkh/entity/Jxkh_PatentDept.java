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
public class Jxkh_PatentDept implements java.io.Serializable {

	/**
	 * @author c
	 */
	private static final long serialVersionUID = 2365439152296255419L;

	private Long id;// 序号
	private String name;// 部门名称
	private String deptId;// 部门编号
	private Integer rank;// 部门排名
	private Jxkh_Patent patent;// 所属知识产权
	private Float score;// 部门绩分

	public Jxkh_PatentDept() {
		super();
	}

	public Jxkh_PatentDept(Long id, String name, String deptId, Integer rank,
			Jxkh_Patent patent, Float score) {
		super();
		this.id = id;
		this.name = name;
		this.deptId = deptId;
		this.rank = rank;
		this.patent = patent;
		this.score = score;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 50)
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	@Column
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PATENT", nullable = true, insertable = true, updatable = true)
	public Jxkh_Patent getPatent() {
		return patent;
	}

	public void setPatent(Jxkh_Patent patent) {
		this.patent = patent;
	}

	@Column
	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jxkh_PatentDept other = (Jxkh_PatentDept) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
