package webserver;

/**
 * 들어온 요청의 method에 따라 다른 메소드를 분기하는 클래스
 */
public abstract class AbstractController implements Controller {

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		HttpMethod method = request.getMethod();
		
		if(method.isPost()){
			doPost(request, response);
		}else{
			doGet(request, response);
		}
	}

	protected void doPost(HttpRequest request, HttpResponse response) {

	}
	
	protected void doGet(HttpRequest request, HttpResponse response) {
		
	}
}
