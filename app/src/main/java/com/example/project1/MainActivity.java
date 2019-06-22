package com.example.project1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project1.catAPI.CatResponse;
import com.example.project1.catAPI.CatService;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ImageView[] img = new ImageView[9];
    HashMap<Integer, Integer> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = new HashMap<>();
        img[0] = (ImageView) findViewById(R.id.img1);
        img[1] = (ImageView) findViewById(R.id.img2);
        img[2] = (ImageView) findViewById(R.id.img3);
        img[3] = (ImageView) findViewById(R.id.img4);
        img[4] = (ImageView) findViewById(R.id.img5);
        img[5] = (ImageView) findViewById(R.id.img6);
        img[6] = (ImageView) findViewById(R.id.img7);
        img[7] = (ImageView) findViewById(R.id.img8);
        img[8] = (ImageView) findViewById(R.id.img9);
        map.put(img[0].getId(), 0);
        map.put(img[1].getId(), 1);
        map.put(img[2].getId(), 2);
        map.put(img[3].getId(), 3);
        map.put(img[4].getId(), 4);
        map.put(img[5].getId(), 5);
        map.put(img[6].getId(), 6);
        map.put(img[7].getId(), 7);
        map.put(img[8].getId(), 8);

        for (int i = 0; i < 9; i++) {
            img[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickImage(view, view.getId());
                }
            });
        }
    }

    int index;

    public void onClickImage(View view, final int id) {

        index = id;
        String[] options = new String[4];
        options[0] = "Open Gallary";
        options[1] = "Camera";
        options[2] = "Download Photo Form Cat API";
        options[3] = "Clear The Image";

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Options");
        mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        mBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //camera
                if (i == 1) {
                    Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cInt, 1);

                    //galary
                } else if (i == 0) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    // Sets the type as image/*. This ensures only components of type image are selected
                    intent.setType("image/*");
                    //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                    String[] mimeTypes = {"image/jpeg", "image/png"};
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    // Launching the Intent
                    startActivityForResult(intent, 2);

                    //cat api
                } else if (i == 2) {
                    catAPI();
                } else if (i == 3){
                    int ii = map.get(id);
                    img[ii].setImageURI(null);
                }
            }
        });

        AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();
    }

    public void catAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CatService service = retrofit.create(CatService.class);

        service.get("json").enqueue(new Callback<List<CatResponse>>() {
            @Override
            public void onResponse(Call<List<CatResponse>> call, Response<List<CatResponse>> response) {
                if (!response.isSuccessful()) {
                    return;
                } else {
                    int i = map.get(index);
                    Picasso.with(getApplicationContext()).load(response.body().get(0).getUrl()).into(img[i]);

                }
            }
            @Override
            public void onFailure(Call<List<CatResponse>> call, Throwable t) {
                Log.d("RESPONCEFAIL", t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                int i = map.get(index);
                img[i].setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 2) {
            Uri selectedImage = data.getData();
            int i = map.get(index);
            img[i].setImageURI(selectedImage);
        }
    }
}


