package com.morn.testweb.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.mornframework.context.annotation.Value;

import com.morn.testweb.beans.IBatisDao;
import com.morn.testweb.dao.IMenuDao;

@IBatisDao("local_dataSource")
public class MenuDaoImpl implements IMenuDao{

	private DataSource dataSource;
	
	@Value("${app.url}")
	private String url;
	
	public void search() {
		System.out.println("MenuDaoImpl.search() dataSource:" + dataSource + ",url:" + url);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(" select * from prov_city ");
			rs = ps.executeQuery();
			while(rs.next()){
				System.out.println(rs.getString("id") + "\t" + rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
}
