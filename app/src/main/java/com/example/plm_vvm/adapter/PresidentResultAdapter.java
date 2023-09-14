package com.example.plm_vvm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.plm_vvm.R;
import com.example.plm_vvm.model.Candidate;
import com.example.plm_vvm.model.President;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PresidentResultAdapter extends RecyclerView.Adapter<PresidentResultAdapter.ViewHolder>{

    private Context context;
    private List<President> list;
    private FirebaseFirestore firebaseFirestore;

    public PresidentResultAdapter(Context context, List<President> list) {
        this.context = context;
        this.list = list;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public PresidentResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.candidate_result_layout, parent, false);
        return new PresidentResultAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PresidentResultAdapter.ViewHolder holder,  @SuppressLint("RecyclerView") int position) {

        holder.name.setText(list.get(position).getName());
        holder.party.setText(list.get(position).getParty());
        holder.position.setText(list.get(position).getPosition());

        Glide.with(context).load(list.get(position).getImage()).into(holder.image);

        firebaseFirestore.collection("President/"+list.get(position).getId()+"/Vote")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {

                        if(!documentSnapshots.isEmpty())
                        {
                            int count = documentSnapshots.size();
                            President president = list.get(position);
                            president.setCount(count);
                            list.set(position, president);

                            notifyDataSetChanged();
                        }

                    }
                });

        holder.result.setText("Vote Count : " + list.get(position).getCount());


//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, VotingActivity.class);
//                intent.putExtra("name", list.get(position).getName());
//                intent.putExtra("party", list.get(position).getParty());
//                intent.putExtra("post", list.get(position).getPosition());
//                intent.putExtra("image", list.get(position).getImage());
//                intent.putExtra("id", list.get(position).getId());
//
//                context.startActivity(intent);
////                Activity activity = (Activity) context;
////                activity.finish();
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<President> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView image;
        private TextView name, position, party, result;
        //private Button Btnvote;
        //private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            position = itemView.findViewById(R.id.post);
            party = itemView.findViewById(R.id.party);
            result = itemView.findViewById(R.id.candidate_result);
            //Btnvote = itemView.findViewById(R.id.btn_vote);
            //cardView = itemView.findViewById(R.id.card_view);

        }
    }
}
