package core;

import org.junit.Test;

import com.domeke.app.base.config.DruidDatasouceUtil;
import com.jfinal.plugin.druid.DruidPlugin;

public class TestDruidDatasouceUtil {

	@Test
	public void testGetDruidPlugin() {
		DruidPlugin druidPlugin = DruidDatasouceUtil.getDruidPlugin();
		druidPlugin.getDataSource();
	}

}
