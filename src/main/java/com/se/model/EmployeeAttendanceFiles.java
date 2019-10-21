package com.se.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "EMPLOYEE_ATTENDANCE_FILES")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class EmployeeAttendanceFiles {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "emp_att_fi_id_Sequence")
	@SequenceGenerator(name = "emp_att_fi_id_Sequence", sequenceName = "emp_att_fi_seq")
	private Long id;
	private String fileName;
	private Long importFlag;
	private Long notificationFlag;
	@Column(name = "STORE_DATE", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date storeDate;

	public EmployeeAttendanceFiles(String fileName, Long importFlag, Long notificationFlag) {
		super();
		this.fileName = fileName;
		this.importFlag = importFlag;
		this.notificationFlag = notificationFlag;
	}

	public EmployeeAttendanceFiles() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getImportFlag() {
		return importFlag;
	}

	public void setImportFlag(Long importFlag) {
		this.importFlag = importFlag;
	}

	public Long getNotificationFlag() {
		return notificationFlag;
	}

	public void setNotificationFlag(Long notificationFlag) {
		this.notificationFlag = notificationFlag;
	}

	public Date getStoreDate() {
		return storeDate;
	}

	public void setStoreDate(Date storeDate) {
		this.storeDate = storeDate;
	}

}
