package core;

import static org.junit.Assert.*;

import org.junit.Test;

import com.domeke.app.base.cofig.DruidDatasouceUtil;
import com.jfinal.plugin.druid.DruidPlugin;

public class TestDruidDatasouceUtil {

	@Test
	public void testGetDruidPlugin() {
		DruidPlugin druidPlugin = DruidDatasouceUtil.getDruidPlugin();
	}

}
