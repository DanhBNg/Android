
package com.example.tangthucac.API;

import com.example.tangthucac.Story;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FirebaseAPI {
    @GET("stories.json")
    Call<Map<String, Story>> getStories();
}