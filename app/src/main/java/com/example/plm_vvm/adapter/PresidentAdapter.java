package com.example.plm_vvm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.plm_vvm.R;
import com.example.plm_vvm.activities.VotingActivity;
import com.example.plm_vvm.activities.VotingPresidentActivity;
import com.example.plm_vvm.model.Candidate;
import com.example.plm_vvm.model.President;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PresidentAdapter extends RecyclerView.Adapter<PresidentAdapter.Viewholder> {

    private Context context;
    private List<President> list;

    public PresidentAdapter(Context context, List<President> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PresidentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.candidate_layout, parent, false);
        return new PresidentAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PresidentAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(list.get(position).getName());
        holder.party.setText(list.get(position).getParty());
        holder.position.setText(list.get(position).getPosition());

        Glide.with(context).load(list.get(position).getImage()).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VotingPresidentActivity.class); // ETO!
                intent.putExtra("name", list.get(position).getName());
                intent.putExtra("party", list.get(position).getParty());
                intent.putExtra("position", list.get(position).getPosition());
                intent.putExtra("image", list.get(position).getImage());
                intent.putExtra("id", list.get(position).getId());

                context.startActivity(intent);
//                Activity activity = (Activity) context;
//                activity.finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<President> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }


    public class Viewholder extends  RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView name, position, party;
        //private Button Btnvote;
        private CardView cardView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            position = itemView.findViewById(R.id.post);
            party = itemView.findViewById(R.id.party);
            //Btnvote = itemView.findViewById(R.id.btn_vote);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
