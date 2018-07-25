package ntk.ambrose.foodinspector;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ntk.ambrose.foodinspector.recognizer.Result;
import ntk.ambrose.foodinspector.sampleview.AdapterSampleView;

public class RecognitionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterSampleView adapterSampleView;
    List<Bitmap> samples;
    int currentPos=0;
    TextView textResult;

    Button btAgain;
    Button btOK;
    FirebaseDatabase database;

    ExpandableLayout expandableLayout;

    public static ArrayList<Result> results;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recognition_activity);

        database = FirebaseDatabase.getInstance();
        samples=new ArrayList<>();
        textResult = findViewById(R.id.textResult);
        expandableLayout = findViewById(R.id.expandableLayout);

        btAgain = findViewById(R.id.btAgain);
        btAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPos++;
                viewItem(currentPos);
            }
        });

        btOK = findViewById(R.id.btOK);
        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout.toggle();
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
        samples.clear();

        DatabaseReference ref = database.getReference(keyword+"/images/");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("FoodInspector",dataSnapshot.getValue().toString());

                try {
                    Bitmap result = new DownloadImage().execute(dataSnapshot.getValue().toString()).get();
                    if(result!=null){
                        samples.add(result);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Log.i("FoodInspector","Finished");

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void viewItem(int id){
        if(id < results.size()){
            textResult.setText(results.get(id).getResult()+" - confidence: "+results.get(id).getConfidence());
            loadSample(results.get(id).getResult());
            expandableLayout.expand();
        }
    }

    private class DownloadImage extends AsyncTask<String,Void,Bitmap> {
        public DownloadImage() {
            super();
        }

        @Override
        protected Bitmap doInBackground(String... arrayLists) {

            Bitmap bitmap = null;
            try {
                if(!arrayLists[0].equals("")) {

                    bitmap = Picasso.get().load(arrayLists[0]).get();
                }
            } catch (IOException ex) {
                Log.i("FoodInspector", "Cannot load image");
            }
            return bitmap;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

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
