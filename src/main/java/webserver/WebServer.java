package webserver;

import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebServer 클래스는 웹 서버를 구동하고,
 * 서버 구동 후 대기상태에서 요청이 들어오면
 * 요청을 RequestHandler 클래스에 위임하는 역할을 한다.
 */
public class WebServer {
	private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 9090;

    public static void main(String args[]) throws Exception {
    	int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }
        
        //서버를 구동하면 저장된 포트번호의 ServerSocket을 생성하고 대기
        //ServerSocket : 소켓이 연결되려면 서버소켓을 통해야 한다.(창구역할) 
        //				외부에서 소켓 연결 요청이 오면 클라이언트 측 소켓과 통신할 서버 측 소켓을 만들고 서로 연결한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            //클라이언트가 연결될때까지 대기한다.
            Socket connection; 
            while ((connection = listenSocket.accept()) != null) { //저장된 포트번호로 접속 요청이 발생하면
            	//Socket을 RequestHandler로 전달
            	RequestHandler requestHandler = new RequestHandler(connection);
                requestHandler.start(); //새로운 스레드 실행(멀티스레드 프로그래밍)
            }
        }
    }
}
