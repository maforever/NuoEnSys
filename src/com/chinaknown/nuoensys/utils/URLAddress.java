package com.chinaknown.nuoensys.utils;

public class URLAddress {

	/**
	 * �û���½��ַ
	 *http://mmqforever.eicp.net:6060/NuoEnSys/control/center/employee_login?username=admin&password=admin&type=2
	 */
	public static final String LOGIN_URL = "http://mmqforever.eicp.net:6060/NuoEnSys/control/center/employee_login";
	
	public static final String BASIC_URL = "http://mmqforever.eicp.net:6060";
	/**
	 * ��˾�����б��ַ
	 * page ҳ��
	 */
	public static final String COMPANYNEWS_URL = "http://mmqforever.eicp.net:6060/NuoEnSys/control/center/companyNew_androidNewlist";
	
	/**
	 * �����Ѿ��������ܱ��б��ַ
	 * page ҳ��
	 */
	public static final String SENDEDREPORT_URL = "http://mmqforever.eicp.net:6060/NuoEnSys/control/center/report_androidSendedReportlist";
	
	/**
	 * ���ҵ�ǰ��½�˽��յ����ܱ��б��ַ
	 * page ҳ��
	 */
	public static final String READREPORT_URL = "http://mmqforever.eicp.net:6060/NuoEnSys/control/center/report_androidReadReportList";
	
	/**
	 * ���������
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
	 * ����ļ����б�
	 * page, id, state
	 */
	public static final String NOTEFORLEAVE_HANDLELIST_URL = "/NuoEnSys/control/center/noteforleave_androidHandleList";
	
	/**
	 * �ҵļ����б�
	 * page, createid, state
	 */
	public static final String NOTEFORLEAVE_LIST_URL = "/NuoEnSys/control/center/noteforleave_androidList";
	
	/**
	 * ����ɾ��
	 * id
	 */
	public static final String NOTEFORLEAVE_DELETE_URL = "/NuoEnSys/control/center/noteforleave_androidDelete";
	
	/**
	 * ͬ�����
	 */
	public static final String NOTEFORLEAVE_ACCESS_URL = "/NuoEnSys/control/center/noteforleave_androidAccess";
}
