package com.example.manjunath.customizedinputform;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.manjunath.customizedinputform.adapter.RecyclerViewAdapter;
import com.example.manjunath.customizedinputform.entity.InputData;
import com.example.manjunath.customizedinputform.entity.InputEntity;
import com.example.manjunath.customizedinputform.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ArrayList<InputEntity>> inputEntityList;
    private RecyclerView inputFormRecyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        loadInputJsonData();
        inputFormRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setAdapterToRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.submit) {
            logResults();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAdapterToRecyclerView() {
        adapter = new RecyclerViewAdapter(this, inputEntityList, inputFormRecyclerView);
        inputFormRecyclerView.setAdapter(adapter);
    }

    private void initializeViews() {
        inputFormRecyclerView = findViewById(R.id.inputFormRecyclerView);
    }

    private void loadInputJsonData() {
        try {
            JSONObject jsonObj = new JSONObject(Utils.loadJSONFromAsset(this));
            InputData inputData = new Gson().fromJson(String.valueOf(jsonObj), InputData.class);
            inputEntityList = inputData.getInputEntities();
            Log.d("loadInputJsonData", inputEntityList.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data != null && data.getExtras().get("data") != null) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                adapter.setImageBitmap(image, requestCode);
                File finalFile = null;
                Uri uri = getImageUri(getApplicationContext(),image);
                if (uri != null) {
                    finalFile = new File(getRealPathFromURI(uri));
                    adapter.data.get(requestCode).get(0).setTittle(finalFile.getAbsolutePath());
                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        if (path != null) {
            return Uri.parse(path);
        }
        else return null;
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void logResults() {
        String result = "";
        for (int i=0; i<adapter.getItemCount(); i++) {
            result+= "\n ItemList " + (i+1) + " ---> ";
            result+= adapter.data.get(i).get(0).getId() + " = " + adapter.data.get(i).get(0).getTittle() + ",";
            result+= adapter.data.get(i).get(1).getId() + " = " + adapter.data.get(i).get(1).getTittle() + ",";
            result+= adapter.data.get(i).get(2).getId() + " = " + adapter.data.get(i).get(2).getTittle() +  ",";
        }
        Log.d("Submitted values" , result);
        Toast.makeText(getBaseContext(),"Submitted Successfully",Toast.LENGTH_LONG).show();
        //setAdapterToRecyclerView(); To reset all the values
    }
}
