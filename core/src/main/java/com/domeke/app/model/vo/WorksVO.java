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
	
	private String comment;
	
	private String pageviews;
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPageviews() {
		return pageviews;
	}

	public void setPageviews(String pageviews) {
		this.pageviews = pageviews;
	}

	public String getWorkname() {
		return workname;
	}

	public void setWorkname(String workname) {
		this.workname = workname;
	}

	private String workname;

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
