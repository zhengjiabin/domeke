// Copyright (C) 1998-2001 by Jason Hunter <jhunter_AT_acm_DOT_org>.
// All rights reserved.  Use of this class is limited.
// Please see the LICENSE for more information.

package com.domeke.app.cos;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * A request wrapper to support MultipartFilter. The filter capability requires
 * Servlet API 2.3.
 * <p>
 * See Jason Hunter's June 2001 article in JavaWorld for a full explanation of
 * the class usage.
 *
 * @author <b>Jason Hunter</b>, Copyright &#169; 2001
 * @version 1.1, 2002/11/15, added getOriginalFileName() to match
 *          MultipartRequest
 * @version 1.0, 2001/06/19
 */
public class MultipartWrapper extends HttpServletRequestWrapper {

	MultipartRequest mreq = null;

	public MultipartWrapper(HttpServletRequest req, String dir)
			throws IOException {
		super(req);
		mreq = new MultipartRequest(req, dir);
	}
}
