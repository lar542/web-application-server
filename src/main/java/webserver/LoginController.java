package webserver;

import model.User;
import db.DataBase;

public class LoginController extends AbstractController{

	/**
     * 로그인
     * @param request
     * @param response
     */
	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		User user = DataBase.findUserById(request.getParameter("userId")); //요청 정보의 userId 파라미터에 해당되는 유저 정보
		if(user != null){ //유저 정보가 있다면
			if(user.login(request.getParameter("password"))){ //비밀번호가 맞으면
				response.addHeader("Set-Cookie", "logined=true"); //응답 헤더에 Set-Cookie 헤더 추가
				response.sendRedirect("/index.html"); //index 페이지로 리다이렉트
			}else{
				response.sendRedirect("/user/login_failed.html"); //로그인 실패 화면으로 리다이렉트
			}
		}else{ //유저 정보가 없다면
			response.sendRedirect("/user/login_failed.html"); //로그인 실패 화면으로 리다이렉트
		}
	}

}
