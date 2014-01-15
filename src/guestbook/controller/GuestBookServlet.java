package guestbook.controller;

import guestbook.model.GuestBookBean;
import guestbook.model.GuestBookDAO;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 
 public class GuestBookServlet extends javax.servlet.http.HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request,response);
	}  	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request,response);
	}
	protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("euc-kr");
		String command=request.getParameter("command");
		
		if(command == null){
			command = "list";
		}		
		
		if(command.equals("list")){
			GuestBookBean [] list = GuestBookDAO.getAllContents();			
			request.setAttribute("list", list);
			
			RequestDispatcher rd=request.getRequestDispatcher("list.jsp");
			rd.forward(request, response);
			return;
			
		}else if(command.equals("write")){
			
			String title=request.getParameter("title");
			String author=request.getParameter("author");				
			String email=request.getParameter("email");				
			String content=request.getParameter("content");				
			String password=request.getParameter("password");
			
			if(title == null || title.trim().length() == 0 ||
			author == null || author.trim().length() == 0 ||
			email == null || email.trim().length() == 0 ||
			content == null || content.trim().length() == 0 ||
			password == null || password.trim().length() == 0 ){
				response.sendRedirect("write.html");
				return;
			}
				
			GuestBookBean gContent=new GuestBookBean(title,author,email,content,password);
			boolean result = GuestBookDAO.writeContent(gContent);
		
			if(result){
				response.sendRedirect("guestbook.do"); 
				return;
			}else{
				response.sendRedirect("error.jsp");
				return;					
			}
		}else if(command.equals("read")){
			String strNum=request.getParameter("num");
			
			if(strNum==null){
				response.sendRedirect("guestbook.do");
				return;
			}
			int num=Integer.parseInt(strNum);
			
			GuestBookBean gContent =GuestBookDAO.getContent(num, true);
			if(gContent == null){
				response.sendRedirect("error.jsp");
				return;						
			}else{
				request.setAttribute("resultContent", gContent);
				
				RequestDispatcher rd=request.getRequestDispatcher("/read.jsp");
				rd.forward(request, response);
				return;
			}
		}else if(command.equals("updateForm")){
			
			String strNum=request.getParameter("num");
			if(strNum == null || strNum.trim().length() == 0){
				response.sendRedirect("guestbook.do");
				return;				
			}
			int num = Integer.parseInt(strNum);
			GuestBookBean gContent =GuestBookDAO.getContent(num, false);
			if(gContent == null){
				response.sendRedirect("error.jsp");
				return;						
			}else{
				request.setAttribute("resultContent", gContent);
				
				RequestDispatcher rd=request.getRequestDispatcher("update.jsp");
				rd.forward(request, response);
				return;
			}
		}else if(command.equals("update")){
			String strNum = request.getParameter("num");
			
			String title=request.getParameter("title");
			String author=request.getParameter("author");				
			String email=request.getParameter("email");				
			String content=request.getParameter("content");
			System.out.println(content);
			String password=request.getParameter("password");
			
			if(strNum == null || strNum.trim().length() == 0 ||
				title == null || title.trim().length() == 0 ||
				author == null || author.trim().length() == 0 ||
				email == null || email.trim().length() == 0 ||
				content == null || content.trim().length() == 0 ||
				password == null || password.trim().length() == 0 ){
					response.sendRedirect("guestbook.do");
					return;
			}
			int num=Integer.parseInt(strNum);
			GuestBookBean gContent=new GuestBookBean(num,title,author,email,content,password);
			boolean result = GuestBookDAO.updateContent(gContent);
			if(result){
				response.sendRedirect("guestbook.do");
				return;
			}else{
				response.sendRedirect("error.jsp");
				return;
			}

		}else if(command.equals("delete")){
			String strNum=request.getParameter("num");
			String password = request.getParameter("password");
			
			if(strNum == null || strNum.trim().length() == 0 ||
				password == null || password.trim().length() == 0){
				response.sendRedirect("guestbook.do");
				return;				
			}
			int num = Integer.parseInt(strNum);
			boolean result = GuestBookDAO.deleteContent(num,password);
			if(result){
				response.sendRedirect("guestbook.do");
				return;
			}else{
				response.sendRedirect("error.jsp");
				return;				
			}
		}
	}
}