/**
 * @license Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here.
	// For complete reference see:
	// http://docs.ckeditor.com/#!/api/CKEDITOR.config

	// The toolbar groups arrangement, optimized for two toolbar rows.
	config.toolbarGroups = [
		{ name: 'clipboard',   groups: [ 'clipboard', 'undo' ] },
		{ name: 'editing',     groups: [ 'find', 'selection', 'spellchecker' ] },
		{ name: 'links' },
		{ name: 'insert' },
		{ name: 'forms' },
		{ name: 'tools' },
		{ name: 'document',	   groups: [ 'mode', 'document', 'doctools' ] },
		{ name: 'others' },
		'/',
		{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
		{ name: 'paragraph',   groups: [ 'list', 'indent', 'blocks', 'align', 'bidi' ] },
		{ name: 'styles' },
		{ name: 'colors' },
		{ name: 'about' }
	];

	// Remove some buttons provided by the standard plugins, which are
	// not needed in the Standard(s) toolbar.
	config.removeButtons = 'Underline,Subscript,Superscript';

	// Set the most common block elements.
	config.format_tags = 'p;h1;h2;h3;pre';

	// Simplify the dialog windows.
	config.removeDialogTabs = 'image:advanced;link:advanced';
	
	config.enterMode = CKEDITOR.ENTER_BR;
    config.shiftEnterMode = CKEDITOR.ENTER_BR;
	
	// set upload
	var curPath=window.document.location.href;
	var pathName=window.document.location.pathname;
	var pos=curPath.indexOf(pathName);
    //获取主机地址如： http://localhost:8083
    var hostPath=curPath.substring(0,pos);
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    //如： http://localhost:8083/uimcardprj
    var rootPath = hostPath+projectName
	config.filebrowserImageBrowseUrl = rootPath + '/editor/uploadImage?Type=Images';
	config.filebrowserFlashBrowseUrl = rootPath + '/editor/uploadImage?Type=Flash';
	config.filebrowserUploadUrl = rootPath + '/editor/uploadImage?type=Files';
	config.filebrowserImageUploadUrl = rootPath + '/editor/uploadImage?type=Images';
	config.filebrowserFlashUploadUrl = rootPath + '/editor/uploadImage?type=Flash';
};
