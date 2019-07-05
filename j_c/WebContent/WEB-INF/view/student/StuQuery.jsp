
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>考试列表</title>
	<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="easyui/css/demo.css">
	<script type="text/javascript" src="easyui/jquery.min.js"></script>
	<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="easyui/themes/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript">
		//验证只能为数字
		function scoreBlur(score){
			if(!/^[1-9]\d*$/.test($(score).val())){
				$(score).val("");
			}
		}
	$(function() {	
		//datagrid初始化 
	    $('#dataList').datagrid({ 
	        title:'成绩列表', 
	        iconCls:'icon-more',//图标 
	        border: true, 
	        collapsible: false,//是否可折叠的 
	        fit: true,//自动大小 
	        method: "post",
	        url:"StudentServlet?method=getGradeList&t="+new Date().getTime(),
	        idField:'wid', 
	        singleSelect: true,//是否单选 
	        pagination: false,//分页控件 
	        rownumbers: true,//行号 
	        sortName:'wid',
	        sortOrder:'ASC', 
	        remoteSort: false,
	        columns: [[  
 		        {field:'wid',title:'作业编号',width:200, sortable: true},  
 		        {field:'grade',title:'成绩',width:100, sortable: true},
	 		]], 
	 		toolbar: [   ]
	    });
	    //设置分页控件 
	    var p = $('#dataList').datagrid('getPager'); 
	    $(p).pagination({ 
	        pageSize: 10,//每页显示的记录条数，默认为10 
	        pageList: [10,20,30,50,100],//可以设置每页记录条数的列表 
	        beforePageText: '第',//页数文本框前显示的汉字 
	        afterPageText: '页    共 {pages} 页', 
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录', 
	    }); 
	    });
	
	</script>
</head>
<body>
	<!-- 数据列表 -->
	<table id="dataList" cellspacing="0" cellpadding="0"> 
	    
	</table> 
	
</body>
</html>