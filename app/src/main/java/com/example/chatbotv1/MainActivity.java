package com.example.chatbotv1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chatbotv1.API.ChatterBot;
import com.example.chatbotv1.API.ChatterBotFactory;
import com.example.chatbotv1.API.ChatterBotSession;
import com.example.chatbotv1.API.ChatterBotType;
import com.example.chatbotv1.API.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final boolean OG_USER = true;
    private RecyclerView rvMensajes;
    private Button btEnviar;
    private EditText etMensaje;
    private String str = "", translation ="";
    private MensajeAdapter adapter;
    private List<Mensaje> mensajes = new ArrayList<>();
    private Mensaje mensajeAux;

    private ChatterBot bot;
    private ChatterBotSession session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initEvents();

    }

    private void initEvents() {

        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = etMensaje.getText().toString();
                TranslateToEng translate = new TranslateToEng(str);
                mensajeAux = new Mensaje(str, OG_USER);
                mensajes.add(mensajeAux);
                adapter.setMensajeList(mensajes);
                adapter.notifyDataSetChanged();
                etMensaje.setText("");
                translate.execute();
            }
        });
    }

    private void initComponents() {
        rvMensajes = findViewById(R.id.rvMensajes);
        btEnviar = findViewById(R.id.btEnviar);
        etMensaje = findViewById(R.id.etMensaje);

        ChatterBotFactory factory = new ChatterBotFactory();

        adapter = new MensajeAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(manager);
        rvMensajes.setAdapter(adapter);


        try{
            bot = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
        }catch (Exception e) {
            e.printStackTrace();
        }

        session = bot.createSession();
    }


    private void showBotResponse(){
        adapter.mensajes.add(new Mensaje(translation, false));
        adapter.notifyDataSetChanged();

    }




    private class Chat extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            chat(translation);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }
    }

    private class TranslateToEng extends AsyncTask<Void, Void, Void>{

        private final Map<String, String> headers;
        private final Map<String, String> vars;
        String s = "Error";

        private TranslateToEng(String message) {
            headers = new LinkedHashMap<String, String>();
            headers.put("Content-type","application/x-www-form-urlencoded");
            headers.put("User-Agent:","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");

            vars = new HashMap<String, String>();
            vars.put("fromLang", "es");
            vars.put("text",message);
            vars.put("to","en");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                s = Utils.performPostCall("https://www.bing.com/ttranslatev3", (HashMap) vars);
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("xyz", "Error: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            translation = decomposeJson(s);
            doTheChat();
        }
    }



    private void chat(String msg) {
        try {
            String response = session.think(msg);
            new TranslateToEs(response).execute();
        }catch(Exception e){
            Log.v("xyz", "Error: " + e.getMessage());
        }
    }




    public String decomposeJson(String json){
        String translationResult = "Could not get";
        try {
            JSONArray arr = new JSONArray(json);
            JSONObject jObj = arr.getJSONObject(0);
            translationResult = jObj.getString("translations");
            JSONArray arr2 = new JSONArray(translationResult);
            JSONObject jObj2 = arr2.getJSONObject(0);
            translationResult = jObj2.getString("text");
        } catch (JSONException e) {
            translationResult = e.getLocalizedMessage();
        }
        return translationResult;
    }




    private class TranslateToEs extends AsyncTask<Void, Void, Void>{

        private final Map<String, String> headers;
        private final Map<String, String> vars;
        String s = "Error";

        private TranslateToEs(String message) {
            headers = new LinkedHashMap<String, String>();
            headers.put("Content-type","application/x-www-form-urlencoded");
            headers.put("User-Agent:","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");

            vars = new HashMap<String, String>();
            vars.put("fromLang", "en");
            vars.put("text",message);
            vars.put("to","es");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                s = Utils.performPostCall("https://www.bing.com/ttranslatev3", (HashMap) vars);
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("xyz", "Error: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            translation = decomposeJson(s);
            showBotResponse();

        }
    }




    public void doTheChat(){
        new Chat().execute();
    }
}
