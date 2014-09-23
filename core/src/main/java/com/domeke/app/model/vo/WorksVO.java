package com.domeke.app.model.vo;

/**
 * 
 * @author
 *
 */
public class WorksVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3683462974089424783L;

	private String worksname;

	private String describle;

	private String cover;

	public String getWorksname() {
		return worksname;
	}

	public void setWorksname(String worksname) {
		this.worksname = worksname;
	}

	public String getDescrible() {
		return describle;
	}

	public void setDescrible(String describle) {
		this.describle = describle;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

}
