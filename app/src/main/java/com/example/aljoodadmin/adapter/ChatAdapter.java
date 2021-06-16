package com.example.aljoodadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aljoodadmin.R;
import com.example.aljoodadmin.model.Msg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Msg> msgArrayList;
    public ChatAdapter(ArrayList<Msg> msgArrayList, Context context) {
        this.context = context;
        this.msgArrayList = msgArrayList;



    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(context).inflate(R.layout.massege_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.setIsRecyclable(false);
            Msg message=msgArrayList.get(position);

            holder.bind(message);
        }catch (IndexOutOfBoundsException v){

        }
    }

    @Override
    public int getItemCount() {
        return msgArrayList.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMsgUser,  txtMsgAdmin, timeUser, timeAdmin;
        public ViewHolder(View inflate) {
            super(inflate);
            txtMsgUser = itemView.findViewById(R.id.txt_msg);
            txtMsgAdmin = itemView.findViewById(R.id.txt_msg_admin);
            timeUser = itemView.findViewById(R.id.txt_time);
            timeAdmin = itemView.findViewById(R.id.txt_time_admin);

        }
        void bind(Msg message) {
if(message.getMsgfrom()==1){
    txtMsgAdmin.setVisibility(View.GONE);
    timeAdmin.setVisibility(View.GONE);
setMsgAndTime(message.getMessage(),message.getTime(),txtMsgUser,timeUser);
}else if(message.getMsgfrom()==2){
    txtMsgUser.setVisibility(View.GONE);
    timeUser.setVisibility(View.GONE);
    setMsgAndTime(message.getMessage(),message.getTime(),txtMsgAdmin,timeAdmin);

}
        }

        private void setMsgAndTime(String message, Date time, TextView txtMsg, TextView txtTime) {
txtMsg.setText(message);
txtTime.setText(getCurrentDate(time));
        }
    }
    private String getCurrentDate(Date time) {
        DateFormat df = new SimpleDateFormat("h:mm a ,d MMM", Locale.ENGLISH);
        return df.format(time);

    }
}
