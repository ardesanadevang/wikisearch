package com.example.wikisearch.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.icu.util.UniversalTimeScale;
import android.os.Bundle;

import com.example.wikisearch.R;
import com.example.wikisearch.adapter.SearchListAdapter;
import com.example.wikisearch.model.WikiModel;
import com.example.wikisearch.util.Util;
import com.example.wikisearch.util.VolleyWebservices;
import com.example.wikisearch.util.WebServices_Url;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MenuItem searchMenuItem;
    RecyclerView searchList;

    VolleyWebservices volleyWebservices;
    Activity activity;

    Gson gson;
    List<WikiModel> responseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activity = MainActivity.this;
        searchList = (RecyclerView) findViewById(R.id.searchResult);
        gson = new Gson();

        volleyWebservices = new VolleyWebservices();

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            if(query.length()>0) {
                volleyWebservices.callWSmethodGET(activity, WebServices_Url.getSearchUrl(query), new VolleyWebservices.Callback() {
                    @Override
                    public void onSuccess(String responce, String TAG) {
                        try {
                            JSONObject obj = new JSONObject(responce);
                            if (obj.has("query")) {
                                JSONObject objQuery = obj.getJSONObject("query");
                                if (objQuery.has("pages")) {
                                    JSONArray array = objQuery.getJSONArray("pages");
                                    for (int i = 0; i < array.length(); i++) {
                                        WikiModel wikiModel = gson.fromJson(array.getJSONObject(i).toString(), WikiModel.class);
                                        responseList.add(wikiModel);
                                    }
                                    updateSearchList();
                                    return;
                                }
                            }
                            Util.showToast(activity, "Invalid data for parsing Response");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String message, String TAG) {
                        Util.showToast(activity, message);
                    }
                }, "");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    SearchListAdapter searchListAdapter;
    public void updateSearchList(){
        if(searchListAdapter == null){
            searchListAdapter = new SearchListAdapter(activity, responseList);
            searchList.setAdapter(searchListAdapter);
        } else {
            searchListAdapter.updateList(responseList);
            searchListAdapter.notifyDataSetChanged();
        }
    }
}
