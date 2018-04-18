package webserver;

import java.util.HashMap;
import java.util.Map;

/**
 * 서비스하는 모든 URL과 Controller를 관리하고 있으며
 * 요청 URL에 해당되는 Controller를 반환하는 역할을 하는 클래스
 */
public class RequestMapping {

	private static Map<String, Controller> controllers = new HashMap<String, Controller>();
	
	static {
		controllers.put("/user/create", new CreateUserController());
		controllers.put("/user/login", new LoginController());
		controllers.put("/user/list", new ListUserController());
	}
	
	/**
	 * 요청 URL에 해당되는 Controller를 반환하는 메소드
	 * @param requestUrl
	 * @return Controller
	 */
	public static Controller getController(String requestUrl) {
		return controllers.get(requestUrl);
	}
}
