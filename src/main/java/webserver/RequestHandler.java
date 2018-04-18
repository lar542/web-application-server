package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 요청 처리와 응답 처리를 담당하는 클래스
 */
public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        //Closeable 객체를 사용 후 자동으로 close해주는 try-with-resource 문법 (JDK 1.7 이후)
        try (
        	//클라이언트가 보낸 요청 데이터를 담고 있는 InputStream
        	InputStream in = connection.getInputStream(); 
        	//클라이언트로 보낼 응답 데이터를 담을 OutputStream
        	OutputStream out = connection.getOutputStream()) {

        	HttpRequest request = new HttpRequest(in); //HTTP 요청 데이터 처리
        	HttpResponse response = new HttpResponse(out); //HTTP 응답 데이터 처리
        	Controller controller = RequestMapping.getController(request.getPath()); //요청 URL에 해당되는 컨트롤러
        	
        	if(controller == null){ //요청 URL에 해당되는 컨트롤러가 없으면 
        		String path = getDefaultPath(request.getPath());
        		response.forward(path);
        	}else{
        		controller.service(request, response);
        	}

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    private String getDefaultPath(String path){
    	if(path.equals("/")){
    		return "/index.html";
    	}
    	return path;
    }
}