<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>作业列表</title>
<link rel="stylesheet" type="text/css"
	href="easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="easyui/css/demo.css">
<script type="text/javascript" src="easyui/jquery.min.js"></script>
<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="easyui/js/validateExtends.js"></script>
<script type="text/javascript">
	$(function() {//JavaScript函数	
		//datagrid初始化 
	    $('#questionList').datagrid({ 
	        title:'作业题目列表', 
	        iconCls:'icon-more',//图标 
	        border: true, 
	        collapsible:false,//是否可折叠的 
	        fit: true,//自动大小 
	        method: "post",
	        url:"StudentServlet?method=getQuestionList&t="+new Date().getTime(),
	        idField:'qid', 
	        singleSelect:true,//是否单选 
	        pagination:true,//分页控件 
	        rownumbers:true,//行号 
	        sortName:'wid',
	        sortOrder:'DESC', 
	        remoteSort: false,
	        columns: [[  
				{field:'chk',checkbox: true,width:50},
 		        {field:'qid',title:'题目号',width:50, sortable: true},    
 		        {field:'qcontent',title:'题目内容',width:200, sortable: true},    
 		        {field:'qtype',title:'题目类型',width:200},
 		        {field:'snum',title:'题目分值',width:100},
 		        {field:'snum',title:'填写答案',width:100,}
	 		]], 
	        toolbar: "#toolbar"
	    }); 
	    //设置分页控件 
	    var p = $('#questionList').datagrid('getPager'); 
	    $(p).pagination({ 
	        pageSize: 10,//每页显示的记录条数，默认为10 
	        pageList: [10,20,30,50,100],//可以设置每页记录条数的列表 
	        beforePageText: '第',//页数文本框前显示的汉字 
	        afterPageText: '页    共 {pages} 页', 
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录', 
	    }); 
	    //设置工具类按钮
	  

	  	
	   
	});
	</script>
</head>
<body>
	<!-- 作业列表 -->
	<table id="questionList" cellspacing="0" cellpadding="0">

	</table>
	<!-- 工具栏 -->
	<div id="toolbar">
		<div style="float: left;">
			<a id="edit" href="StudentServlet?&action=editwork" >作业编辑</a>
		</div>
		<div style="float: left;" class="datagrid-btn-separator"></div>
		<div style="float: left;" class="datagrid-btn-separator"></div>

	</div>

</body>
</html>
