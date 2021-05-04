package com.example.artranslator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.PostHolder> {

    private ArrayList<String> originalTextList;
    private ArrayList<String> translateTextList;

    public RecylerAdapter(ArrayList<String> originalTextList, ArrayList<String> translateTextList) {
        this.originalTextList = originalTextList;
        this.translateTextList = translateTextList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row,parent,false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.originalText.setText(originalTextList.get(position));
        holder.translateText.setText(translateTextList.get(position));
    }

    @Override
    public int getItemCount() {
        return originalTextList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{
        TextView originalText;
        TextView translateText;

        public PostHolder(@NonNull View itemView){
            super(itemView);

            originalText = itemView.findViewById(R.id.recycler_row_original_text);
            translateText = itemView.findViewById(R.id.recycler_row_translate_text);
        }
    }
}
