package com.huythanh0x.training_thanhvh_java_spring_jwt_jpa.crawler_runner.crawler;

import com.huythanh0x.training_thanhvh_java_spring_jwt_jpa.crawler_runner.base.CouponUrlCrawlerBase;
import com.huythanh0x.training_thanhvh_java_spring_jwt_jpa.crawler_runner.fetcher.WebContentFetcher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Component class for crawling coupon URLs from the Enext API.
 * Extends CouponUrlCrawlerBase.
 */
@Component
public class EnextCrawler extends CouponUrlCrawlerBase {
    String apiUrl = "https://jobs.e-next.in/public/assets/data/udemy.json";
    int maxCouponRequest;

    EnextCrawler(@Value("${custom.number-of-enext-coupon}") int maxCouponRequest) {
        this.maxCouponRequest = maxCouponRequest;
    }

    /**
     * Retrieves all coupon URLs from an API based on the provided URL.
     * @return a list of strings containing all the coupon URLs
     */
    @Override
    public List<String> getAllCouponUrls() {
        var jsonArray = fetchListJsonFromAPI(apiUrl);
        List<String> allUrls = new ArrayList<>();
        for (var jo : jsonArray) {
            JSONObject jsonObject = (JSONObject) jo;
            allUrls.add(extractCouponUrl(jsonObject));
        }
        System.out.println("Enext jsonArray length: " + jsonArray.length() + " maxCouponRequest: " + maxCouponRequest);
        if (maxCouponRequest < allUrls.size()) {
            return allUrls.subList(0, maxCouponRequest);
        } else {
            return allUrls;
        }
    }

    /**
     * Extracts the coupon url from the provided JSONObject.
     *
     * @param jsonObject the JSONObject containing the coupon url and code
     * @return the formatted URL with the coupon code appended
     */
    String extractCouponUrl(JSONObject jsonObject) {
        try {
            String url = jsonObject.getString("url");
            String couponCode = jsonObject.getString("code");
            return String.format("https://www.udemy.com/course/%s/?couponCode=%s", url, couponCode);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Fetches a JSON array from an API using the specified API URL.
     *
     * @param apiUrl the URL of the API to fetch the JSON array from
     * @return a JSON array retrieved from the API
     */
    public JSONArray fetchListJsonFromAPI(String apiUrl) {
        return new WebContentFetcher().getJsonArrayFrom(apiUrl);
    }
}
