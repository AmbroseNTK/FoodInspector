package ntk.ambrose.foodinspector.sampleview;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ntk.ambrose.foodinspector.R;

public class AdapterSampleView extends RecyclerView.Adapter<RecyclerSampleViewHolder>{
    private List<Bitmap> listData = new ArrayList<>();

    public AdapterSampleView(List<Bitmap> listData){
        this.listData = listData;
    }
    @Override
    public RecyclerSampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.image_item,parent,false);
        return new RecyclerSampleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerSampleViewHolder holder, int position) {
        holder.imgSample.setImageBitmap(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}