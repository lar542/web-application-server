package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
	
	private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	private DataOutputStream dos;
	private Map<String, String> headers = new HashMap<String, String>(); //응답 헤더 맵
	
	/**
	 * 응답 데이터를 담을 OutputStream으로 보조스트림인 DataOutputStream 생성 
	 * @param out
	 */
	public HttpResponse(OutputStream out) {
		dos = new DataOutputStream(out);
	}
	
	public void addHeader(String headerName, String headerValue){
		headers.put(headerName, headerValue);
	}

	/**
	 * 리다이렉트 처리(302 상태코드)를 나타내는 response line과
	 * 응답 헤더와
	 * 리다이렉트할 위치를 나타내는 Location 헤더를
	 * 응답 데이터에 입력
	 */
	public void sendRedirect(String redirectUrl){
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			processHeaders();
			dos.writeBytes("Location: " + redirectUrl + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	public void forward(String url){
		try {
			byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath()); //webapp 아래의 파일을 body 배열로 읽음

			//요청 URL의 확장자에 따라 Content-Type 응답 헤더를 다르게 지정
			if(url.endsWith(".css")){ 
				headers.put("Content-Type", "text/css");
			}else if(url.endsWith(".js")){
				headers.put("Content-Type", "application/javascript");
			}else{
				headers.put("Content-Type", "text/html;charset=utf-8");
			}
			headers.put("Content-Length", body.length + ""); //body의 길이를 나타내는 응답 헤더
			response200Header(); //response line + response headers를 응답 데이터에 입력
			responseBody(body); //response body를 응답 데이터에 입력하고 내보냄
			
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	public void forwardBody(String body){
		byte[] contents = body.getBytes();
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Content-Length", contents.length + "");
        response200Header();
        responseBody(contents);
	}
	
	private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
	
	/**
	 * 처리 성공에 대한 response line과
	 * 응답 헤더를
	 * 응답 데이터에 입력
	 */
	private void response200Header() {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            processHeaders();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
	
	/**
	 * body에 있는 컨텐츠에 대한 부가정보(메타데이터)를 나타내는 응답 헤더들를 응답 데이터에 입력
	 */
	private void processHeaders(){
		try {
			Set<String> keys = headers.keySet();
			for(String key : keys){
				dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
