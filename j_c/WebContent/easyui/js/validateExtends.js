/**
 * 扩展easyui表单的验证
 */

$.extend($.fn.validatebox.defaults.rules, {
    //验证汉字
    CHS: {
        validator: function (value) {
            return /^[\u0391-\uFFE5]+$/.test(value);//校验函数经常通过正则表达式来实现
                                                    //test方法检索字符串中指定的值。返回 true 或 false。
        },
        message: '只能输入汉字'//所写校验函数返回false时的提示信息
    },
    //移动手机号码验证
    mobile: {//value值为文本框中的值
        validator: function (value) {
            var reg = /^1[3|4|5|8|9]\d{9}$/;
            return reg.test(value);
        },
        message: '13/14/15/18/19开头,且11位的手机号'
    },
  	//只能为数字
    number: {//value值为文本框中的值
        validator: function (value) {
            var reg = /^[0-9]*$/;
            return reg.test(value);
        },
        message: '只能为数字格式'
    },
	//验证账号不能重复
	repeat: {
		validator: function (value) {
			var flag = true;
			$.ajax({
				type: "post",//规定请求的类型
				async: false, //布尔值，表示请求是否异步处理。默认是 true。
				url: "SystemServlet?method=AllAccount&t="+new Date().getTime(),//规定发送请求的URL
				success: function(data){//在验证函数里加载数据，success加载过来后判断输入的值,当请求成功时运行的函数。
					var account = $.parseJSON(data);
		            for(var i=0;i < account.length;i++){
		            	if(value == account[i]){
		            		flag = false;
		            		break;
		            	}
		            }
				}
			});
			return flag;
	    },
	    message: '用户已存在'
	},
	
	//验证课程不能重复
	repeat_course: {
		validator: function (value) {
			var flag = true;
			$.ajax({
				type: "post",
				async: false,
				url: "CourseServlet?method=CourseList&t="+new Date().getTime(),
				success: function(data){//在验证函数里加载数据，加载过来后判断输入的值
					var course = $.parseJSON(data);
		            for(var i=0;i < course.length;i++){
		            	if(value == course[i].name){
		            		flag = false;
		            		break;
		            	}
		            }
				}
			});
			return flag;
	    },
	    message: '课程名称已存在'
	},
	
	//验证学院不能重复
	repeat_grade: {
		validator: function (value) {
			var flag = true;
			$.ajax({
				type: "post",
				async: false,
				url: "CollegeServlet?method=CollegeList&t="+new Date().getTime(),
				success: function(data){//在验证函数里加载数据，加载过来后判断输入的值
					var college = $.parseJSON(data);
		            for(var i=0;i < college.length;i++){
		            	if(value == college[i].name){
		            		flag = false;
		            		break;
		            	}
		            }
				}
			});
			return flag;
	    },
	    message: '学院名称已存在'
	},
	
	//验证专业不能重复
	repeat_major: {
		validator: function (value, param) {
			var gradeid = $(param[0]).combobox("getValue");
			var flag = true;
			$.ajax({
				type: "post",
				async: false,
				data: {majorid: majorid},
				url: "MajorServlet?method=MajorList&t="+new Date().getTime(),
				success: function(data){//在验证函数里加载数据，加载过来后判断输入的值
					var major = $.parseJSON(data);
		            for(var i=0;i < major.length;i++){
		            	if(value == major[i].name){
		            		flag = false;
		            		break;
		            	}
		            }
				}
			});
			return flag;
	    },
	    message: '该年级下已存在同名专业'
	},
	
	//验证两个值是否相同
	equals: {//param的值为[]中值
        validator: function (value, param) {
        	if($(param[0]).val() != value){ //val()返回被选元素的当前值。
        		return false;
        	} else{
        		return true;
        	}
            
        }, message: '两次密码不同.'
    },
    
    //密码规则
    password: {
        validator: function (value) {
        	var reg = /^[a-zA-Z0-9]{6,16}$/;
        	return reg.test(value);
            
        }, message: '密码6-16位，且只能为英文、数字'
    },
    
    //验证输入密码是否正确
    oldPassword: {
        validator: function (value, param) {
        	if(param != value){
        		return false;
        	} else{
        		return true;
        	}
            
        }, message: '密码不正确'
    },
    
    //国内邮编验证
    zipcode: {
        validator: function (value) {
            var reg = /^[1-9]\d{5}$/;
            return reg.test(value);
        },
        message: '邮编必须是非0开始的6位数字.'
    },
    //用户账号验证(只能包括 _ 数字 字母) 
    account: {//param的值为[]中值
        validator: function (value, param) {
            if (value.length < param[0] || value.length > param[1]) {
                $.fn.validatebox.defaults.rules.account.message = '用户名长度必须在' + param[0] + '至' + param[1] + '范围';
                return false;
            } else {
                if (!/^[\w]+$/.test(value)) {
                    $.fn.validatebox.defaults.rules.account.message = '用户名只能数字、字母、下划线组成.';
                    return false;
                } else {
                    return true;
                }
            }
        }, message: ''
    }
}) 
