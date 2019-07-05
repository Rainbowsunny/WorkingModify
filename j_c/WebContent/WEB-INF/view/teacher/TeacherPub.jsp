<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="ie=edge">
	<title>作业发布</title>
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
	        title:'题目列表', 
	        iconCls:'icon-more',//图标 
	        border: true, 
	        collapsible:false,//是否可折叠的 
	        fit: true,//自动大小 
	        method: "post",
	        url:"TeacherServlet?method=getQuestionList&t="+new Date().getTime(),
	        idField:'qid', 
	        singleSelect:false,//是否单选 
	        pagination:true,//分页控件 
	        rownumbers:true,//行号 
	        sortName:'qid',
	        sortOrder:'DESC', 
	        remoteSort: false,
	        columns: [[  
				{field:'chk',checkbox: true,width:50},
 		        {field:'qid',title:'题号',width:50, sortable: true},    
 		        {field:'qcontent',title:'题目内容',width:150, sortable: true},    
 		        {field:'snum',title:'分值',width:100},
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
	    //发布作业
	    $("#pub").click(function(){
	    	var selectRows = $("#dataList").datagrid("getSelections");
        	var selectLength = selectRows.length;
        	if(selectLength == 0){
            	$.messager.alert("消息提醒", "请选择题目进行发布!", "warning");
            } else{
	    	table = $("#pubTable");
	    	$("#addWork").dialog("open");
            }
	    });
	    //添加题目
	    $("#add").click(function(){
	    	table = $("#addTable");
	    	$("#addDialog").dialog("open");
	    });
	  //设置题目删除按钮
	    $("#delete").click(function(){
	    	var selectRows = $("#dataList").datagrid("getSelections");
        	var selectLength = selectRows.length;
        	if(selectLength == 0){
            	$.messager.alert("消息提醒", "请选择数据进行删除!", "warning");
            } else{
            	var qids = [];
            	$(selectRows).each(function(i, row){
            		qids[i] = row.qid;
            	});
            	$.messager.confirm("消息提醒", "将删除选中题目，确认继续？", function(r){
            		if(r){
            			$.ajax({
							type: "post",
							url: "TeacherServlet?method=deleteQuestion",
							data: {qids:qids},
							success: function(msg){
								if(msg == "success"){
									$.messager.alert("消息提醒","删除成功!","info");
									//刷新表格
									$("#dataList").datagrid("reload");
									$("#dataList").datagrid("uncheckAll");
								} else{
									$.messager.alert("消息提醒","删除失败!","warning");
									return;
								}
							}
						});
            		}
            	});
            }
	    });
	  //设置作业发布窗口
	    $("#addWork").dialog({
	    	title: "作业发布",
	    	width: 850,
	    	height: 550,
	    	iconCls: "icon-pub",
	    	modal: true,
	    	collapsible: false,
	    	minimizable: false,
	    	maximizable: false,
	    	draggable: true,
	    	closed: true,
	    	buttons: [
	    		{
					text:'作业发布',
					plain: true,
					iconCls:'icon-work_add',
					handler:function(){
			            	var validate = $("#addWorkFrom").form("validate");
							if(!validate){
								$.messager.alert("消息提醒","请检查你输入的数据!","warning");
								return;
							}if($("#work_start").textbox("getText")>$("#work_end").textbox("getText")){
								$.messager.alert("消息提醒","截止时间不能小于开始时间!","warning");
								$("#work_end").textbox('setValue', "");
								return;
							} else{
								var selectRows = $("#dataList").datagrid("getSelections");
								var ids = [];
				            	$(selectRows).each(function(i, row){
				            		ids[i] = row.qid;
				            	});
								var wname = $("#add_work_name").textbox("getText");
								var starttime = $("#work_start").textbox("getText");
								var endtime = $("#work_end").textbox("getText");
								var data = {wname:wname,starttime:starttime,endtime:endtime,ids:ids};
								$.ajax({
									type: "post",
									url: "TeacherServlet?method=addWork",
									data: data,
									success: function(msg){
										if(msg == "success"){
											$.messager.alert("消息提醒","发布成功!","info");
											//关闭窗口
											$("#addWork").dialog("close");
											//清空原表格数据
											$("#add_work_name").textbox('setValue', "");
											$("#work_start").textbox('setValue', "");
											$("#work_end").textbox('setValue', "");
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
						$("#add_work_name").textbox('setValue', "");	
						$("#work_start").textbox('setValue', "");
						$("#work_end").textbox('setValue', "");
					}
				},
			],
			onClose: function(){
				$("#add_work_name").textbox('setValue', "");
				$("#work_start").textbox('setValue', "");
				$("#work_end").textbox('setValue', "");
			}
	    });
	  	//设置添加题目窗口
	    $("#addDialog").dialog({
	    	title: "添加题目",
	    	width: 850,
	    	height: 550,
	    	iconCls: "icon-add",
	    	modal: true,
	    	collapsible: false,
	    	minimizable: false,
	    	maximizable: false,
	    	draggable: true,
	    	closed: true,
	    	buttons: [
	    		{
					text:'添加题目',
					plain: true,
					iconCls:'icon-question_add',
					handler:function(){
						var validate = $("#addForm").form("validate");
						if(!validate){
							$.messager.alert("消息提醒","请检查你输入的数据!","warning");
							return;
						} else{
							var qcontent = $("#qcontent").textbox("getText");
							var snum = $("#snum").textbox("getText");
							var data = {qcontent:qcontent,snum:snum};
							$.ajax({
								type: "post",
								url: "TeacherServlet?method=addQuestion",
								data: data,
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","添加成功!","info");
										//关闭窗口
										$("#addDialog").dialog("close");
										//清空原表格数据
										$("#qcontent").textbox('setValue', "");
										$("#snum").textbox('setValue', "");
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
						$("#qcontent").textbox('setValue', "");
						$("#snum").textbox('setValue', "");
					}
				},
			],
			onClose: function(){
				$("#qcontent").textbox('setValue', "");
				$("#snum").textbox('setValue', "");
			}
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
		<div style="float: left;"><a id="pub" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-pub',plain:true">作业发布</a></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
		<div style="float: left;"><a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
		<div><a id="delete" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-some-delete',plain:true">删除</a></div>
	</div>
	
	<!-- 添加题目窗口 -->
	<div id="addDialog" style="padding: 10px;">  
   		<form id="addForm" method="post">
	    	<table id="addTable" border=0 style="width:800px; table-layout:fixed;" cellpadding="6" >
	    		<tr>
	    			<td style="width:80px">题目内容:</td>
	    			<td colspan="4">
	    				<input id="qcontent"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="number" data-options="required:true, validType:'repeat', missingMessage:'请输入题目内容'" />
	    			</td>
	    		</tr>
	    		<tr>
	    			<td style="width:80px">分值:</td>
	    			<td colspan="4"><input id="snum" style="width: 200px; height: 30px;" class="easyui-textbox" type="text" name="qq" validType="number" /></td>
	    		</tr>
	    	</table>
	    </form>
	</div>
	
	<!-- 作业发布 -->
	<div id="addWork" style="padding: 10px">
	   	<form id="addWorkFrom" method="post">
	    	<table id="pubTable" border=0 style="width:800px; table-layout:fixed;" cellpadding="6" >
	    		<tr>
	    			<td style="width:40px">作业名称:</td>
	    			<td colspan="3"><input id="add_work_name"  class="easyui-textbox" style="width: 200px; height: 30px;" type="text" name="wname" data-options="required:true, validType:'repeat', missingMessage:'请输入作业名称'" /></td>
	    		</tr>
	    		<tr>
	    			<td>开始时间:</td>
	    			<td colspan="3" ><input id="work_start" class="easyui-textbox" style="width: 200px; height: 30px;" type="date" name="start" data-options="required:true, validType:'repeat', missingMessage:'请输入开始时间'" ></td>
	    		<tr>
	    			<td>截止时间:</td>
	    			<td colspan="3" ><input  id="work_end" class="easyui-textbox" style="width: 200px; height: 30px;" type="date" name="end" data-options="required:true, validType:'repeat', missingMessage:'请输入截止时间'" ></td>
	    	</table>
	    </form>
	</div>
</body>
</html>