package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import db.DataBase;

public class CreateUserController extends AbstractController{
	private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);
	
	/**
     * 요청 데이터의 파라미터 값으로 유저 객체 생성
     * @param request 요청 데이터
     * @param response 응답 데이터
     */
	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		User user = new User(
			request.getParameter("userId"), 
			request.getParameter("password"), 
			request.getParameter("name"), 
			request.getParameter("email"));
		log.debug("New User : {}", user);
		DataBase.addUser(user); 
		response.sendRedirect("/index.html"); //index 페이지로 리다이렉트
	}

}
