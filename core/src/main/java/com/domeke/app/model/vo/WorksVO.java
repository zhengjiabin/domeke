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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private String worksname;

	private String describle;

	private String cover;
	
	private String comment;
	
	private String pageviews;
	
	private String comic;
	
	private String workname;
	
	private String desc;
	
	
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


	public String getComic() {
		return comic;
	}

	public void setComic(String comic) {
		this.comic = comic;
	}

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
