package webserver;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;

public class RequestLine {
	
	private static final Logger log = LoggerFactory.getLogger(RequestLine.class);
	private HttpMethod method; //요청 method
	private String path; //요청 URL
	private Map<String, String> params = new HashMap<String, String>(); //전달된 파라미터를 저장할 파라미터 맵 (GET인 경우)
	
	/**
	 * request line에서 method에 따라 URL과 파라미터 분리
	 * @param requestLine
	 */
	public RequestLine(String requestLine){
		log.debug("request line : {}", requestLine);
		String[] tokens = requestLine.split(" ");
		if(tokens.length != 3){//method url HTTP/1.1
			throw new IllegalArgumentException(requestLine + "이 형식에 맞지 않습니다.");
			//IllegalArgumentException : 메소드의 매개변수 유형을 잘못 사용할 때
			//RuntimeException(UncheckedException) : 주로 프로그램의 오류가 있을 때 발생하도록 의도된 것들
		}
		method = HttpMethod.valueOf(tokens[0]); //지정한 값과 일치하는 열거형 상수를 반환
		
		//POST인 경우 요청 URL을 저장하고 종료
		if(method.isPost()){ 
			path = tokens[1];
			return;
		}
		//GET인 경우 요청 URL과 파라미터를 분리
		int index = tokens[1].indexOf("?");
		if(index == -1){ //파라미터가 없는 경우
			path = tokens[1]; //URL 저장
		}else{ //파라미터가 있는 경우
			path = tokens[1].substring(0, index); //? 앞의 URL 저장
			params = HttpRequestUtils.parseQueryString(tokens[1].substring(index+1)); //? 뒤의 파라미터를 파라미터 맵에 저장
		}
	}
	
	/**
	 * 외부에서 요청 method를 조회하기 위한 메소드
	 * @return method
	 */
	public HttpMethod getMethod(){
		return method;
	}
	
	/**
	 * 외부에서 요청 URL를 조회하기 위한 메소드
	 * @return path
	 */
	public String getPath(){
		return path;
	}
	
	/**
	 * 외부에서 파라미터 맵을 조회하기 위한 메소드
	 * @return
	 */
	public Map<String, String> getParams(){
		return params;
	}
}
