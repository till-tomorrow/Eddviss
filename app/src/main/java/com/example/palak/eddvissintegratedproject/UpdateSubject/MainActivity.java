package com.example.palak.eddvissintegratedproject.UpdateSubject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.palak.eddvissintegratedproject.AppController;
import com.example.palak.eddvissintegratedproject.R;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getName();
    @BindView(R.id.imgIV)
    ImageView imgIV;
    @BindView(R.id.chooseimgBTN)
    Button imgBTN;
    @BindView(R.id.uploadimgBTN)
    Button uploadimgBTN;
    @BindView(R.id.iconIV)
    ImageView iconIV;
    @BindView(R.id.chooseiconBTN)
    Button iconBTN;
    @BindView(R.id.uploadiconBTN)
    Button uploadiconBTN;
    @BindView(R.id.subnameET)
    EditText subnameET;
    @BindView(R.id.subdescrptnET)
    EditText subdescrptnET;
    @BindView(R.id.weeklyET)
    EditText wPrice;
    @BindView(R.id.monthET)
    EditText mPrice;
    @BindView(R.id.quaterlyET)
    EditText qPrice;
    @BindView(R.id.halfET)
    EditText hPrice;
    @BindView(R.id.yearET)
    EditText yPrice;

    static final int PICK_IMAGE_REQUEST = 1;
    static final int PICK_ICON_REQUEST = 2;
    private int quality = 50;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_update);
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        imgBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if everything is ok we will open image chooser
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE_REQUEST);
            }
        });

        /*uploadimgBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/

        iconBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_ICON_REQUEST);
            }
        });

        /*uploadiconBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/

    }

    private ProgressDialog progressdialog() {
        ProgressDialog progressdialog = new ProgressDialog(getApplicationContext());
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();
        progressdialog.setCancelable(false);
        return progressdialog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            ProgressDialog progressDialog = progressdialog();

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                Log.e(TAG, "image chosen from gallary");
                progressDialog.dismiss();

                //displaying selected image to imageview
                imgIV.setImageBitmap(bitmap);

                //calling the method uploadBitmap to upload image
                uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == PICK_ICON_REQUEST && resultCode == RESULT_OK && data != null){
            ProgressDialog progressDialog = progressdialog();

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                Log.e(TAG, "image chosen from gallary");
                progressDialog.dismiss();

                //displaying selected image to imageview
                iconIV.setImageBitmap(bitmap);

                //calling the method uploadBitmap to upload image
                uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadBitmap(final Bitmap bitmap) {
        final ProgressDialog progressDialog = progressdialog();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
        byte[] data = bos.toByteArray();
        InputStream in = new ByteArrayInputStream(data);
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.addPart("file", new InputStreamBody(in, "image/jpeg"));
        entityBuilder.addTextBody("extension", "jpeg");
        entityBuilder.addTextBody("fileName", "abc");
        entityBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        PhotoMultipartRequest photoMultipartRequest = new PhotoMultipartRequest(EndPoints.UPLOAD_URL, entityBuilder, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error is " + error);
                Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_LONG).show();
            }
        }, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.e(TAG, "Response is " + response);
            }
        });
        photoMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(100000000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(photoMultipartRequest);
    }
}
