<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>作业列表</title>
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
            	$.messager.alert("消息提醒", "请选择作业进行批改!", "warning");
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
					url: "TeacherServlet?method=getSubmitList",
					data: data,
					dataType: "json",
					async: false,
					success: function(result){
						 //提交列表
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
				   	     	   {field:'chk',checkbox: true,width:50},
				   	     	   {field:'wid',title:'作业号',width:100, sortable: true},
				   	     	   {field:'sid',title:'学号',width:100, sortable: true},
				   	           {field:'qid',title:'题号',width:100, sortable: true},
				   	     	   {field:'answer',title:'学生答案',width:100, sortable: true},
							]],
				   	    }); 
					}
            	});
            	setTimeout(function(){
			    	$("#escoreList").datagrid("options").url = "TeacherServlet?method=getSubmitList&t="+new Date().getTime();
			    	$("#escoreList").datagrid("options").queryParams = data;
			    	$("#escoreList").datagrid("reload");
			    	$("#escoreListDialog").dialog("open");
            	}, 100)
		    	
	    	}
	    });
	  //批改作业
	    $("#pub").click(function(){
	    	var selectRows = $("#escoreList").datagrid("getSelections");
        	var selectLength = selectRows.length;
        	if(selectLength == 0){
            	$.messager.alert("消息提醒", "请选择题目进行批改!", "warning");
            } else{
	    	table = $("#pubTable");
	    	$("#submitWork").dialog("open");
            }
	    });
	  //作业批改窗口
	    $("#escoreListDialog").dialog({
	    	title: "作业批改",
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
	  //设置作业批改窗口
	    $("#submitWork").dialog({
	    	title: "作业批改",
	    	width: 450,
	    	height: 250,
	    	iconCls: "icon-pub",
	    	modal: true,
	    	collapsible: false,
	    	minimizable: false,
	    	maximizable: false,
	    	draggable: true,
	    	closed: true,
	    	buttons: [
	    		{
					text:'确定',
					plain: true,
					iconCls:'icon-work_add',
					handler:function(){
			            	var validate = $("#addWorkFrom").form("validate");
							if(!validate){
								$.messager.alert("消息提醒","请检查你输入的数据!","warning");
								return;
							}else{
								var selectRows = $("#escoreList").datagrid("getSelected");
								var wids = [];
				            	$(selectRows).each(function(i, row){
				            		wids[i] = row.wid;
				            	});
				            	var qids = [];
				            	$(selectRows).each(function(i, row){
				            		qids[i] = row.qid;
				            	});
				            	var sids = [];
				            	$(selectRows).each(function(i, row){
				            		sids[i] = row.sid;
				            	});
								var grade = $("#grade").textbox("getText");
								var data = {grade:grade,wids:wids,qids:qids,sids:sids};
								$.ajax({
									type: "post",
									url: "TeacherServlet?method=correctSub",
									data: data,
									success: function(msg){
										if(msg == "success"){
											$.messager.alert("消息提醒","录入成功!","info");
											//关闭窗口
											$("#addWork").dialog("close");
											$("#submitWork").dialog("close");
											//重新刷新页面数据
								  			$('#dataList').datagrid("reload");
										} else{
											$.messager.alert("消息提醒","添加失败!","warning");
											return;
										}
									}
								});
							}
						}
				},
				{
					text:'重置',
					plain: true,
					iconCls:'icon-reload',
					handler:function(){
						$("#grade").textbox('setValue', "");
					}
				},
			],
			onClose: function(){
				$("#grade").textbox('setValue', "");
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
	<div><a id="escore" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-chart_bar',plain:true">作业批改</a></div>
	</div>
	<!-- 考试成绩表 -->
	<div id="escoreListDialog">
	<div style="float: left;"><a id="pub" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-pub',plain:true">作业批改</a></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
		<table id="escoreList" cellspacing="0" cellpadding="0"> 
		</table> 
	</div>
	
	<!-- 作业批改 -->
	<div id="submitWork" style="padding: 10px">
	   	<form id="addWorkFrom" method="post">
	    	<table id="pubTable" border=0 style="width:200px; table-layout:fixed;" cellpadding="6" >
	    		<tr>
	    			<td style="width:40px">得分:</td>
	    			<td colspan="3"><input id="grade"  class="easyui-textbox" style="width: 160px; height: 30px;" type="text" name="wname" data-options="required:true, validType:'repeat', missingMessage:'请打分'" /></td>
	    		</tr>
	    	</table>
	    </form>
	</div>
</body>
</html>