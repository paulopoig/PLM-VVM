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
import com.example.plm_vvm.edit.EditPresidentActivity;
import com.example.plm_vvm.edit.EditVicePresidentActivity;
import com.example.plm_vvm.model.President;
import com.example.plm_vvm.model.VicePresident;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditVicePresidentAdapter extends RecyclerView.Adapter<EditVicePresidentAdapter.ViewHolder> {


    private Context context;
    private List<VicePresident> list;

    public EditVicePresidentAdapter(Context context, List<VicePresident> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EditVicePresidentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.candidate_layout, parent, false);
        return new EditVicePresidentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditVicePresidentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(list.get(position).getName());
        holder.party.setText(list.get(position).getParty());
        holder.position.setText(list.get(position).getPosition());

        Glide.with(context).load(list.get(position).getImage()).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VicePresident vicePresident = list.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, EditVicePresidentActivity.class); // ETO!
//                intent.putExtra("name", list.get(position).getName());
//                intent.putExtra("party", list.get(position).getParty());
//                intent.putExtra("post", list.get(position).getPosition());
                intent.putExtra("image", list.get(position).getImage());
//                intent.putExtra("id", list.get(position).getId());
                intent.putExtra("vicePresident", vicePresident);


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

    public void filterList(ArrayList<VicePresident> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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
