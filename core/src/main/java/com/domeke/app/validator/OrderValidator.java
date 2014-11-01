package com.domeke.app.validator;

import com.domeke.app.model.Goods;
import com.domeke.app.model.User;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.validate.Validator;

public class OrderValidator extends Validator{

	@Override
	protected void validate(Controller c) {
		String realname = c.getPara("user.realname");
		if (StrKit.isBlank(realname)) {
			addError("realnameMsg", "收件人姓名必须输入！");
		}
		String address = c.getPara("user.address");
		if (StrKit.isBlank(address)) {
			addError("addressMsg", "收件人地址必须输入！");
		}

		String mobile = c.getPara("user.mobile");
		if (StrKit.isBlank(mobile)) {
			addError("mobileMsg2", "收件人电话必须输入！");
		}		
	}

	@Override
	protected void handleError(Controller c) {
		c.keepModel(User.class);
		Long peas = c.getParaToLong("peas");
		Long goodsnum = c.getParaToLong("goodsnum");
		Long goodsid = c.getParaToLong("goods.goodsid");
		Goods goodsModel = Goods.dao.getUserForId(goodsid);
		Long userid = c.getParaToLong("user.userid");
		User user = User.dao.getUserForId(userid);
		String realname = c.getPara("user.realname");
		Long sumDougprice = goodsModel.getLong("dougprice")*goodsnum;
		Long surplus = peas-sumDougprice;
		if (StrKit.isBlank(realname)){
			user.set("realname", realname);
		}
		String address = c.getPara("user.address");
		if (StrKit.isBlank(address)){
			user.set("address", address);
		}
		String mobile = c.getPara("user.mobile");
		if (StrKit.isBlank(mobile)){
			user.set("mobile", mobile);
		}
		c.setAttr("peas", peas);
		c.setAttr("goodsnum", goodsnum);
		c.setAttr("goods", goodsModel);
		c.setAttr("user", user);
		c.setAttr("sumDougprice", sumDougprice);
		c.setAttr("surplus", surplus);
		c.render("/ShopOderLayout.html");
	}

}
