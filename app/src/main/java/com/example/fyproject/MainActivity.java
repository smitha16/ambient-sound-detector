package com.example.fyproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //String url = "http://10.0.2.2:5000/predict";
    String url = "https://smiths.pythonanywhere.com/predict";
    LinearLayout ll;
    Handler hlr  = new Handler();
    Runnable run;
    HashMap<Integer, String> img = new HashMap<Integer, String>();
    int d;
    HashMap<Integer, String> hm = new HashMap<Integer, String>();
    int[] ar = new int[10];
    RadioGroup rg;
    CameraManager camMgr;
    String camID;
    Vibrator v;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ll = findViewById(R.id.container);

        hm.put(0, "Baby Crying");
        hm.put(1, "Dog Barking");
        hm.put(2, "Coughing");
        hm.put(3, "Dishes/Pots");
        hm.put(4, "Doorbell");
        hm.put(5, "Fire Alarm");
        hm.put(6, "Smoke Detector");
        hm.put(7, "Sneeze");
        hm.put(8, "Thunder");
        hm.put(9, "Water");

        img.put(0, "baby");
        img.put(1, "dog");
        img.put(2, "cough");
        img.put(3, "dishes");
        img.put(4, "doorbell");
        img.put(5, "fire");
        img.put(6, "smoke");
        img.put(7, "sneeze");
        img.put(8, "thunder");
        img.put(9, "water");

        for(int i = 0; i<10; i++){
            ar[i] = 0;
        }

        rg = (RadioGroup) findViewById(R.id.rdg);
        camMgr = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {

            camID = camMgr.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    private void addSound(String name, String radio) {
        Toast.makeText(MainActivity.this, "New Sound Detected", Toast.LENGTH_SHORT).show();

        final View v = getLayoutInflater().inflate(R.layout.card, null);

        TextView card = v.findViewById(R.id.name);
        Button discard = v.findViewById(R.id.delete);
        ImageView im = v.findViewById(R.id.imageView);
        card.setText(hm.get(Integer.valueOf(name)));


        String s = "@drawable/" + img.get(Integer.parseInt(name));;
        int src = getResources().getIdentifier(s, null, getPackageName());
        Drawable r = getResources().getDrawable(src);
        im.setImageDrawable(r);

        ll.addView(v);

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                String temp = (String) card.getText();
                //Toast.makeText(MainActivity.this, temp, Toast.LENGTH_SHORT).show();
                for(int i = 0; i<10; i++){
                    if(hm.get(i) == temp){
                        ar[i] = 0;
                        break;
                    }
                }
                ll.removeView(v);
            }
        });
    }

    protected void onResume () {
            hlr.postDelayed(run = new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                public void run() {
                    d = 8000;
                    hlr.postDelayed(run, d);
                    try{
                        camMgr.setTorchMode(camID, false);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    StringRequest req = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String data = jsonObject.getString("result");
                                        RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());

                                        if (!data.equals("None") && !data.equals("Firebase Empty") && ar[Integer.parseInt(data)] == 0) {

                                            ar[Integer.parseInt(data)] = 1;
                                            if(rb.getText().equals("Vibration")) {
                                                v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    v.vibrate(VibrationEffect.createOneShot(1600, VibrationEffect.DEFAULT_AMPLITUDE));
                                                }
                                                addSound(data, (String) rb.getText());
                                            }
                                            else{
                                                try {
                                                    camMgr.setTorchMode(camID, true);
                                                    addSound(data, (String) rb.getText());

                                                } catch (CameraAccessException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                        } else{

                                            //handler.postDelayed(runnable, 200);
                                            //Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }) {

                    };
                    RequestQueue que = Volley.newRequestQueue(MainActivity.this);
                    que.add(req);

                }
            }, d);
            super.onResume();
    }
    @Override
    protected void onPause () {
            super.onPause();
            hlr.removeCallbacks(run);
    }
}