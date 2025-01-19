package com.jvb.demo.service.serviceImp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.jvb.demo.config.MomoConfig;
import com.jvb.demo.service.MomoService;

@Service
public class MomoServiceImp implements MomoService {
	// return payment url
	@Override
	public String createPayment(Long order_id, Float amount) {
		String tempOrder_id = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()) + order_id;
		JSONObject json = new JSONObject();
		String partnerCode = MomoConfig.PARTNER_CODE;
		String accessKey = MomoConfig.ACCESS_KEY;
		String secretKey = MomoConfig.SECRET_KEY;
		String returnUrl = MomoConfig.RETURN_URL;
		String notifyUrl = MomoConfig.NOTIFY_URL;
		json.put("partnerCode", partnerCode);
		json.put("accessKey", accessKey);
		json.put("requestId", String.valueOf(System.currentTimeMillis()));
		json.put("amount", String.valueOf(amount.longValue()));
		json.put("orderId", tempOrder_id);
		json.put("orderInfo", "Thanh toan don hang " + tempOrder_id);
		json.put("returnUrl", returnUrl);
		json.put("notifyUrl", notifyUrl);
		json.put("requestType", "captureMoMoWallet");
		String data = "partnerCode=" + partnerCode
				+ "&accessKey=" + accessKey
				+ "&requestId=" + json.get("requestId")
				+ "&amount=" + json.get("amount")
				+ "&orderId=" + json.get("orderId")
				+ "&orderInfo=" + json.get("orderInfo")
				+ "&returnUrl=" + returnUrl
				+ "&notifyUrl=" + notifyUrl
				+ "&extraData=";
		try {
			String hashData = signHmacSHA256(data, secretKey);
			json.put("signature", hashData);
			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost post = new HttpPost(MomoConfig.CREATE_ORDER_URL);
			StringEntity stringEntity = new StringEntity(json.toString());
			post.setHeader("content-type", "application/json");
			post.setEntity(stringEntity);

			CloseableHttpResponse res = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
			StringBuilder resultJsonStr = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				resultJsonStr.append(line);
			}
			JSONObject result = new JSONObject(resultJsonStr.toString());
			System.out.println(result);
			if (result.get("message").equals("Success"))
				return result.get("payUrl").toString();
			else
				System.out.println(result.get("message"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String signHmacSHA256(String data, String secretKey)
			throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(secretKeySpec);
		byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
		return toHexString(rawHmac);
	}

	private String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		Formatter formatter = new Formatter(sb);
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}
		formatter.close();
		return sb.toString();
	}
}
