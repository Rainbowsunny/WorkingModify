<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>教师列表</title>
	<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="easyui/css/demo.css">
	<script type="text/javascript" src="easyui/jquery.min.js"></script>
	<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="easyui/js/validateExtends.js"></script>
	<script type="text/javascript">
	$(function() {	
		var table;
		//datagrid初始化 
	    $('#dataList').datagrid({ 
	        title:'作业列表', 
	        iconCls:'icon-more',//图标 
	        border: true,
	        collapsible:false,//是否可折叠的 
	        fit: true,//自动大小 
	        method: "post",
	        url:"TeacherServlet?method=getWorkList&t="+new Date().getTime(),
	        idField:'wid', 
	        singleSelect:true,//是否单选 
	        pagination:true,//分页控件 
	        rownumbers:true,//行号 
	        sortName:'wid',
	        sortOrder:'DESC', 
	        remoteSort: false,
	        columns: [[
                {field:'chk',checkbox: true,width:50}, 
                {field:'wid',title:'作业号',width:100, sortable: true},
 		        {field:'wname',title:'作业',width:100, sortable: true},
 		        {field:'starttime',title:'开始时间',width:100, sortable: true},
 		        {field:'endtime',title:'结束时间',width:100, sortable: true}
	 		]], 
	        toolbar: "#toolbar"
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
	   //设置工具类按钮
	  //成绩查询
	    $("#escore").click(function(){
	    	var work = $("#dataList").datagrid("getSelected");
        	if(work == null){
            	$.messager.alert("消息提醒", "请选择作业进行查询!", "warning");
            } else{
            	var selectRows = $("#dataList").datagrid("getSelections");
				var wid;
            	$(selectRows).each(function(i, row){
            		wid = row.wid;
            	});
            	var data = {wid: wid};
            	//动态显示该次考试的科目
            	$.ajax({
            		type: "post",
					url: "TeacherServlet?method=getGradeList",
					data: data,
					dataType: "json",
					async: false,
					success: function(result){
						 //成绩列表
					    $('#escoreList').datagrid({ 
				   	        border: true, 
				   	        collapsible: false,//是否可折叠的 
				   	        fit: true,//自动大小 
				   	        method: "post",
				   	        noheader: true,
				   	        singleSelect: true,//是否单选 
				   	        rownumbers: true,//行号 
				   	     	sortName:'sid',
				   	        sortOrder:'DESC', 
				   	        remoteSort: false,
				   	        toolbar: "#escoreToolbar",
				   	     	columns:  [[  
				   	 		   {field:'sid',title:'学号',width:100, sortable: true},
				   	 	       {field:'grade',title:'成绩',width:100, sortable: true},        
							]],
				   	    }); 
					}
            	});
            	setTimeout(function(){
			    	$("#escoreList").datagrid("options").url = "TeacherServlet?method=getGradeList&t="+new Date().getTime();
			    	$("#escoreList").datagrid("options").queryParams = data;
			    	$("#escoreList").datagrid("reload");
			    	$("#escoreListDialog").dialog("open");
            	}, 100)
		    	
	    	}
	    });
	  //考试成绩窗口
	    $("#escoreListDialog").dialog({
	    	title: "成绩查询",
	    	width: 850,
	    	height: 550,
	    	iconCls: "icon-chart_bar",
	    	modal: true,
	    	collapsible: false,
	    	minimizable: false,
	    	maximizable: false,
	    	draggable: true,
	    	closed: true,
	    	onClose: function(){
   	        	$("#escoreMajorList").combobox("clear");
   	        }
	    });
	  	
	    $("#escoreList").datagrid({ 
   	        toolbar: "#escoreToolbar",
   	    });
	    
	    $("#regEscoreList").datagrid({ 
   	        toolbar: "#regEscoreToolbar",
   	    });
	});
</script>
</head>	   
<body>
	<!-- 数据列表 -->
	<table id="dataList" cellspacing="0" cellpadding="0"> 
	</table> 
	<!-- 工具栏 -->
	<div id="toolbar">
	<div><a id="escore" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-chart_bar',plain:true">成绩查询</a></div>
	</div>
	<!-- 考试成绩表 -->
	<div id="escoreListDialog">
		<table id="escoreList" cellspacing="0" cellpadding="0"> 
		</table> 
	</div>
</body>
</html>