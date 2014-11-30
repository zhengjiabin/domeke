package com.jfinal.kit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domeke.app.model.Goods;
import com.domeke.app.model.Works;
import com.google.common.collect.Lists;

public class ParseDemoKit {
	public static List<Map<String, Object>> worksParse(List<Works> workss) {
		List<Map<String, Object>> resultMap = Lists.newArrayList();
		try {
			for (Works works : workss) {
				if (works == null)
					continue;
				Map<String, Object> map = new HashMap<String, Object>();
				Object worksid = works.get("worksid");
				map.put("worksid", worksid);
				String worksname = works.get("worksname");
				if (worksname.length() > 10) {
					worksname = worksname.substring(0, 10);
				}
				map.put("worksname", worksname);
				map.put("workstype", works.get("workstype"));
				map.put("pageviews", works.get("pageviews"));
				map.put("comment", works.get("comment"));
				map.put("pageviews", works.get("pageviews"));
				String cover = works.get("cover");
				map.put("cover", cover);
				String playUrl = "";
				String detailUrl = "";
				Integer type = works.getInt("type");
				if (type == 1) {
					// 0专辑
					playUrl = "cartoon/playVideo?id=" + worksid;
					detailUrl = "cartoon/showDetail?id=" + worksid;
				} else if (type == 2) {
					// 视频
					playUrl = "manhua/playVideo?id=" + worksid;
					detailUrl = "manhua/showDetail?id=" + worksid;
				}
				map.put("playUrl", playUrl);
				map.put("detailUrl", detailUrl);
				String typeUrl = "cartoon/showWorksList?workstype=" + works.get("workstype");
				map.put("typeUrl", typeUrl);
				String describle = works.get("describle");
				if (describle.length() > 10) {
					describle = describle.substring(0, 10) + "...";
				}
				if (StrKit.isBlank(describle)) {
					describle = "";
				}
				map.put("describle", describle);
				map.put("creater", works.get("createtime"));
				map.put("creatername", works.get("creatername"));
				map.put("createtime", works.get("createtime"));
				map.put("maxnum", works.get("maxnum"));
				map.put("modifier", works.get("modifier"));
				map.put("modifiername", works.get("modifiername"));
				map.put("updatetime", works.get("updatetime"));
				resultMap.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	public static List<Map<String, Object>> goodsParse(List<Goods> goodss) {
		List<Map<String, Object>> resultMap = Lists.newArrayList();
		try {
			for (Goods goods : goodss) {
				if (goods == null)
					continue;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("goodsid", goods.get("goodsid"));
				String goodsname = goods.get("goodsname");
				if (goodsname.length() >= 10) {
					goodsname = goodsname.substring(0, 10);
				}
				map.put("goodsname", goodsname);
				map.put("price", goods.get("price"));
				map.put("oldprice", goods.get("oldprice"));
				map.put("amount", goods.get("amount"));
				map.put("pic", goods.get("pic"));
				map.put("goodsattr1", goods.get("goodsattr1"));
				map.put("dougprice", goods.get("dougprice"));
				map.put("showflag", goods.get("showflag"));
				String message = goods.get("message");
				if (message.length() > 50) {
					message = message.substring(0, 50) + "...";
				}
				map.put("message", message);
				map.put("submitdate", goods.get("submitdate"));
				map.put("username", goods.get("username"));
				map.put("headimg", goods.get("headimg"));
				map.put("sumlove", goods.get("sumlove"));
				String detailUrl = "goods/getGoodsDetail?goodsid=" + goods.get("goodsid") + "&goodsattr="
						+ goods.get("goodsattr1") + "&dougprice=" + goods.get("dougprice") + "&headimg="
						+ goods.get("headimg");
				map.put("detailUrl", detailUrl);
				resultMap.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

}
