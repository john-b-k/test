package guestbook.model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class GuestBookDAO {

	static DataSource source = null;
	
	static{		 
		try {
			Context initContext = new InitialContext();		
			Context envContext			
			= (Context)initContext.lookup("java:/comp/env");
			
			source = (DataSource)envContext.lookup("jdbc/mycubrid");
		} catch (NamingException e) {			
			e.printStackTrace();
		}	
	}
		
	
	// 게시물 등록
	public static boolean writeContent(GuestBookBean vo){
		Connection con = null;	
		PreparedStatement pstmt = null;
		boolean result = false;
		
		String sql = "INSERT INTO gbook VALUES(null,?,?,?,?,?,sysdate,0)";
		
		try {
			con = source.getConnection();
			pstmt = con.prepareStatement(sql);

	        pstmt.setString(1,vo.getTitle());
	        pstmt.setString(2,vo.getAuthor());
	        pstmt.setString(3, vo.getEmail());
	        pstmt.setString(4, vo.getContent());
	        pstmt.setString(5, vo.getPassword());
			int count = pstmt.executeUpdate();			
			if(count != 0){
				result = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(pstmt, con);
		}
		return result;		
	}
	
	// 게시물 조회 - boolean flag 값 = read인 경우 true, update인 경우  false
	public static GuestBookBean getContent(int  num, boolean flag){		
		Connection con = null;	
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		GuestBookBean vo  = null;
		String sql1="UPDATE gbook set readnum=readnum+1 WHERE num=?";	
		
		String sql2="SELECT title,author,email,content,password, to_char(writeday, 'YYYY-MM-DD HH:MI:SS AM') , readnum from gbook WHERE num=?";

		try {
			con = source.getConnection();
			if(flag){
				pstmt = con.prepareStatement(sql1);
				pstmt.setInt(1, num);
				int count = pstmt.executeUpdate();
				if(count == 0){
					return vo;
				}
			}
			pstmt = con.prepareStatement(sql2);
			pstmt.setInt(1, num);	
			rset = pstmt.executeQuery();
			
			if(rset.next()){
				vo = 	new GuestBookBean(num,rset.getString(1),
						rset.getString(2),rset.getString(3),rset.getString(4).replaceAll("</n>","<br>"),
						rset.getString(5),rset.getString(6),rset.getInt(7));
			}

			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(pstmt, con);
		}
		return vo;
	}
	
	//게시물 삭제
	public  static boolean deleteContent(int num, String password){
		Connection con = null;	
		PreparedStatement pstmt = null;
		boolean result = false;
		
		String sql="DELETE FROM gbook WHERE num=? and password=?";
		
		try {
			con = source.getConnection();
			pstmt = con.prepareStatement(sql);

	        pstmt.setInt(1,num);
	        pstmt.setString(2,password);

			int count = pstmt.executeUpdate();
			
			if(count != 0){
				result = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(pstmt, con);
		}
		return result;
	}
	
	// 게시물 수정
	public  static boolean updateContent(GuestBookBean vo){
		Connection con = null;	
		PreparedStatement pstmt = null;
		boolean result = false;
		
		String sql="UPDATE gbook set title=?,author=?,email=?,content=? WHERE num=? AND password=?";
		
		try {
			con = source.getConnection();
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1,vo.getTitle());
		    pstmt.setString(2,vo.getAuthor());
		    pstmt.setString(3, vo.getEmail());
		    pstmt.setString(4, vo.getContent());
		    pstmt.setInt(5, vo.getNum());
		    pstmt.setString(6, vo.getPassword());
			pstmt.executeUpdate();

			int count = pstmt.executeUpdate();
			
			if(count != 0){
				result = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(pstmt, con);
		}
		return result;
		
	}
	
	// 모든 게시물 조회
	public  static GuestBookBean[] getAllContents(){
		Connection con = null;	
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		GuestBookBean[] list  = null;
		ArrayList alist = new ArrayList();
		
		String sql="SELECT num,title,author,email,content,password,to_char(writeday, 'YYYY-MM-DD HH:MI:SS AM'), readnum from gbook order by num desc";	
		
		try {
			con = source.getConnection();
			pstmt = con.prepareStatement(sql);
			
			rset = pstmt.executeQuery();
			while(rset.next()){
			
				alist.add(	new GuestBookBean(rset.getInt(1),rset.getString(2),
						rset.getString(3),rset.getString(4),rset.getString(5)
		 				,rset.getString(6),rset.getString(7),rset.getInt(8)));
			}
			list = new GuestBookBean[alist.size()];
			alist.toArray(list);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(rset, pstmt, con);
		}
		return list;
	}
	public  static void close(Statement stmt, Connection con){
		try{
			if(stmt != null){
				stmt.close();
				stmt = null;
			}
			if(con != null){
				con.close();
				con = null;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public  static void close(ResultSet rset, Statement stmt, Connection con){
		try{
			if(rset != null){
				rset.close();			
			}
			if(stmt != null){
				stmt.close();				
			}
			if(con != null){
				con.close();				
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
