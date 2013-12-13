package com.chinaknown.nuoensys.utils;

public class URLAddress {

	/**
	 * 用户登陆地址
	 *http://mmqforever.eicp.net:6060/NuoEnSys/control/center/employee_login?username=admin&password=admin&type=2
	 */
	public static final String LOGIN_URL = "http://mmqforever.eicp.net:6060/NuoEnSys/control/center/employee_login";
	
	public static final String BASIC_URL = "http://mmqforever.eicp.net:6060";
	/**
	 * 公司新闻列表地址
	 * page 页数
	 */
	public static final String COMPANYNEWS_URL = "http://mmqforever.eicp.net:6060/NuoEnSys/control/center/companyNew_androidNewlist";
	
	/**
	 * 超找已经发出的周报列表地址
	 * page 页数
	 */
	public static final String SENDEDREPORT_URL = "http://mmqforever.eicp.net:6060/NuoEnSys/control/center/report_androidSendedReportlist";
	
	/**
	 * 超找当前登陆人接收到的周报列表地址
	 * page 页数
	 */
	public static final String READREPORT_URL = "http://mmqforever.eicp.net:6060/NuoEnSys/control/center/report_androidReadReportList";
	
	/**
	 * 发送请假条
	 * 			maps.put("creatorid", String.valueOf(employee.getUserid()));
				maps.put("creator", employee.getRealName());
				maps.put("department", employee.getDepartment());
				maps.put("duty", employee.getDuty());
				maps.put("project", employee.getProject());
				maps.put("departmentid", String.valueOf(employee.getDepartmentid()));
				maps.put("dutyid", String.valueOf(employee.getDutyid()));
				maps.put("projectid", String.valueOf(employee.getProjectid()));
				maps.put("fromDate", fromDate);
				maps.put("toDate", toDate);
				maps.put("nfltitle", title);
				maps.put("totaldays", String.valueOf(days));
				maps.put("proceid", String.valueOf(proce.getIdx()));
				maps.put("nflreason", reason);
	 */
	public static final String SENDNOTEFORLEAVE_URL	= "/NuoEnSys/control/center/noteforleave_androidAdd";
	
	/**
	 * 处理的假条列表
	 * page, id, state
	 */
	public static final String NOTEFORLEAVE_HANDLELIST_URL = "/NuoEnSys/control/center/noteforleave_androidHandleList";
	
	/**
	 * 我的假条列表
	 * page, createid, state
	 */
	public static final String NOTEFORLEAVE_LIST_URL = "/NuoEnSys/control/center/noteforleave_androidList";
	
	/**
	 * 假条删除
	 * id
	 */
	public static final String NOTEFORLEAVE_DELETE_URL = "/NuoEnSys/control/center/noteforleave_androidDelete";
	
	/**
	 * 同意假条
	 */
	public static final String NOTEFORLEAVE_ACCESS_URL = "/NuoEnSys/control/center/noteforleave_androidAccess";
}
