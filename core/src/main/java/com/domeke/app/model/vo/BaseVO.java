/**
 * 
 */
package com.domeke.app.model.vo;

import java.io.Serializable;

/**
 * @author lijiasen@domeke.com
 *
 */
public class BaseVO implements Serializable {

	private static final long serialVersionUID = 1L;

	public long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
