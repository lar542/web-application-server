package webserver;

import java.util.Collection;
import java.util.Map;

import util.HttpRequestUtils;
import model.User;
import db.DataBase;

public class ListUserController extends AbstractController {

	/**
     * 회원 목록
     * @param request
     * @param response
     */
	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		//로그인되어있지않으면
		if(!isLogin(request.getHeader("Cookie"))){ 
			response.sendRedirect("/user/login.html"); //로그인화면으로 리다이렉트
			return;
		}
		//로그인되어있으면
		Collection<User> users = DataBase.findAll(); //모든 유저 정보를 가져와 StringBuilder 객체에 씀
		StringBuilder sb = new StringBuilder();
		sb.append("<table border='1'>");
		for(User user : users){
			sb.append("<tr>");
			sb.append("<td>" + user.getUserId() + "</td>");
			sb.append("<td>" + user.getName() + "</td>");
			sb.append("<td>" + user.getEmail() + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		response.forwardBody(sb.toString());
	}

	private boolean isLogin(String cookieValue){
    	Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieValue);
    	String value = cookies.get("logined");
    	if(value == null){
    		return false;
    	}
    	return Boolean.parseBoolean(value);
    }
}
