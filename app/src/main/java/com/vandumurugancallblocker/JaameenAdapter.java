package com.vandumurugancallblocker;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vandumurugancallblocker.room.AgaraathiPudichchavan;

import java.util.List;

public class JaameenAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private Context context;
    private List<AgaraathiPudichchavan> agaraathiPudichchavans;
    private ItemClickListener itemClickListener;


    public JaameenAdapter(Context context, List<AgaraathiPudichchavan> agaraathiPudichchavans) {
        this.context = context;
        this.agaraathiPudichchavans = agaraathiPudichchavans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return (new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_blocked,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder)holder;
        final AgaraathiPudichchavan agaraathiPudichchavan = getCurrentItem(position);
        if(agaraathiPudichchavan.getNote()!=null && !"".equals(agaraathiPudichchavan.getNote().trim())){
            myViewHolder.noteTv.setText(agaraathiPudichchavan.getNote());
        }else{
            myViewHolder.noteTv.setVisibility(View.GONE);
        }
        String contentHtml=context.getString(R.string.incoming_call)+" <b>"+VakkeelUtils.getConditionString(context,agaraathiPudichchavan)+"</b>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            myViewHolder.contentTv.setText(Html.fromHtml(contentHtml, Html.FROM_HTML_MODE_COMPACT));
        } else {
            myViewHolder.contentTv.setText(Html.fromHtml(contentHtml));
        }

        if(itemClickListener!=null){
            myViewHolder.deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onDeleteClick(agaraathiPudichchavan);
                }
            });
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onListItemClick(agaraathiPudichchavan);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return agaraathiPudichchavans.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private AgaraathiPudichchavan getCurrentItem(int position){
        return agaraathiPudichchavans.get(position);
    }
    private class MyViewHolder extends RecyclerView.ViewHolder{
        TextView contentTv,noteTv;
        ImageView deleteIcon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTv=itemView.findViewById(R.id.conditionTv);
            noteTv=itemView.findViewById(R.id.noteTv);
            deleteIcon=itemView.findViewById(R.id.deleteIcon);
        }
    }
    public interface ItemClickListener{
        void onDeleteClick(AgaraathiPudichchavan agaraathiPudichchavan);
        void onListItemClick(AgaraathiPudichchavan agaraathiPudichchavan);
    }
}
