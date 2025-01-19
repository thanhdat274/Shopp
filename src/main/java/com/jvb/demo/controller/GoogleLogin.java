package com.jvb.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin("*")
@RequestMapping("/google")
public class GoogleLogin {
    private static String GET_TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
    private static String GET_USER_INFOR_URL = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
    private static String CLIENT_ID = "374289014721-r1qkm5v05o9lnoj6ugqtlhq7fg0iilli.apps.googleusercontent.com";
    private static String SECRET_KEY = "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";

    @GetMapping("/login")
    public String loginGoogle(@CookieValue("token")String token,HttpServletResponse response) {
        System.out.println(token);
        Cookie cookie = new Cookie("test", "test");
        response.addCookie(cookie);
        return "login-google";
    }

    @RequestMapping(value="/do")
    public String doGoogle(@Param("code") String code, Model model,HttpServletResponse response) throws Exception {
        System.out.println(code);
        // String response = Request.Post(GET_TOKEN_URL)
		// 		.bodyForm(Form.form().add("client_id", CLIENT_ID)
		// 				.add("client_secret", SECRET_KEY)
		// 				.add("redirect_uri", "http://localhost:8080/oauth2/google")
        //                 .add("code", code)
		// 				.add("grant_type", "authorization_code").build())
		// 		.execute().returnContent().asString();

        // System.out.println(response);

        // CloseableHttpClient client = HttpClients.createDefault();
        // HttpPost post = new HttpPost(GET_TOKEN_URL);
        // JSONObject json = new JSONObject();
        // json.put("client_id", CLIENT_ID);
        // json.put("client_secret", SECRET_KEY);
        // json.put("redirect_uri", "http://localhost:8080/oauth2/google");
        // json.put("code", code);
        // json.put("grant_type", "authorization_code");
        // post.setEntity(new StringEntity(json.toString()));
        // CloseableHttpResponse res = client.execute(post);
        // System.out.println(res);

        Cookie cookie = new Cookie("token", code);
        response.addCookie(cookie);
        return "login-google";
    }


    public Map<String, String> getUserInfor(String token) {
        CloseableHttpClient client = HttpClients.createDefault();
        Map<String, String> result = new HashMap<>();
        return result;
    }
}
