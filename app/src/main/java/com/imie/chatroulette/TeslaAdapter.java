package com.imie.chatroulette;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imie.chatroulette.Message;
import com.imie.chatroulette.MessageActivity;
import com.imie.chatroulette.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class TeslaAdapter extends RecyclerView.Adapter<TeslaAdapter.MessageHolder> {
    private ArrayList<Message> listMessage = new ArrayList<>();

    public interface ListItemClickListener {
        void onListItemClick(String message, View view);
    }

    final private ListItemClickListener messageOnClickListener;

    public TeslaAdapter(ListItemClickListener listener) {
        messageOnClickListener = listener;
        SharedPreferences sp = MessageActivity.getSp();
        if (!sp.getAll().isEmpty()){
            Set<String> keys =  sp.getAll().keySet();
            for (String key : keys){
                Log.e("key",key);
                Log.e("value",sp.getString(key,""));
                String[] messageString = sp.getString(key,"").split(";");
                try{
                    listMessage.add(new Message(
                            messageString[0],
                            messageString[2],
                            messageString[1]));
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                notifyItemInserted(listMessage.size()-1);
            }
        }
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        Message message = listMessage.get(position);
        holder.bind(
                message.getMessage(),
                message.getPseudo(),
                message.getHour()
        );
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParent = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParent);

        return new MessageHolder(view);
    }

    @Override
    public int getItemCount() {
        return listMessage.size();
    }
    //TODO addToListe
    public int addToListe(Message message){
        listMessage.add(message);
        notifyItemInserted(listMessage.size() - 1);
        notifyDataSetChanged();
        SharedPreferences sp = MessageActivity.getSp();
        sp.edit().putString(String.valueOf(listMessage.indexOf(message)),
                message.getMessage()+";" +
                        message.getHour()+ ";"+
                        message.getPseudo()
        ).apply();

        return listMessage.indexOf(message);
    }

    public void clearListe(){
        listMessage.clear();
        notifyDataSetChanged();
    }

    class MessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemMessage;
        TextView itemPseudo;
        TextView itemHour;

        public MessageHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            itemMessage = (TextView) itemView.findViewById(R.id.textViewMsg);
            itemPseudo = (TextView) itemView.findViewById(R.id.textViewPseudo);
            itemHour = (TextView) itemView.findViewById(R.id.textViewDate);
        }

        void bind(String message, String pseudo, String hour) {
            itemMessage.setText(message);
            itemPseudo.setText(pseudo);
            itemHour.setText(hour);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Message message = listMessage.get(clickedPosition);
            String messageBody = message.getMessage();
            messageOnClickListener.onListItemClick(messageBody, view);
        }
    }
}