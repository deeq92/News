package com.example.diazapps.news;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<NewsItem> arrayList;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView news_list = (ListView) findViewById(R.id.news_list);

        arrayList = new ArrayList<>();
        arrayAdapter = new CustomAdapter(this, arrayList);
        news_list.setAdapter(arrayAdapter);

        execute(getString(R.string.ap));

//        RelativeLayout footerLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.listview_button, null);
//        news_list.addFooterView(footerLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.ap:
                    execute(getString(R.string.ap));
                    return true;
                case R.id.espn:
                    execute(getString(R.string.espn));
                    return true;
                case R.id.cnn:
                    execute(getString(R.string.cnn));
                    return true;
                case R.id.google:
                    execute(getString(R.string.google));
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } catch(Exception e){
        }
        return false;
    }

    public void execute(String url)
    {
        arrayList.clear();
        arrayAdapter.notifyDataSetChanged();
        DownloadTask download = new DownloadTask();
        download.execute(url);
    }


    public class DownloadTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1)
                {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject jsonObject = new JSONObject(result);
                String newsInfo = jsonObject.getString("articles");

                JSONArray jsonArray = new JSONArray(newsInfo);

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonPart = jsonArray.getJSONObject(i);

                    NewsItem tempnews = new NewsItem();
                    tempnews.setTitle(jsonPart.getString("title"));
                    tempnews.setDesc(jsonPart.getString("description"));
                    tempnews.setUrl(jsonPart.getString("url"));
                    tempnews.setPic_url(jsonPart.getString("urlToImage"));

                    arrayList.add(i, tempnews);

                    arrayAdapter.notifyDataSetChanged();
                }
                //Toast.makeText(getApplicationContext(), "Refreshed!", Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
