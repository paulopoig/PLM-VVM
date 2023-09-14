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
import com.example.plm_vvm.activities.AlterActivity;
import com.example.plm_vvm.activities.EditCandidateActivity;
import com.example.plm_vvm.activities.VotingActivity;
import com.example.plm_vvm.model.Candidate;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditCandidateAdapter extends RecyclerView.Adapter<EditCandidateAdapter.Viewholder> {

    private Context context;
    private List<Candidate> list;

    public EditCandidateAdapter(Context context, List<Candidate> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EditCandidateAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.candidate_layout, parent, false);
        return new EditCandidateAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(list.get(position).getName());
        holder.party.setText(list.get(position).getParty());
        holder.position.setText(list.get(position).getPosition());

        Glide.with(context).load(list.get(position).getImage()).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Candidate candidate = list.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, AlterActivity.class); // ETO!
//                intent.putExtra("name", list.get(position).getName());
//                intent.putExtra("party", list.get(position).getParty());
//                intent.putExtra("post", list.get(position).getPosition());
                intent.putExtra("image", list.get(position).getImage());
//                intent.putExtra("id", list.get(position).getId());
                intent.putExtra("candidate", candidate);


                context.startActivity(intent);
//                Activity activity = (Activity) context;
//                activity.finish();

            }
        });

    }

    @Override
    public int getItemCount()  {
        return list.size();
    }

    public void filterList(ArrayList<Candidate> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }


    public class Viewholder extends RecyclerView.ViewHolder {

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
