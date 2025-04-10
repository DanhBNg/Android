package com.example.tangthucac;


import android.util.Log;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class OpenAIHelper {
    private static final String TAG = "GeminiAPI";
    private static final String API_KEY = "AIzaSyC3YdOPnUA1SFjgK_iuB3t79DvaNq5U3Go";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    public static void sendQuestion(String message, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();

        try {
            JSONObject part = new JSONObject();
            part.put("text", message);

            JSONObject content = new JSONObject();
            content.put("parts", new JSONArray().put(part));

            json.put("contents", new JSONArray().put(content));

            RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(API_URL + "?key=" + API_KEY)
                    .post(body)
                    .build();

            Log.d(TAG, "Request body: " + json.toString());

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi tạo yêu cầu gửi Gemini", e);
        }
    }


    public static String parseGeminiResponse(String jsonResponse) {
        try {
            Log.d(TAG, "Raw response: " + jsonResponse);
            JSONObject obj = new JSONObject(jsonResponse);

            if (obj.has("error")) {
                JSONObject err = obj.getJSONObject("error");
                return "Lỗi từ Gemini: " + err.getString("message");
            }

            JSONArray candidates = obj.getJSONArray("candidates");
            if (candidates.length() > 0) {
                JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");
                if (parts.length() > 0) {
                    return parts.getJSONObject(0).getString("text");
                }
            }

            return "Không có nội dung phản hồi từ Gemini.";
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi phân tích phản hồi", e);
            return "Lỗi khi phân tích phản hồi từ Gemini.";
        }
    }
}
