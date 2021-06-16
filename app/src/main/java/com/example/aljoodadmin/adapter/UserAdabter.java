package com.example.aljoodadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aljoodadmin.R;
import com.example.aljoodadmin.model.User;
import com.example.aljoodadmin.view.AddUser;
import com.example.aljoodadmin.view.Chat;

import java.util.ArrayList;

public class UserAdabter extends RecyclerView.Adapter<UserAdabter.ViewHolder> {
    private Context context;
    private ArrayList<User> userArrayList;

    public UserAdabter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;

    }
    public interface OnItemClickListener {
        void onItemClicked(String firebaseId);

    }

    private UserAdabter.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(UserAdabter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }
    @NonNull
    @Override
    public UserAdabter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new UserAdabter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull UserAdabter.ViewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,email,account,points;
        CardView cardView;
        LinearLayout contact;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            account = itemView.findViewById(R.id.account_txt);
            cardView = itemView.findViewById(R.id.cv_login);
            points=itemView.findViewById(R.id.points_txt);
contact=itemView.findViewById(R.id.contact);
        }
        void  bind(final User user) {
            name.setText(user.getName());
            email.setText(user.getEmail());
            account.setText(user.getAccount()+" ش");
            points.setText(user.getPoints()+" نقطة");
cardView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        onItemClickListener.onItemClicked(user.getFirebaseId());

    }
});
contact.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        context.startActivity(new Intent(context, Chat.class)
                .putExtra("name",user.getName())
                .putExtra("id",user.getFirebaseId())
        .putExtra("token",user.getTokenId())
                .putExtra("points",user.getPoints())
        );
    }
});
        }
        }
}
