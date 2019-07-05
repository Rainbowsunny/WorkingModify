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
	        url:"StudentServlet?method=getWorkList&t="+new Date().getTime(),
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
 		        {field:'tid',title:'教师',width:100, sortable: true},
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
	  //点击题列表
	    $("#question").click(function(){
	    	var work = $("#dataList").datagrid("getSelected");
        	if(work == null){
            	$.messager.alert("消息提醒", "请选择作业进行答题!", "warning");
            } else{
            	var selectRows = $("#dataList").datagrid("getSelections");
				var wid;
            	$(selectRows).each(function(i, row){
            		wid = row.wid;
            	});
            	var data = {wid: wid};
            	//动态显示该次作业的题目
            	$.ajax({
            		type: "post",
					url: "StudentServlet?method=getQuestionList",
					data: data,
					dataType: "json",
					async: false,
					success: function(result){
						 //题目列表
					    $('#questionList').datagrid({ 
				   	        border: true, 
				   	        collapsible: false,//是否可折叠的 
				   	        fit: true,//自动大小 
				   	        method: "post",
				   	        noheader: true,
				   	        singleSelect: true,//是否单选 
				   	        rownumbers: true,//行号 
				   	     	sortName:'qid',
				   	        sortOrder:'DESC', 
				   	        remoteSort: false,
				   	        toolbar: "#questiontoolbar",
				   	     	columns:  [[  
				   	     	   {field:'chk',checkbox: true,width:50}, 
				   	 		   {field:'qid',title:'题目编号',width:100, sortable: true},
				   	 	       {field:'qcontent',title:'题目内容',width:100, sortable: true},
				   	 	   	   {field:'snum',title:'分值',width:100, sortable: true},
							]],
				   	    }); 
					}
            	});
            	setTimeout(function(){
			    	$("#questionList").datagrid("options").url = "StudentServlet?method=getQuestionList&t="+new Date().getTime();
			    	$("#questionList").datagrid("options").queryParams = data;
			    	$("#questionList").datagrid("reload");
			    	$("#questiontListDialog").dialog("open");
            	}, 100)
		    	
	    	}
	    });
	  //批改作业
	    $("#answer").click(function(){
	    	var selectRows = $("#questionList").datagrid("getSelections");
        	var selectLength = selectRows.length;
        	if(selectLength == 0){
            	$.messager.alert("消息提醒", "请选择题目进行答题!", "warning");
            } else{
	    	table = $("#answerTable");
	    	$("#answerListDialog").dialog("open");
           }
	    });
	  //题目列表窗口
	    $("#questiontListDialog").dialog({
	    	title: "题目列表",
	    	width: 850,
	    	height: 550,
	    	iconCls: "icon-chart_bar",
	    	modal: true,
	    	collapsible: false,
	    	minimizable: false,
	    	maximizable: false,
	    	draggable: true,
	    	closed: true,
	    });
	  //设置作业批改窗口
	    $("#answerListDialog").dialog({
	    	title: "答题界面",
	    	width: 350,
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
					text:'提交',
					plain: true,
					iconCls:'icon-work_add',
					handler:function(){
			            	var validate = $("#answerFrom").form("validate");
							if(!validate){
								$.messager.alert("消息提醒","请检查你输入的数据!","warning");
								return;
							}else{
								var selectRows = $("#questionList").datagrid("getSelected");
								var qids = [];
				            	$(selectRows).each(function(i, row){
				            		qids[i] = row.qid;
				            	});
				            	var selectRows = $("#dataList").datagrid("getSelected");
								var tids = [];
								var wids = [];
				            	$(selectRows).each(function(i, row){
				            		tids[i] = row.tid;
				            		wids[i] = row.wid;
				            	});
								var myanswer = $("#myanswer").textbox("getText");
								var data = {myanswer:myanswer,qids:qids,wids:wids,tids:tids};
								$.ajax({
									type: "post",
									url: "StudentServlet?method=addSubmit",
									data: data,
									success: function(msg){
										if(msg == "success"){
											$.messager.alert("消息提醒","添加成功!","info");
											//清空原表格数据
											$("#myanswer").textbox('setValue', "");
											//关闭窗口
											$("#answerListDialog").dialog("close");
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
						$("#myanswer").textbox('setValue', "");
					}
				},
			],
			onClose: function(){
				$("#myanswer").textbox('setValue', "");
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
	<div><a id="question" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-chart_bar',plain:true">题目列表</a></div>
	</div>
	<!-- 题目列表 -->
	<div id="questiontListDialog">
	<div id="questiontoolbar" style="float: left;"><a id="answer" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-pub',plain:true">答题</a></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
		<table id="questionList" cellspacing="0" cellpadding="0"> 
		</table> 
	</div>
	
	<!-- 答题界面 -->
	<div id="answerListDialog" style="padding: 10px">
	   	<form id="answerFrom" method="post">
	    	<table id="answerTable" border=0 style="width:200px; table-layout:fixed;" cellpadding="6" >
	    		<tr>
	    			<td style="width:120px">我的答案:</td>
	    		</tr>
	    		<tr>
	    			<td colspan="3"><input id="myanswer"  class="easyui-textbox" style="width: 300px; height: 200px;" type="text" name="wname" data-options="required:true, validType:'repeat', missingMessage:'请填写答案'" /></td>
	    		</tr>
	    	</table>
	    </form>
	</div>
</body>
</html>