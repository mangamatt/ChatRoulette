package com.imie.chatroulette;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.preference.PreferenceManager;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.Toolbar;
        import android.text.format.DateFormat;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.EditText;

        import java.util.Date;

public class MessageActivity extends AppCompatActivity implements TeslaAdapter.ListItemClickListener {
    private RecyclerView messageList;
    public static SharedPreferences sp;
    private EditText editTextMessage;
    private TeslaAdapter messageAdapter;
    private String pseudo;

    public static SharedPreferences getSp() {
        return sp;
    }
    public static void setSp(SharedPreferences sp) {
        MessageActivity.sp = sp;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        sp = getSharedPreferences("listeMessage",MODE_PRIVATE);
        messageAdapter = new TeslaAdapter(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        setSupportActionBar(toolbar);

        pseudo = getIntent().getExtras().getString("pseudo");

        messageList = (RecyclerView) findViewById(R.id.RecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        messageList.setLayoutManager(linearLayoutManager);
        messageList.setAdapter(messageAdapter);

        /*TextView textViewPrenom = (TextView) findViewById(R.id.textViewPrenom);
        textViewPrenom.setText();*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // add send message here
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO(3) ajouter notre message à la liste
                //TODO (4) afficher un format correct de date
                //TODO (5) go to bottom of the list
                messageAdapter.addToListe(new Message(
                        pseudo, editTextMessage.getText().toString(),
                        DateFormat.format("dd-MM hh:mm a", new Date()).toString()
                ));
                //TODO(1) Clear l'editText quand j'envoie
                editTextMessage.setText("");
                //TODO(2) Close Keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                editTextMessage.clearFocus();
                messageList.scrollToPosition(messageAdapter.getItemCount()-1);
            }
        });

        messageList.scrollToPosition(messageAdapter.getItemCount()-1);

    }

    @Override
    public void onListItemClick(String message, View view) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.menu_clear_sp){
//            getSp().edit().clear().apply();
//            messageAdapter.clearListe();
//        }
//        return true;
//    }
}