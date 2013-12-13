package com.chinaknown.nuoensys.model;

import java.io.Serializable;

public class NoteForLeave implements Serializable{

	private Integer idx;
	private String title;
	private Integer creatorid;   //假条申请人的idx
	private Integer departmentId;
	private String department;  //部门
	private Integer dutyId;
	private String duty;    //职务
	private String project;  //参与项目
	private Integer projectId; 
	private String creator;  //假条申请人的真实姓名
	private Integer days;	 //请假总共天数
	private String reason;   //请假原因
	private String approver;    //假条批准人的真实姓名
	private Integer approverIdx;  
	private String applicationDate;     //申请日期
	private String approvaldate;        //批复日期
	private String startDate;           //开始日期
	private String endDate;             //结束日期
	private Integer state;   //假条状态 0申请中， 1通过，2未通过 ，3已过期,4已撤销
	private String procedureName;     //申请路程名称
	private Integer procedureIdx;	  //申请流程IDX
	private String proce;             //流程指示
	private String refusalreason;  //拒绝原因
	private int location = 0;        //等待处理的位置      如路程是A→B→C→D,刚填写玩申请是位置是0， 等待的是数组0的位置及A
	private String locationName;        //等待处理的实际名称
	private Integer userIsVisible;      //申请人是否是在职状态，
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getCreatorid() {
		return creatorid;
	}
	public void setCreatorid(Integer creatorid) {
		this.creatorid = creatorid;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Integer getDutyId() {
		return dutyId;
	}
	public void setDutyId(Integer dutyId) {
		this.dutyId = dutyId;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public Integer getApproverIdx() {
		return approverIdx;
	}
	public void setApproverIdx(Integer approverIdx) {
		this.approverIdx = approverIdx;
	}
	public String getApplicationDate() {
		return applicationDate;
	}
	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
	}
	public String getApprovaldate() {
		return approvaldate;
	}
	public void setApprovaldate(String approvaldate) {
		this.approvaldate = approvaldate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getProcedureName() {
		return procedureName;
	}
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	public Integer getProcedureIdx() {
		return procedureIdx;
	}
	public void setProcedureIdx(Integer procedureIdx) {
		this.procedureIdx = procedureIdx;
	}
	public String getProce() {
		return proce;
	}
	public void setProce(String proce) {
		this.proce = proce;
	}
	public String getRefusalreason() {
		return refusalreason;
	}
	public void setRefusalreason(String refusalreason) {
		this.refusalreason = refusalreason;
	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public Integer getUserIsVisible() {
		return userIsVisible;
	}
	public void setUserIsVisible(Integer userIsVisible) {
		this.userIsVisible = userIsVisible;
	}
	@Override
	public String toString() {
		return "NoteForLeave [idx=" + idx + ", title=" + title + ", creatorid="
				+ creatorid + ", departmentId=" + departmentId
				+ ", department=" + department + ", dutyId=" + dutyId
				+ ", duty=" + duty + ", project=" + project + ", projectId="
				+ projectId + ", creator=" + creator + ", days=" + days
				+ ", reason=" + reason + ", approver=" + approver
				+ ", approverIdx=" + approverIdx + ", applicationDate="
				+ applicationDate + ", approvaldate=" + approvaldate
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", state=" + state + ", procedureName=" + procedureName
				+ ", procedureIdx=" + procedureIdx + ", proce=" + proce
				+ ", refusalreason=" + refusalreason + ", location=" + location
				+ ", locationName=" + locationName + ", userIsVisible="
				+ userIsVisible + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((applicationDate == null) ? 0 : applicationDate.hashCode());
		result = prime * result
				+ ((approvaldate == null) ? 0 : approvaldate.hashCode());
		result = prime * result
				+ ((approver == null) ? 0 : approver.hashCode());
		result = prime * result
				+ ((approverIdx == null) ? 0 : approverIdx.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result
				+ ((creatorid == null) ? 0 : creatorid.hashCode());
		result = prime * result + ((days == null) ? 0 : days.hashCode());
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
		result = prime * result
				+ ((departmentId == null) ? 0 : departmentId.hashCode());
		result = prime * result + ((duty == null) ? 0 : duty.hashCode());
		result = prime * result + ((dutyId == null) ? 0 : dutyId.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + location;
		result = prime * result
				+ ((locationName == null) ? 0 : locationName.hashCode());
		result = prime * result + ((proce == null) ? 0 : proce.hashCode());
		result = prime * result
				+ ((procedureIdx == null) ? 0 : procedureIdx.hashCode());
		result = prime * result
				+ ((procedureName == null) ? 0 : procedureName.hashCode());
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		result = prime * result
				+ ((projectId == null) ? 0 : projectId.hashCode());
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result
				+ ((refusalreason == null) ? 0 : refusalreason.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result
				+ ((userIsVisible == null) ? 0 : userIsVisible.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NoteForLeave other = (NoteForLeave) obj;
		if (applicationDate == null) {
			if (other.applicationDate != null)
				return false;
		} else if (!applicationDate.equals(other.applicationDate))
			return false;
		if (approvaldate == null) {
			if (other.approvaldate != null)
				return false;
		} else if (!approvaldate.equals(other.approvaldate))
			return false;
		if (approver == null) {
			if (other.approver != null)
				return false;
		} else if (!approver.equals(other.approver))
			return false;
		if (approverIdx == null) {
			if (other.approverIdx != null)
				return false;
		} else if (!approverIdx.equals(other.approverIdx))
			return false;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (creatorid == null) {
			if (other.creatorid != null)
				return false;
		} else if (!creatorid.equals(other.creatorid))
			return false;
		if (days == null) {
			if (other.days != null)
				return false;
		} else if (!days.equals(other.days))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (departmentId == null) {
			if (other.departmentId != null)
				return false;
		} else if (!departmentId.equals(other.departmentId))
			return false;
		if (duty == null) {
			if (other.duty != null)
				return false;
		} else if (!duty.equals(other.duty))
			return false;
		if (dutyId == null) {
			if (other.dutyId != null)
				return false;
		} else if (!dutyId.equals(other.dutyId))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (idx == null) {
			if (other.idx != null)
				return false;
		} else if (!idx.equals(other.idx))
			return false;
		if (location != other.location)
			return false;
		if (locationName == null) {
			if (other.locationName != null)
				return false;
		} else if (!locationName.equals(other.locationName))
			return false;
		if (proce == null) {
			if (other.proce != null)
				return false;
		} else if (!proce.equals(other.proce))
			return false;
		if (procedureIdx == null) {
			if (other.procedureIdx != null)
				return false;
		} else if (!procedureIdx.equals(other.procedureIdx))
			return false;
		if (procedureName == null) {
			if (other.procedureName != null)
				return false;
		} else if (!procedureName.equals(other.procedureName))
			return false;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		if (projectId == null) {
			if (other.projectId != null)
				return false;
		} else if (!projectId.equals(other.projectId))
			return false;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (refusalreason == null) {
			if (other.refusalreason != null)
				return false;
		} else if (!refusalreason.equals(other.refusalreason))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (userIsVisible == null) {
			if (other.userIsVisible != null)
				return false;
		} else if (!userIsVisible.equals(other.userIsVisible))
			return false;
		return true;
	}
	
	
	
	
	
}
