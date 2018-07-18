package com.example.palak.eddvissintegratedproject;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    Button imgBTN;
    @BindView(R.id.chapnameET)
    EditText chapnameET;
    @BindView(R.id.chapdescrptnET)
    EditText chapdescrptnET;
    @BindView(R.id.weeklyET)
    EditText wPriceET;
    @BindView(R.id.monthET)
    EditText mPriceET;
    @BindView(R.id.quaterlyET)
    EditText qPriceET;
    @BindView(R.id.halfET)
    EditText hPriceET;
    @BindView(R.id.yearET)
    EditText yPriceET;
    @BindView(R.id.updateBTN)
    Button updtChapBTN;
    String url = "http://sharkups.com:8080/eddviss/getChapterById";
    String url_used_for_updating = "http://sharkups.com:8080/eddviss/updateChapter";
    ChapterDetail chapterDetail;
    Chapter chapter;
    String chapterId = "33";
    String userId = "95";
    String key = "f82436ed9dd8f27a650d9b79e1ce071c";
    String email = "null";
    String checkSubscription = "false";
    public final String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        chapterDetail = new ChapterDetail();
        chapter = new Chapter();
        stringrequest();
        updateChapInfo();

    }

    private void updateChapInfo() {
        updtChapBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chapName = chapnameET.getText().toString().trim();
                String chapDescrptn = chapdescrptnET.getText().toString().trim();
                String wPrice = wPriceET.getText().toString().trim();
                String mPrice = mPriceET.getText().toString().trim();
                String qPrice = qPriceET.getText().toString().trim();
                String hPrice = hPriceET.getText().toString().trim();
                String yPrice = yPriceET.getText().toString().trim();

                if (isNullValuesInUpdate(chapName, chapDescrptn, wPrice, mPrice, qPrice, hPrice, yPrice)) {
                    //no empty fields after update
                    Toast.makeText(MainActivity.this, "Empty fields found in update...Try updating chapter details again!", Toast.LENGTH_LONG).show();
                } else {
                    setDetailsOfObject(chapName, chapDescrptn, wPrice, mPrice, qPrice, hPrice, yPrice);
                }

                sendDetailstoServer(chapName, chapDescrptn, wPrice, mPrice, qPrice, hPrice, yPrice);
            }
        });
    }

    private boolean isNullValuesInUpdate(String chapName, String chapDescrptn, String wPrice, String mPrice,
                                         String qPrice, String hPrice, String yPrice) {
        if (chapName.equals("") || chapDescrptn.equals("") || wPrice.equals("") || mPrice.equals("") ||
                qPrice.equals("")
                || hPrice.equals("") || yPrice.equals("")) {
            return true;
        }
        return false;
    }

    private void setDetailsOfObject(String chapName, String chapDescrptn, String wPrice, String mPrice, String qPrice, String hPrice, String yPrice) {
        chapter.setChapterName(chapName);
//        chapter.setChapterId(chapterId);
        chapter.setDescription(chapDescrptn);
        chapter.setWeeklyPrice(wPrice);
        chapter.setMonthlyPrice(mPrice);
        chapter.setQuaterlyPrice(qPrice);
        chapter.setHalfYearlyPrice(hPrice);
        chapter.setYearlyPrice(yPrice);
        return;
    }

    private void sendDetailstoServer(String chapName, String chapDescrptn, String wPrice, String mPrice, String qPrice, String hPrice, String yPrice) {


        UserCheck userCheck = new UserCheck();
        userCheck.setKey(key);
        userCheck.setUserId(userId);
        chapter.setUserCheck(userCheck);

        Gson gson = new GsonBuilder().create();
        String s = gson.toJson(chapter);

        try {
            JSONObject jsonObject = new JSONObject(s);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_used_for_updating,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "response while sending data to server is\n" + response);
                            Toast.makeText(MainActivity.this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Error occurred while updating...", Toast.LENGTH_LONG).show();
                        }
                    });
            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void stringrequest() {
        final ProgressDialog progressDialog = progressdialog();
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("Json", "Response is:\n" + response);
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();


                        chapterDetail = gson.fromJson(response.toString(), ChapterDetail.class);
                        chapter = chapterDetail.getResponseObject();
                        Log.e("stringRequest", "CHAPTER     --    " + chapterDetail);

                        String chapName = chapterDetail.getResponseObject().getChapterName();
                        Log.e("stringRequest", "chapterName   " + chapName);
                        String chapDescrptn = chapterDetail.getResponseObject().getDescription();
                        Log.e("stringRequest", "chapterDescrption   " + chapDescrptn);
                        String wPrice = chapterDetail.getResponseObject().getWeeklyPrice();
                        Log.e("stringRequest", "weeklyPrice   " + wPrice);
                        String mPrice = chapterDetail.getResponseObject().getMonthlyPrice();
                        Log.e("stringRequest", "monthlyPrice   " + mPrice);
                        String qPrice = chapterDetail.getResponseObject().getQuaterlyPrice();
                        Log.e("stringRequest", "quarterlyPrice   " + qPrice);
                        String hPrice = chapterDetail.getResponseObject().getHalfYearlyPrice();
                        Log.e("stringRequest", "half yearlyPrice   " + hPrice);
                        String yPrice = chapterDetail.getResponseObject().getYearlyPrice();
                        Log.e("stringRequest", "yearlyPrice   " + yPrice);
                        progressDialog.dismiss();

                        chapnameET.setText(chapName);
                        chapdescrptnET.setText(chapDescrptn);
                        wPriceET.setText(wPrice);
                        mPriceET.setText(mPrice);
                        qPriceET.setText(qPrice);
                        hPriceET.setText(hPrice);
                        yPriceET.setText(yPrice);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("json", "error");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("chapterId", chapterId);
                params.put("userId", userId);
                params.put("key", key);
                params.put("email", email);
                params.put("checkSubscription", checkSubscription);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    private ProgressDialog progressdialog() {
        ProgressDialog progressdialog = new ProgressDialog(getApplicationContext());
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();
        progressdialog.setCancelable(false);
        return progressdialog;
    }
}

