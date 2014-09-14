package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * @author 陈智聪
 * DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `menuid` bigint(20) NOT NULL AUTO_INCREMENT,
  `menuname` varchar(64) DEFAULT NULL,
  `actionkey` varchar(256) DEFAULT NULL,
  `top` char(2) DEFAULT NULL,
  `sortnum` int(11) DEFAULT NULL,
  `parentmenuid` bigint(20) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `creater` varchar(32) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`menuid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
 */
@TableBind(tableName="menu", pkName="menuid")
public class Menu extends Model<Menu> {
	
	public static Menu menuDao = new Menu();
	/**
	 * 获取我的方舟主菜单
	 * @return
	 */
	public List<Menu> getMenuMP(){
		List<Menu> menuMPList = this.find("select * from menu where menutype = '2' order by sortnum");
		return menuMPList;
	}
	/**
	 * 获取商城主菜单
	 * @return
	 */
	public List<Menu> getMenuShop(){
		List<Menu> menuShopList = this.find("select * from menu where menutype = '4' and top = '1' order by sortnum");
		return menuShopList;
	}
	
	/**
	 * 获取社区主菜单
	 * @return
	 */
	public List<Menu> getMenuForum(){
		List<Menu> menuForumList = this.find("select * from menu where menutype = '3' and top = '1' order by sortnum");
		return menuForumList;
	}

	/**
	 * 新增菜单
	 */
	public void saveMenu(){
		this.save();
	}
	
	/**
	 * 查询菜单列表
	 * @return 查询列表
	 */
	public List<Menu> selectMenu(){
		List<Menu> menuList = this.find("select * from menu order by sortnum");
		return menuList;
	}
	
	/**
	 * 获取一级菜单菜单
	 * @return 返回一级次菜单
	 */
	public List<Menu> getTopMenu(){
		List<Menu> menuOneMenu = this.find("select * from menu where menutype = '1' and top = '1' order by sortnum");
		return menuOneMenu;
	}
	
	/**
	 * 获取top级的菜单
	 * @param top 级次
	 * @param parentmenuid
	 * @return
	 */
	public List<Menu> getTwoMenu(String parentmenuid){
		List<Menu> menuTwoMenu = this.find("select * from menu where menutype = '1' and top = '2' and parentmenuid = ?", parentmenuid);
		return menuTwoMenu;
	}
	
	/**
	 * 根据menuid查询菜单
	 * @param menuid 参数
	 * @return 返回menu
	 */
	public Menu selectMenuById(int menuid){
		Menu menu = this.findById(menuid);
		return menu;
	}
	
	/**
	 * 更新菜单
	 */
	public void updateMenu(){
		this.update();
	}
	
	/**
	 * 删除菜单
	 * @param menuid 菜单id
	 */
	public void deleteMenu(int menuid){
		this.deleteById(menuid);
	}
	
	/**
	 * 删除缓存
	 */
	public void removeCache(){
		CacheKit.removeAll("Menu");
		CacheKit.removeAll("menuList");
	}	
}
