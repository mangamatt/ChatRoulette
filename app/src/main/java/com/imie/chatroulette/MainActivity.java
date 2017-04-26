package com.imie.chatroulette;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import static com.imie.chatroulette.R.id.textViewId;

public class MainActivity extends AppCompatActivity {

    private TextView textViewId;
    private TextView textViewTitle;
    private TextView textViewBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewId = (TextView) findViewById(R.id.textViewId);

        Button createuser = (Button) findViewById(R.id.createuser);
        createuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MessageActivity.class);
                EditText plainTextBody = (EditText) findViewById(R.id.editTextUser);
                String pseudo = plainTextBody.getText().toString();
                intent.putExtra("pseudo", pseudo);
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, "DISPLAY NAME = '" + pseudo + "'", null, null);
                if (cursor.moveToFirst()) {
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor phones = contentResolver.query(Phone.CONTENT_URI, null, Phone._ID, null, null);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private void getAndShowPost() {
        Uri constructionURI = Uri.parse("https://jsonplaceholder.typicode.com").buildUpon()
                .appendPath("posts")
                .appendPath("1")
                //.appendQueryParameter("id",String.valueOf(1))
                .build();
        try {
            URL urlFinal = new URL(constructionURI.toString());
            Toast.makeText(this.getBaseContext(), urlFinal.toString(), Toast.LENGTH_LONG).show();
            new AsyncTask<URL, Integer, String>() {
                @Override
                protected String doInBackground(URL... urls) {
                    if (urls.length > 0) {
                        try {
                            return getResponseFromHttpUrl(urls[0]);
                        } catch (IOException error) {
                            error.printStackTrace();
                        }
                    }
                    return null;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected void onPostExecute(String result) {
                    if (result != null && !result.equals("")) {
                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject postOne = new JSONObject(result);
                            Iterator<?> keys = postOne.keys();
                            while (keys.hasNext()) {
                                String key = (String) keys.next();
                                Log.i("MainActivity_KV", key + " : " + postOne.get(key));
                                switch (key) {
                                    case "userId":
                                        textViewId.setText(postOne.get(key).toString());
                                        break;
                                    case "body":
                                        textViewBody.setText(postOne.get(key).toString());
                                        break;
                                    case "title":
                                        textViewTitle.setText(postOne.get(key).toString());
                                        break;
                                }

                            }
                        } catch (JSONException jsonError) {
                            jsonError.printStackTrace();
                        }
                    }
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    super.onProgressUpdate(values);
                }
            }.execute(urlFinal);


        } catch (IOException error) {
            error.printStackTrace();
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }
}
