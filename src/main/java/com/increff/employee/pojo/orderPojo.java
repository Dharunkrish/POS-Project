package com.increff.employee.pojo;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class orderPojo {

	@Id
	private int id;
    private Timestamp sqlTimestamp;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Timestamp getSqlTimestamp() {
		return sqlTimestamp;
	}
	public void setSqlTimestamp(Timestamp sqlTimestamp) {
		this.sqlTimestamp = sqlTimestamp;
	}


}
