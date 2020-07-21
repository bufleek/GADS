package com.fleek.books;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class apiUtil {
    private static final String QUERY_PARAMETER_KEY = "q";
    private static final String KEY = "key";
    private static final String API_KEY = "";

    private apiUtil(){}

    public static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes";

    public static URL buildUrl(String title){
        URL url = null;
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, title)
                .appendQueryParameter(KEY, API_KEY)
                .build();
        try {
            url = new URL(uri.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getJson(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            try {
                InputStream stream = connection.getInputStream();
                Scanner scanner = new Scanner(stream);
                scanner.useDelimiter("\\A");
                boolean hasData = scanner.hasNext();
                if (hasData) {
                    return scanner.next();
                } else {
                    return null;
                }
            }
            catch (Exception e){
                Log.d("Error", e.toString());
                return null;
            }
            finally {
                connection.disconnect();
            }
    }

    public static ArrayList<Book> getBooksFromJson(String json){
        final String ID = "id";
        final String TITLE = "title";
        final String SUBTITLE = "subTitle";
        final String AUTHORS = "authors";
        final String PUBLISHER = "publisher";
        final String PUBLISHED_DATE = "publishedDate";
        final String ITEMS = "items";
        final String VOLUME_INFO = "volumeInfo";
        final String DESCRIPTION = "description";
        final String IMAGE_LINKS = "imageLinks";
        final String THUMBNAIL = "thumbnail";
        ArrayList<Book> books = new ArrayList<Book>();

        try {
            JSONObject jsonBooks = new JSONObject(json);
            JSONArray arrayBooks = jsonBooks.getJSONArray(ITEMS);
            int numberOfBooks = arrayBooks.length();
            for(int i = 0; i < numberOfBooks; i++){
                JSONObject bookJson = arrayBooks.getJSONObject(i);
                JSONObject volumeInfoJson = bookJson.getJSONObject(VOLUME_INFO);
                JSONObject imageLinksJson = volumeInfoJson.getJSONObject(IMAGE_LINKS);
                int authorsNumber = volumeInfoJson.getJSONArray(AUTHORS).length();
                String[] authors = new String[authorsNumber];
                for(int j = 0; j < authorsNumber; j++){
                    authors[j] = volumeInfoJson.getJSONArray(AUTHORS).get(j).toString();
                }

                Book book = new Book(
                        bookJson.getString(ID),
                        volumeInfoJson.getString(TITLE),
                        volumeInfoJson.isNull(SUBTITLE)?"":volumeInfoJson.getString(SUBTITLE),
                        authors,
                        volumeInfoJson.getString(PUBLISHER),
                        volumeInfoJson.getString(PUBLISHED_DATE),
                        volumeInfoJson.getString(DESCRIPTION),
                        imageLinksJson.getString(THUMBNAIL));
                books.add(book);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return books;
    }
}