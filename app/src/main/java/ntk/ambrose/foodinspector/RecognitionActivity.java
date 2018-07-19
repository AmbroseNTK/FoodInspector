package ntk.ambrose.foodinspector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ntk.ambrose.foodinspector.recognizer.Result;
import ntk.ambrose.foodinspector.sampleview.AdapterSampleView;

public class RecognitionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterSampleView adapterSampleView;
    List<Bitmap> samples;
    int currentPos=0;
    TextView textResult;

    Button btAgain;
    FirebaseDatabase database;

    public static ArrayList<Result> results;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recognition_activity);

        database = FirebaseDatabase.getInstance();
        textResult = findViewById(R.id.textResult);
        btAgain = findViewById(R.id.btAgain);
        btAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPos++;
                viewItem(currentPos);
            }
        });

        if(results!=null && results.size()!=0) {

            recyclerView = findViewById(R.id.recyclerSampleView);
            adapterSampleView = new AdapterSampleView(samples);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapterSampleView);
            viewItem(currentPos);
        }
        else{
            finish();
        }

    }
    public void loadSample(String keyword){
        samples = new ArrayList<>();
        ArrayList<String> links = new ArrayList<>();
        int i=1;
        while(true) {
            database.getReference(keyword + "/images/i"+String.valueOf(i)).getKey();

        }
    }
    private void viewItem(int id){
        if(id < results.size()){
            textResult.setText(results.get(id).getResult()+" - confidence: "+results.get(id).getConfidence());
            loadSample(results.get(id).getResult());

        }
    }

    private class DownloadImage extends AsyncTask<ArrayList<String>,Void,ArrayList<Bitmap>> {

        ArrayList<Bitmap> bitmaps;

        public DownloadImage() {
            super();
        }

        @Override
        protected ArrayList<Bitmap> doInBackground(ArrayList<String>... arrayLists) {
            try{
                bitmaps = new ArrayList<>();
                for(int i=0;i<arrayLists.length;i++){
                    bitmaps.add(BitmapFactory.decodeStream(new URL(arrayLists[0].get(i)).openConnection().getInputStream()));
                }
            }
            catch(IOException ex){
                return null;
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
            super.onPostExecute(bitmaps);
            samples = bitmaps;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }



        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
