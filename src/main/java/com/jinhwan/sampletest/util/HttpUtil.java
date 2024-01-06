package com.jinhwan.sampletest.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * HTTP 호출을 위한 재사용 클래스
 * 
 *
 */
public class HttpUtil {
	private final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	public static JSONObject send(String url_path, String method, String bodyString) {
		try {
			// HTTP 요청을 위한 URL 오브젝트 생성
			URL url = new URL(url_path);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod(method);
			con.setRequestProperty(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
			con.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8");
			
			// 리퀘스트 바디 전송
			if(!StringUtil.isEmpty(bodyString)) {
				OutputStream os = con.getOutputStream();
				os.write(bodyString.getBytes());
				os.flush();
				os.close();
				
				System.out.print("HttpUtil.sendParam:" + bodyString);
			}

			// 응답 코드 확인
			int responseCode = con.getResponseCode();
			System.out.println("[HttpRequest] POST Response Code :: " + responseCode + "	message ::" + con.getResponseMessage());

			BufferedReader br;
			if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_NO_CONTENT) { // 정상 응답
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				System.out.println("[HttpRequest] POST request not worked");
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			

			// 응답 바디 read
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();

			// 응답 문자열 확인
			System.out.println("RESPONSE_STRING : " + URLDecoder.decode(response.toString(), "UTF-8"));

			// 응답 문자열 인코딩, JSONObject 변환
			JSONParser parser = new JSONParser();
			JSONObject json = null;
			if(!StringUtil.isEmpty(response.toString())) {
				Object obj = parser.parse(URLDecoder.decode(response.toString(), "UTF-8"));
				json = (JSONObject) obj;
			}

			// 토큰 반환
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
