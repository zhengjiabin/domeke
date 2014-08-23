package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.CodeType;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;

/**
 * @author 陈智聪
 *
 */
@ControllerBind(controllerKey="codetype")
public class CodeTypeController extends Controller {
	
	/**
	 * 新增
	 */
	public void goCodeType(){
		render("/demo/addCodeType.html");
	}
	
	/**
	 * 新增
	 */
	public void saveCodeType(){
		CodeType codeType = getModel(CodeType.class);
		codeType.saveCodeType();
		selectCodeType();
	}
	
	/**
	 * 查询
	 */
	public void selectCodeType(){
		CodeType.codeTypeDao.removeCache();
		CodeType codeType = getModel(CodeType.class);
		List<CodeType> codeTypeList = codeType.selectCodeType();
		setAttr("codeTypeList", codeTypeList);
		render("/demo/selectCodeType.html");
	}
	
	/**
	 * 
	 */
	public void selectCodeTypeById(){
		int codeTypeId = getParaToInt();
		CodeType codeType = CodeType.codeTypeDao.selectCodeTypeById(codeTypeId);
		setAttr("codeType",codeType);
		render("/demo/updateCodeType.html");
	}
	
	/**
	 * 更新
	 */
	public void updateCodeType(){
		CodeType codeType = getModel(CodeType.class);
		codeType.updateCodeType();
		selectCodeType();
	}
	
	/**
	 * 删除
	 */
	public void deleteCodeType(){
		CodeType codeType = getModel(CodeType.class);
		int codeTypeId = getParaToInt();
		codeType.deleteCodeType(codeTypeId);
		selectCodeType();
	}
}
