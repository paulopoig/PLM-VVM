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
import com.example.plm_vvm.edit.EditSecretaryActivity;
import com.example.plm_vvm.edit.EditTreasurerActivity;
import com.example.plm_vvm.model.Secretary;
import com.example.plm_vvm.model.Treasurer;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditTreasurerAdapter extends RecyclerView.Adapter<EditTreasurerAdapter.ViewHolder> {

    private Context context;
    private List<Treasurer> list;

    public EditTreasurerAdapter(Context context, List<Treasurer> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EditTreasurerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.candidate_layout, parent, false);
        return new EditTreasurerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditTreasurerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(list.get(position).getName());
        holder.party.setText(list.get(position).getParty());
        holder.position.setText(list.get(position).getPosition());

        Glide.with(context).load(list.get(position).getImage()).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Treasurer treasurer = list.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, EditTreasurerActivity.class); // ETO!
//                intent.putExtra("name", list.get(position).getName());
//                intent.putExtra("party", list.get(position).getParty());
//                intent.putExtra("post", list.get(position).getPosition());
                intent.putExtra("image", list.get(position).getImage());
//                intent.putExtra("id", list.get(position).getId());
                intent.putExtra("treasurer", treasurer);


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

    public void filterList(ArrayList<Treasurer> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView image;
        private TextView name, position, party;
        //private Button Btnvote;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
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
