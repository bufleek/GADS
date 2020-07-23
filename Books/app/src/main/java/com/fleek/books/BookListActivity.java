package com.fleek.books;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ProgressBar mLoadingProgress;
    private RecyclerView rvBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        mLoadingProgress = (ProgressBar) findViewById(R.id.pbLoading);
        rvBooks = (RecyclerView) findViewById(R.id.rv_books);
        LinearLayoutManager booksLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvBooks.setLayoutManager(booksLayoutManager);

        Intent intent = getIntent();
        String query = intent.getStringExtra("Query");

        URL booksUrl;
        try {
            if(query == null || query.isEmpty()){
                booksUrl = apiUtil.buildUrl("cooking");
            }else{
                booksUrl = new URL(query);
            }
            String jsonResult = apiUtil.getJson(booksUrl);
            new BooksQueryTask().execute(booksUrl);
        }
        catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.item_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        ArrayList<String> recentList = SpUtil.getQueryList(getApplicationContext());
        int recentListNum = recentList.size();
        MenuItem recentMenu;
        for(int i=0; i<recentListNum; i++){
            recentMenu = menu.add(Menu.NONE, i, Menu.NONE, recentList.get(i));
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            URL url = apiUtil.buildUrl(query);
            new BooksQueryTask().execute(url);
        }catch (Exception e){
            Log.d("error", e.getMessage());
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String result = null;
            try {
                result = apiUtil.getJson(searchUrl);
            }
            catch (IOException e){
                Log.d("Error", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView tvError = (TextView) findViewById(R.id.tv_error);
            if(result == null){
                rvBooks.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.VISIBLE);
            }else {
                rvBooks.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.INVISIBLE);
                ArrayList<Book> books = apiUtil.getBooksFromJson(result);
                BooksAdapter booksAdapter = new BooksAdapter(books);
                rvBooks.setAdapter(booksAdapter);
                mLoadingProgress.setVisibility(View.INVISIBLE);
                mLoadingProgress.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected void onPreExecute() {
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_advanced_search:
                startActivity(new Intent(getApplicationContext(), searchActivity.class));
                return true;

            default:
                int position = item.getItemId() + 1;
                String preferenceName = SpUtil.QUERY + String.valueOf(position);
                String query = SpUtil.getPreferenceString(getApplicationContext(), preferenceName);
                String[] prefParams = query.split("\\,");
                String[] queryParams = new String[4];
                for(int i = 0; i < prefParams.length; i++){
                    queryParams[i] = prefParams[i];
                }
                URL bookUrl = apiUtil.buildUrl(
                        (queryParams[0] == null)?"" : queryParams[0],
                        (queryParams[1] == null)?"" : queryParams[1],
                        (queryParams[2] == null)?"" : queryParams[2],
                        (queryParams[3] == null)?"" : queryParams[3]
                );
                new BooksQueryTask().execute(bookUrl);

            return super.onOptionsItemSelected(item);
        }
    }
}