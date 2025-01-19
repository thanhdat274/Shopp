package com.jvb.demo.config;

public class GHNConfig {
    public static final String TOKEN = "201937cd-db8b-11ed-ab31-3eeb4194879e";
    public static final String SHOP_ID = "123933";
    public static final String FROM_DISTRICT_ID = "1490";
    public static final String SERVICE_STANDARD_ID = "53320";
    public static final String API_ENDPOINT = "https://dev-online-gateway.ghn.vn/shiip/public-api";
    public static final String CREATE_ORDER_URL = API_ENDPOINT + "/v2/shipping-order/create";
    public static final String CANCELED_ORDER_URL = API_ENDPOINT + "/v2/switch-status/cancel";
    public static final String CALCULATE_SHIP_FEE_URL = API_ENDPOINT + "/v2/shipping-order/fee";
}
