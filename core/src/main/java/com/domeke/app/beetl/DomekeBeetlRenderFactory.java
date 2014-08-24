/**
 * 
 */
package com.domeke.app.beetl;

import java.io.IOException;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.jfinal.BeetlRender;
import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.jfinal.render.IMainRenderFactory;
import com.jfinal.render.Render;

/**
 * @author lijiasen
 *
 */
public class DomekeBeetlRenderFactory extends BeetlRenderFactory implements IMainRenderFactory {
	public static String viewExtension = ".html";
	public static GroupTemplate groupTemplate = null;

	public DomekeBeetlRenderFactory()
	{

		if (groupTemplate != null)
		{
			groupTemplate.close();
		}

		try
		{
			Configuration cfg = Configuration.defaultConfiguration();
			WebAppResourceLoader resourceLoader = new WebAppResourceLoader("/tempalte");
			groupTemplate = new GroupTemplate(resourceLoader, cfg);

		}
		catch (IOException e)
		{
			throw new RuntimeException("加载GroupTemplate失败", e);
		}

	}

	public Render getRender(String view)
	{
		return new BeetlRender(groupTemplate, view);
	}

	public String getViewExtension()
	{
		return viewExtension;
	}

	
}
