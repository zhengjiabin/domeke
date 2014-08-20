package com.domeke.app.tablebind;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.domeke.app.utils.ClassSearcher;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Model;

/**
 * 	SimpleNameStyles 中已有的命名风格以及映射的表明如下表：
 *					 		DEFAULT	 FIRST_LOWER	  UP	            LOWER	  UP_UNDERLINE	    LOWER_UNDERLINE
*DevInfo.java		DevInfo	  devInfo	        DEVINFO	     devinfo	     DEV_INFO	                  dev_info
*	ParamNameStyles 含有构造参数的命名风格：
*							module(test)	lowerModule(test)	upModule(test)	upUnderlineModule(test)	lowerUnderlineModule(test)
*DevInfo.java		test_DevInfo	    test_devinfo			test_DEVINFO			test_DEV_INFO						test_dev_info
*当model到表的映射不符合规范需要单独配置，需要在model上加上TableName注解
* 例如：@TableBind(tableName = "user", pkName = "userid")
*/
public class AutoTableBindPlugin extends ActiveRecordPlugin {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("rawtypes")
	private List<Class<? extends Model>> excludeClasses = Lists.newArrayList();
	private List<String> includeJars = Lists.newArrayList();
	private boolean autoScan = true;
	private boolean includeAllJarsInLib;
	private INameStyle nameStyle;

	public AutoTableBindPlugin(DataSource dataSource) {
		this(dataSource, SimpleNameStyles.DEFAULT);
	}

	/**
	  * @param dataSource
	  * @param nameStyle
	*/
	public AutoTableBindPlugin(DataSource dataSource, INameStyle nameStyle) {
		super(dataSource);
		this.nameStyle = nameStyle;
	}

	public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider) {
		this(dataSourceProvider, SimpleNameStyles.DEFAULT);
	}

	public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider, INameStyle nameStyle) {
		super(dataSourceProvider);
		this.nameStyle = nameStyle;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AutoTableBindPlugin addExcludeClasses(Class<? extends Model>... clazzes) {
		for (Class<? extends Model> clazz : clazzes) {
			excludeClasses.add(clazz);
		}
		return this;
	}

	@SuppressWarnings("rawtypes")
	public AutoTableBindPlugin addExcludeClasses(List<Class<? extends Model>> clazzes) {
		if (clazzes != null) {
			excludeClasses.addAll(clazzes);
		}
		return this;
	}

	public AutoTableBindPlugin addJars(List<String> jars) {
		if (jars != null) {
			includeJars.addAll(jars);
		}
		return this;
	}

	public AutoTableBindPlugin addJars(String... jars) {
		if (jars != null) {
			for (String jar : jars) {
				includeJars.add(jar);
			}
		}
		return this;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean start() {
		List<Class<? extends Model>> modelClasses = ClassSearcher.of(Model.class).injars(includeJars)
				.includeAllJarsInLib(includeAllJarsInLib).search();
		TableBind tb = null;
		for (Class modelClass : modelClasses) {
			if (excludeClasses.contains(modelClass)) {
				continue;
			}
			tb = (TableBind) modelClass.getAnnotation(TableBind.class);
			String tableName;
			if (tb == null) {
				if (!autoScan) {
					continue;
				}
				tableName = nameStyle.name(modelClass.getSimpleName());
				this.addMapping(tableName, modelClass);
				log.debug("addMapping(" + tableName + ", " + modelClass.getName() + ")");
			} else {
				tableName = tb.tableName();
				if (StrKit.notBlank(tb.pkName())) {
					this.addMapping(tableName, tb.pkName(), modelClass);
					log.debug("addMapping(" + tableName + ", " + tb.pkName() + "," + modelClass.getName() + ")");
				} else {
					this.addMapping(tableName, modelClass);
					log.debug("addMapping(" + tableName + ", " + modelClass.getName() + ")");
				}
			}
		}
		return super.start();
	}

	@Override
	public boolean stop() {
		return super.stop();
	}

	public AutoTableBindPlugin autoScan(boolean autoScan) {
		this.autoScan = autoScan;
		return this;
	}

	public AutoTableBindPlugin includeAllJarsInLib(boolean includeAllJarsInLib) {
		this.includeAllJarsInLib = includeAllJarsInLib;
		return this;
	}
}
