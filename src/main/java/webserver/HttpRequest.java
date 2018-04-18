package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.IOUtils;

/**
 * 요청 데이터를 처리하는 클래스 
 */
public class HttpRequest {
	
	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
	private Map<String, String> headers = new HashMap<String, String>(); //헤더를 저장할 헤더 맵
	private Map<String, String> params = new HashMap<String, String>(); //전달된 파라미터를 저장할 파라미터 맵
	private RequestLine requestLine;
	
	/**
	 * @param InputStream in 요청 데이터를 담고 있음
	 */
	public HttpRequest(InputStream in) {
		try {
			//InputStream을 한 줄 단위로 읽기 위해 문자기반 보조스트림인 BufferedReader 생성
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8")); //InputStreamReader : 바이트기반 스트림 <-> 문자기반 스트림
			
			//HTTP 요청에서 첫 번째 줄(request line)을 읽음
        	String line = br.readLine(); 
        	if(line == null){
        		return;
        	}
        	//request line에서 method에 따라 URL과 파라미터 분리
        	requestLine = new RequestLine(line);
        	
        	line = br.readLine(); //그 다음 줄 읽음 
        	
        	while(!"".equals(line)){ //공백 문자열이 나올 때까지 요청 헤더를 읽어들인다.(공백 문자열 이후가 body파트)
        		log.debug("header : {}", line); //요청 헤더 출력
        		String tokens[] = line.split(":");
        		headers.put(tokens[0].trim(), tokens[1].trim()); //현재 줄의 헤더명과 헤더값을 헤더 맵에 저장
        		line = br.readLine(); //그 다음 줄 읽음
        	}
        	
        	//POST 방식 이면 요청 헤더로 body의 길이를 나타내는 Content-Length가 포함된다.
        	if(getMethod().isPost()){
        		String bodyQueryString = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length"))); //body 길이 수만큼 body 영역의 쿼리스트링을 읽어들임
        		params = HttpRequestUtils.parseQueryString(bodyQueryString); //파라미터 맵에 저장
        		
        	}else{ //GET 방식이면
        		params = requestLine.getParams();
        	}
        	
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * @return request line의 요청 method
	 */
	public HttpMethod getMethod(){
		return requestLine.getMethod();
	}
	
	/**
	 * @return request line의 요청 URL
	 */
	public String getPath(){
		return requestLine.getPath();
	}
	
	/**
	 * @param name 요청 헤더명
	 * @return 요청 헤더 값
	 */
	public String getHeader(String name){
		return headers.get(name);
	}
	
	/**
	 * @param name 파라미터명
	 * @return 파라미터 값
	 */
	public String getParameter(String name){
		return params.get(name);
	}
}
