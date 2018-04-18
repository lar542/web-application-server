package webserver;

/**
 * 서로 연관성을 가진 상수 값은 enum을 사용하기 적합하다.
 */
public enum HttpMethod {
	GET, POST;
	
	/**
	 * @return 자신이 POST인지 여부
	 */
	public boolean isPost(){
		return this == POST;
	}
}

