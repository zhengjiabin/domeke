package com.domeke.app.model;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * @author chenzhicong
 *
 */
@TableBind(tableName="login_pic", pkName="picid")
public class LoginPic extends Model<LoginPic> {
	private static final long serialVersionUID = -4324755844654141029L;
	
	public static LoginPic lpDao = new LoginPic();
	
	/**
	 * 获取登录界面图片
	 * @return
	 */
	public String getPicUrl() {
		String url = Db.queryStr("select picurl from login_pic where status='70'");
		return url;
	}
	
	/**
	 * 新增
	 */
	public void saveLoginPic() {
		this.save();
	}
	
	/**
	 * 删除
	 */
	public void deleteLoginPic() {
		this.delete();
	}
	
	/**
	 * 修改状态及名称
	 * @param status
	 */
	public void updateLoginPic(String picId, String picName, String status) {
		Db.update("update login_pic set picname = '" + picName + "', status = '" + status + "' where picid = '" + picId + "'");
	}
	
	public void upLoginPicStatus(String picId, String status) {
		Db.update("update login_pic set status = '" + status + "' where picid = '" + picId + "'");
	}
	public void upLoginPic(){
		this.update();
	}
	
	/**
	 * 分页查询
	 * 
	 * @param pageNumber
	 *            页号
	 * @param pageSize
	 *            页数
	 * @return
	 */
	public Page<LoginPic> findPage(int pageNumber, int pageSize) {
		Page<LoginPic> loginPicPage = this.paginate(pageNumber, pageSize, "select *", "from login_pic");
		return loginPicPage;
	}
	
	public LoginPic findLoginPicById(int picId) {
		LoginPic loginPic = this.findById(picId);		
		return loginPic;
	}
	
	public LoginPic findLoginPicEnable(){
		LoginPic loginPic = this.findFirst("select * from login_pic where status='70'");
		return loginPic;
	}
}
