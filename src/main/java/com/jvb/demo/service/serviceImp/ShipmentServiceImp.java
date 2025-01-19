package com.jvb.demo.service.serviceImp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.jvb.demo.config.GHNConfig;
import com.jvb.demo.dto.GHNItemDTO;
import com.jvb.demo.entity.Order;
import com.jvb.demo.entity.OrderDetail;
import com.jvb.demo.service.ShipmentService;
@Service
public class ShipmentServiceImp implements ShipmentService {
    @Override
    public float calculateShipFee(int to_district_id, String to_ward_code) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(GHNConfig.CALCULATE_SHIP_FEE_URL);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("from_district_id", GHNConfig.FROM_DISTRICT_ID));
        params.add(new BasicNameValuePair("service_id", GHNConfig.SERVICE_STANDARD_ID));
        params.add(new BasicNameValuePair("to_district_id", String.valueOf(to_district_id)));
        params.add(new BasicNameValuePair("to_ward_code", to_ward_code));
        params.add(new BasicNameValuePair("height", "20"));
        params.add(new BasicNameValuePair("length", "20"));
        params.add(new BasicNameValuePair("width", "20"));
        params.add(new BasicNameValuePair("weight", "1000"));
        params.add(new BasicNameValuePair("insurance_value", "3000000"));
        try {
            URI uri = new URIBuilder(httpGet.getURI()).addParameters(params).build();
            httpGet.setURI(uri);
            httpGet.setHeader("token", GHNConfig.TOKEN);
            httpGet.setHeader("shopid", GHNConfig.SHOP_ID);
            CloseableHttpResponse response = client.execute(httpGet);
            client.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                jsonString.append(line);
            }
            JSONObject jsonObject = new JSONObject(jsonString.toString());
            int response_code = jsonObject.getInt("code");
            if(response_code == 200){ // success code
                float ship_fee = jsonObject.getJSONObject("data").getFloat("total");
                return ship_fee;
            }else 
                System.out.println(jsonObject.getString("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public Map<String, String> createGHNOrder(Order order) throws Exception{
        JSONObject json = new JSONObject();
        json.put("required_note", "CHOXEMHANGKHONGTHU");
        json.put("client_order_code", DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())+order.getId().toString());
        json.put("to_name", order.getShipment().getName());
        json.put("to_phone", "0394127684"); //hard code bc GHN check if phone number exist
        json.put("to_address", order.getShipment().getAddress());
        json.put("to_ward_name", order.getShipment().getWard_name());
        json.put("to_district_name", order.getShipment().getDistrict_name());
        json.put("to_province_name", order.getShipment().getCity_name());
        json.put("weight", 1000);
        json.put("length", 20);
        json.put("width", 20);
        json.put("height", 20);
        json.put("service_id", Integer.valueOf(GHNConfig.SERVICE_STANDARD_ID));
        json.put("payment_type_id", 1); //shop pays shipping fee
        
        List<GHNItemDTO> listItem = new ArrayList<>();
        for(OrderDetail orderDetail : order.getOrderDetails()){
            GHNItemDTO item = new GHNItemDTO();
            item.setName(orderDetail.getProduct().getName());
            item.setQuantity(orderDetail.getQuantity());
            item.setWeight(150);
            item.setPrice(orderDetail.getPrice().longValue());
            listItem.add(item);
        }
        JSONArray jsonArr = new JSONArray(listItem);
        json.put("items", jsonArr);
        System.out.println(json.toString());

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(GHNConfig.CREATE_ORDER_URL);
        StringEntity stringEntity = new StringEntity(json.toString(), StandardCharsets.UTF_8);
        post.setHeader("content-type", "application/json");
        post.setHeader("token", GHNConfig.TOKEN);
        post.setHeader("shopid", GHNConfig.SHOP_ID);
        post.setEntity(stringEntity);
        CloseableHttpResponse response = client.execute(post);
        client.close();
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            resultJsonStr.append(line);
        }
        JSONObject resultJsonObject = new JSONObject(resultJsonStr.toString());
        Map<String, String> result = new HashMap<>();
        System.out.println(resultJsonObject);
        if(resultJsonObject.get("code").toString().equals("200") && resultJsonObject.get("message").toString().equals("Success")){
            System.out.println("success");
            JSONObject data = (JSONObject) resultJsonObject.get("data");
            result.put("response_message", "success");
            result.put("order_code", data.get("order_code").toString());
            result.put("ship_fee", data.get("total_fee").toString());
            result.put("expected_delivery_time", data.get("expected_delivery_time").toString());
        }else{
            System.out.println("GHN failed");
            result.put("response_message", "failed");
            result.put("error", resultJsonObject.get("message").toString());
        }
        return result;
    }

    @Override
    public boolean cancelGHNOrder(String orderCode) throws Exception {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray(Arrays.asList(orderCode));
        json.put("order_codes", jsonArray);
        StringEntity stringEntity = new StringEntity(json.toString(), StandardCharsets.UTF_8);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(GHNConfig.CANCELED_ORDER_URL);
        post.setHeader("content-type", "application/json");
        post.setHeader("token", GHNConfig.TOKEN);
        post.setHeader("shopid", GHNConfig.SHOP_ID);
        post.setEntity(stringEntity);
        CloseableHttpResponse response = client.execute(post);
        client.close();
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            resultJsonStr.append(line);
        }
        JSONObject resultJsonObject = new JSONObject(resultJsonStr.toString());
        System.out.println(resultJsonObject);
        if(resultJsonObject.getInt("code") == 200 && resultJsonObject.get("message").equals("Success"))
            return true;
        else
            return false;
    }

    
}
