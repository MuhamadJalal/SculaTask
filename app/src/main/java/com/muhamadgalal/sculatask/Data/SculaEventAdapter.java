package com.muhamadgalal.sculatask.Data;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.muhamadgalal.sculatask.Model.SculaEvent;
import com.muhamadgalal.sculatask.R;

import java.util.List;

public class SculaEventAdapter extends RecyclerView.Adapter<SculaEventAdapter.ViewHolder> {

    private Context context;
    private List<SculaEvent> sculaEventList;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    public SculaEventAdapter(Context context, List<SculaEvent> sculaEventList) {
        this.context = context;
        this.sculaEventList = sculaEventList;
    }

    @NonNull
    @Override
    public SculaEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_model ,parent ,false);

        return new ViewHolder(context , view);
    }

    @Override
    public void onBindViewHolder(@NonNull SculaEventAdapter.ViewHolder holder, int position) {

        SculaEvent sculaEvent = sculaEventList.get(position);

        holder.eventTitle.setText(sculaEvent.getTitle());
        holder.eventDescription.setText(sculaEvent.getDescription());
        holder.eventAddedDate.setText(sculaEvent.getEventAddedDate());
    }

    @Override
    public int getItemCount() {
        return sculaEventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView eventTitle;
        private TextView eventDescription;
        private TextView eventAddedDate;
        private Button starredEvent;
        private Button deleteEvent;

        public ViewHolder(Context con, View itemView) {
            super(itemView);
            context = con;

            eventTitle = (TextView) itemView.findViewById(R.id.titleTextView);
            eventDescription =(TextView) itemView.findViewById(R.id.descriptionTextView);
            eventAddedDate = (TextView) itemView.findViewById(R.id.addedDateTextView);

            starredEvent = (Button)itemView.findViewById(R.id.starredEventBtn);
            starredEvent.setOnClickListener(this);

            deleteEvent = (Button) itemView.findViewById(R.id.deleteButton);
            deleteEvent.setOnClickListener(this);

            // Handle adapter items Event Listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // goes oo event's details
                    SculaEvent sculaEvent = sculaEventList.get(getAdapterPosition());

                    Toast.makeText(context, sculaEvent.getTitle() + " is Clicked", Toast.LENGTH_SHORT).show();
                    // or create a details activity for the item
                    // to proceed more action on it
                    // using the following block
                    /**
                    Intent intent = new Intent(context , EventDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("clickedEvent" , sculaEvent);
                    intent.putExtras(bundle);
                    */
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.starredEventBtn :
                    AddEventToFavorite(sculaEventList.get(getAdapterPosition()));
                    break;
                case R.id.deleteButton:
                    DeleteEvent(sculaEventList.get(getAdapterPosition()).getEventID());
            }
        }

        // Delete an event
        private void DeleteEvent(final int eventID) {

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.confirm_dialog , null);

            dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setView(view);
            dialog = dialogBuilder.create();
            dialog.show();

            Button noButton = (Button) view.findViewById(R.id.confirmNoBtn);
            Button yesButton = (Button) view.findViewById(R.id.confirmYesBtn);

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler databaseHandler = new DatabaseHandler(context);
                    // delete from database
                    databaseHandler.DeleteEvent(eventID);
                    // remove from adapter
                    sculaEventList.remove(getAdapterPosition());
                    // refresh adapter after removing an item
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();

                }
            });
        }
        // add event to favorite
        private void AddEventToFavorite(SculaEvent sculaEvent) {


        }
    }
}
