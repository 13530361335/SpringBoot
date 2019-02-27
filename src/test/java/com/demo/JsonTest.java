package com.demo;

import com.demo.com.Result;
import com.demo.entity.UserInfo;
import com.demo.util.JsonUtil;


public class JsonTest {

    public static void main(String[] args)  {
        String json = "{\n" +
                "  \"code\": 200,\n" +
                "  \"message\": \"OK\",\n" +
                "  \"data\": {\n" +
                "    \"total\": 1,\n" +
                "    \"list\": [\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"account\": \"000001\",\n" +
                "        \"password\": \"123456\",\n" +
                "        \"emali\": \"761878367@qq.com\",\n" +
                "        \"phone\": \"13530361335\",\n" +
                "        \"address\": \"深圳\",\n" +
                "        \"name\": \"靖敏\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"pageNum\": 1,\n" +
                "    \"pageSize\": 2,\n" +
                "    \"size\": 1,\n" +
                "    \"startRow\": 1,\n" +
                "    \"endRow\": 1,\n" +
                "    \"pages\": 1,\n" +
                "    \"prePage\": 0,\n" +
                "    \"nextPage\": 0,\n" +
                "    \"isFirstPage\": true,\n" +
                "    \"isLastPage\": true,\n" +
                "    \"hasPreviousPage\": false,\n" +
                "    \"hasNextPage\": false,\n" +
                "    \"navigatePages\": 8,\n" +
                "    \"navigatepageNums\": [\n" +
                "      1\n" +
                "    ],\n" +
                "    \"navigateFirstPage\": 1,\n" +
                "    \"navigateLastPage\": 1\n" +
                "  }\n" +
                "}";
        Result result = JsonUtil.toObject(json, Result.class);
        System.out.println(result.getData());

        UserInfo userInfo =JsonUtil.toObject(json, UserInfo.class);
        System.out.println(userInfo.getAccount());

        String jsonString = JsonUtil.toString(result);
        System.out.println(jsonString);



    }
}
