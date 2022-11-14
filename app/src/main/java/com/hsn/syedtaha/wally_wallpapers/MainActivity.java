package com.hsn.syedtaha.wally_wallpapers;

import static android.content.ContentValues.TAG;

import static java.lang.Math.floor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {

    private EditText searchEdit;
    private ImageView searchIV;
    private RecyclerView categoryRV,wallpaperRV;
    private ProgressBar loadingPB;
    private ArrayList<String> wallpaperArrayList;
    private ArrayList<CategoryRVModel> categoryRVModelArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private WallpaperRVAdapter wallpaperRVAdapter;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    int count=0;
    private int[] pageslist;
    // 563492ad6f91700001000001a487858e4280415b84167f0fa2e4700d

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        Banner Ad
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        searchEdit = findViewById(R.id.idEditSearch);
        searchIV = findViewById(R.id.idIVSearch);
        categoryRV = findViewById(R.id.idRVCategory);
        wallpaperRV = findViewById(R.id.idRVWallpapers);
        loadingPB = findViewById(R.id.idPBLoading);
        wallpaperArrayList = new ArrayList<>();
        categoryRVModelArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this,RecyclerView.HORIZONTAL,false);
        categoryRV.setLayoutManager(linearLayoutManager);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModelArrayList,this,this::onCategoryClick);
        categoryRV.setAdapter(categoryRVAdapter);

//        2 is the number of columns for wallpaper
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        wallpaperRV.setLayoutManager(gridLayoutManager);
        wallpaperRVAdapter = new WallpaperRVAdapter(wallpaperArrayList,this);
        wallpaperRV.setAdapter(wallpaperRVAdapter);

        getCategories();
        getWallpapers();

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = count+1;
                String searchStr = searchEdit.getText().toString();
                if(searchStr.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter the wallpaper you want to search", Toast.LENGTH_SHORT).show();
                }else {
                    getWallpaperByCategory(searchStr);
                }
            }
        });
    }

    private void getWallpaperByCategory(String category) {
        wallpaperArrayList.clear();
        loadingPB.setVisibility(View.VISIBLE);
        String url = "https://api.pexels.com/v1/search?query="+category+"&per_page=30&page=1";
        count = count+1;
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray photoArray = null;
                try {
                    photoArray = response.getJSONArray("photos");
                    for(int i=0; i<photoArray.length(); i++){
                        JSONObject photoObj = photoArray.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                        wallpaperArrayList.add(imgUrl);
                    }
                    loadingPB.setVisibility(View.GONE);
                    wallpaperRVAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String ,String>headers = new HashMap<>();
                headers.put("Authorization","563492ad6f91700001000001a487858e4280415b84167f0fa2e4700d");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }

    private void getWallpapers() {
        wallpaperArrayList.clear();
        loadingPB.setVisibility(View.VISIBLE);
//        String url = "https://api.pexels.com/v1/curated/?page=1&per_page=15";
        Log.d("one","log1");
        Log.d("one","log2");
        Log.d("one","log3");
        Log.d("one","log4");
        Log.d("one","log5");
        Log.d("one","log6");
        wall("https://api.pexels.com/v1/curated");
        Log.d("one",""+pageslist[0]);

        for(int i=1; i<=pageslist[0]; i++){
            String url = "https://api.pexels.com/v1/curated/?page="+i+"&per_page="+pageslist[1];
            Log.d("one","log7");
            Log.d("one","log8");
            Log.d("one","log9");
            Log.d("one","log10");
            Log.d("one","log11");
            Log.d("one","log12");
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray photoArray = response.getJSONArray("photos");
                    for(int j=0; j<photoArray.length(); j++){
                        JSONObject photoObj = photoArray.getJSONObject(j);
                        String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                        wallpaperArrayList.add(imgUrl);
                    }
//                    wallpaperRVAdapter.notifyDataSetChanged();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("Authorization","563492ad6f91700001000001a487858e4280415b84167f0fa2e4700d");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);}
        loadingPB.setVisibility(View.GONE);
        wallpaperRVAdapter.notifyDataSetChanged();

    }

    private void getCategories() {
        categoryRVModelArrayList.add(new CategoryRVModel("Architecture","https://images.unsplash.com/photo-1659132394880-d55f55b3a794?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Nature","https://images.unsplash.com/photo-1659296197284-199ec641f7f5?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=388&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModel("Street Photography","https://images.unsplash.com/photo-1658341232760-59b422626896?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1374&q=80"));
        categoryRVAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModelArrayList.get(position).getCategory();
        getWallpaperByCategory(category);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        if (count>0){
            count=0;
            getWallpapers();
        }else {
            super.onBackPressed();
        }

    }

    public void wall(String url) {
//        wallpaperArrayList.clear();
//        loadingPB.setVisibility(View.VISIBLE);
//        String url = "https://api.pexels.com/v1/curated/?page=2&per_page=15";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray photoArray = response.getJSONArray("photos");
                    int totalpages = countFunc(response);
                    int pages = photoArray.length();
                    Log.d("tag",""+pages);
                    Log.d("tag",""+totalpages);
                    pageslist[0] = totalpages;
                    pageslist[1] = pages;
//                    loadingPB.setVisibility(View.GONE);
//                    wallpaperRVAdapter.notifyDataSetChanged();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("Authorization","563492ad6f91700001000001a487858e4280415b84167f0fa2e4700d");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public int countFunc(JSONObject obj) {
        try {
            int totalpics = obj.getInt("total_results");
            int pics_per_page = obj.getInt("per_page");
            Log.d("tago",""+pics_per_page);
            int total_pages = (int) floor (totalpics/pics_per_page);
            return total_pages;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("tago","helo");
            Log.d("tago","bc");
            Log.d("tago","chutiya");
            return 0;
        }
    }
}