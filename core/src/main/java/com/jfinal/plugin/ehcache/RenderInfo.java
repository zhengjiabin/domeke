/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.plugin.ehcache;

import java.io.Serializable;
import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.JspRender;
import com.jfinal.render.Render;
import com.jfinal.render.XmlRender;

/**
 * RenderInfo.
 */
public class RenderInfo implements Serializable {
	
	private static final long serialVersionUID = -7299875545092102194L;
	
	private String view;
	private Integer renderType;
	
	public RenderInfo(Render render) {
		if (render == null)
			throw new IllegalArgumentException("Render can not be null.");
		
		view = render.getView();
		if (render instanceof FreeMarkerRender)
			renderType = RenderType.FREE_MARKER_RENDER;
		else if (render instanceof JspRender)
			renderType = RenderType.JSP_RENDER;
		else if (render instanceof XmlRender)
			renderType = RenderType.XML_RENDER;
		else
			throw new IllegalArgumentException("CacheInterceptor can not support the render of the type : " + render.getClass().getName());
	}
	
	public Render createRender() {
		if (renderType == RenderType.FREE_MARKER_RENDER)
			return new FreeMarkerRender(view);
		else if (renderType == RenderType.JSP_RENDER)
			return new JspRender(view);
		else if (renderType == RenderType.XML_RENDER)
			return new XmlRender(view);
		throw new IllegalArgumentException("CacheInterceptor can not support the renderType of the value : " + renderType);
	}
}
