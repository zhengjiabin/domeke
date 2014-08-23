package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.CodeTable;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
/**
 * @author 陈智聪
 *
 */
@ControllerBind(controllerKey="codetable")
public class CodeTableController extends Controller {
	
	public void goCodeTable(){
		render("/demo/addCodeTable.html");
	}
	
	public void saveCodeTable(){
		CodeTable codeTable = getModel(CodeTable.class);
		codeTable.saveCodeTable();
		selectCodeTable();
	}
	
	public void selectCodeTable(){
		CodeTable.codeTableDao.removeCache();
		CodeTable codeTable = getModel(CodeTable.class);
		List<CodeTable> codeTableList = codeTable.selectCodeTable();
		setAttr("codeTableList", codeTableList);
		render("/demo/selectCodeTable.html");
	}
	
	public void selectCodeTableById(){
		int codeTableId = getParaToInt();
		CodeTable codeTable = CodeTable.codeTableDao.selectCodeTableById(codeTableId);
		setAttr("codeTable", codeTable);
		render("/demo/updateCodeTable.html");
	}
	
	public void updateCodeTable(){
		CodeTable codeTable = getModel(CodeTable.class);
		codeTable.updateCodeTable();
		selectCodeTable();
	}
	
	public void deleteCodeTable(){
		CodeTable codeTable = getModel(CodeTable.class);
		int codeTableId = getParaToInt();
		codeTable.deleteCodeTable(codeTableId);
		selectCodeTable();
	}
}
