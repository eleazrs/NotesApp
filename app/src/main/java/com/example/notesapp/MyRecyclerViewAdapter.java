package com.example.notesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData.get(position);
        holder.myTextView.setText(animal);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        ViewHolder(final View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.noteTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedItem = getAdapterPosition();
                    MainActivity.notePosition = selectedItem;

                    Context context = view.getContext();
                    Intent intent = new Intent(context, AddNote.class);
                    context.startActivity(intent);

                    Toast.makeText(itemView.getContext(), "Short tap, position is " + selectedItem, Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {
                    final int selectedItem = getAdapterPosition();
                    Toast.makeText(itemView.getContext(), "Long tap, position is " + selectedItem, Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(itemView.getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("You want to delete the note!?")
                            .setMessage("Maybe this note apreciates your presence, are you sure to send it to non existing garbage? " +
                                    "So, the note will be definitely deleted, to nothing, to the forgotten realm.")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // MainActivity.deleteNode(selectedItem);
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MainActivity.deleteNode(selectedItem);
                                    MainActivity.recyclerView.setAdapter(new MyRecyclerViewAdapter(view.getContext(), MainActivity.getAllNotes()));
                                    MainActivity.sharedPreferences.edit().putString("notes", MainActivity.convertNotesToSerialized()).apply();
                                }
                            })
                            .show();
                    return false;
                }
            });
        }

    }

}
