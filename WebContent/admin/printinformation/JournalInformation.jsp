<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.uniwin.basehs.service.BaseService"%>
<%@ page import="org.iti.jxkh.service.JxkhAwardService"%>
<%@ page import="com.iti.common.util.BeanFactory"%>
<%@ page import="org.iti.jxkh.entity.*"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>打印期刊论文信息</title>
<style media="print">
.Noprint {
	display: none;
}

.PageNext {
	page-break-after: always;
}
</style>
<style>
.tdp {
	border-bottom: 1 solid #000000;
	border-left: 1 solid #000000;
	border-right: 0 solid #ffffff;
	border-top: 0 solid #ffffff;
}

.tabp {
	border-color: #000000 #000000 #000000 #000000;
	border-style: solid;
	border-top-width: 2px;
	border-right-width: 2px;
	border-bottom-width: 1px;
	border-left-width: 1px;
}

.NOPRINT {
	font-family: "宋体";
	font-size: 9pt;
}

.out {
	border-top: 40px #D6D3D6 solid;
	width: 0px;
	height: 0px;
	border-left: 80px #BDBABD solid;
	position: relative;
}
</style>
<script type="text/javascript">
	function pconfirm() {
		alert("为了确保您的打印效果，打印时，在菜单栏 文件 按钮中，页面设置 方向请选择纵打印；页眉页脚选项清空，页边距（毫米）设置：左右均为9.91，上为20，下为5.08。（注意：打印效果与您的打印机设置有关，请根据需要调整。）")
	}
</script>
</head>
<body>
	<center class="Noprint">
		<table width="700">
			<tr align="right">
				<td><input type="button" value="打印说明"
					onclick="return pconfirm();" />&nbsp;&nbsp; <input type="button"
					value="打印 " onclick="window.print();" /></td>
			</tr>
		</table>
	</center>
	<p align="center">
		<strong style="font-size: 30px">河北省科学技术情报院</strong>
		<br/>
		<strong style="font-size: 25px">学术会议详细信息</strong>
	</p>
	<div align="center">
		<table width="700" border="1" align="center" cellspacing="0">
			<tr height="40">
				<td width="150"><strong>期刊论文名称</strong></td>
				<td width="550" colspan="3"><s:property
						value="#request.journal.lwName" /></td>
			</tr>
			<tr height="40">
				<td width="150"><strong>全部作者及排序</strong></td>
				<td width="550" colspan="3" align="left"><s:property
						value="#request.writingmember" /></td>
			</tr>
			<tr height="40">
				<td width="150"><strong>完成部门及排序</strong></td>
				<td width="550" colspan="3" align="left"><s:property
						value="#request.writingdept" /></td>
			</tr>
			<tr height="40">
				<td width="150"><strong>合作单位</strong>
				<td width="200"><s:property value="#request.writingcoo" /></td>
				<td width="150"><strong>论文级别</strong></td>
				<td width="200"><s:property value="#request.journal.lwRank" /></td>
			</tr>

			<tr height="40">
				<td width="150"><strong>期刊类别</strong></td>
				<td width="200"><s:property
						value="#request.journal.qkGrade.kbName" /></td>
				<td width="150"><strong>收录类别</strong></td>
				<td width="200"><s:property
						value="#request.journal.lwType.kbName" /></td>
			</tr>

			<tr height="40">
				<td width="150"><strong>发表刊物名称</strong></td>
				<td width="200"><s:property value="#request.journal.kwName" /></td>
				<td width="150"><strong>发表刊物期次</strong></td>
				<td width="200"><s:property value="#request.journal.lwQC" /></td>
			</tr>
			<tr height="40">
				<td width="150"><strong>收录时间</strong></td>
				<td width="200"><s:property value="#request.journal.lwTime" /></td>
				<td width="150"><strong>信息填写人</strong></td>
				<td width="200"><s:property value="#request.journal.lwWriter" /></td>
			</tr>
			<tr height="120">
				<td width="150"><strong>项目组成员签字</strong></td>
				<td colspan="3" align="left" valign="top"><a>请核对信息，确认无误后请签字：</a>
				</td>
			</tr>
			<tr height="120">
				<td width="150"><strong>承担部门签字</strong></td>
				<td colspan="3" align="left" valign="top"><a>请核对信息，确认无误后请签字：</a>
				</td>
			</tr>
			<tr height="120">
				<td width="150"><strong>部门审核意见</strong></td>
				<td colspan="3" align="left"><s:property
						value="#request.journal.depReason" /></td>
			</tr>
			<tr height="120">
				<td width="150"><strong>业务办审核意见</strong></td>
				<td colspan="3" align="left"><s:property
						value="#request.journal.ywReason" /></td>
			</tr>
		</table>
	</div>
</body>
</html>