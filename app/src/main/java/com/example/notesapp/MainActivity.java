package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static List<String> notes;
    public static int notePosition;
    public static RecyclerView recyclerView;
    public static SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.addNote) {
            MainActivity.notePosition = -1;
            Intent intent = new Intent(this, AddNote.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String notesSerialized = sharedPreferences.getString("notes", "");
        if (Objects.equals(notesSerialized, "")) notes = new ArrayList<>();
        else getSerializedNotedFromSharedPreferences();

        // set up the RecyclerView
        recyclerView = findViewById(R.id.recycler);
        fillRecyclerView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        fillRecyclerView();
        putSerializedNotesIntoSharedPreferences(convertNotesToSerialized());
    }

    public static void saveNote(String note, Context context, int position) {
        if (position >= 0) MainActivity.notes.set(position, note);
        else MainActivity.notes.add(note);

        Toast.makeText(context, "Note saved", Toast.LENGTH_SHORT).show();
    }

    public static void deleteNode(int position) {
        MainActivity.notes.remove(position);
        Log.i("Item deleted", String.valueOf(position));
    }

    public static String getNote(int position) {
        return MainActivity.notes.get(position);
    }

    private void fillRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyRecyclerViewAdapter(this, notes));
    }

    public static String convertNotesToSerialized() {
        try {
            return new ObjectMapper().writeValueAsString(MainActivity.notes);
        } catch (JsonProcessingException e) {
            Log.e("JsonProcessingException", "Error serializing notes to string, damn!");
        }
        return null;
    }

    private void getSerializedNotedFromSharedPreferences() {
        String serializedNotes = sharedPreferences.getString("notes", "");
        try {
            MainActivity.notes = new ObjectMapper().readValue(serializedNotes, new TypeReference<ArrayList<String>>() {
            });
        } catch (JsonProcessingException e) {
            Log.e("JsonProcessingException", "Error serializing json from serializedNotes in SharedPreferences");
        }
    }

    private void putSerializedNotesIntoSharedPreferences(String serializedNotes) {
        sharedPreferences.edit().putString("notes", serializedNotes).apply();
    }

    public static List<String> getAllNotes() {
        return MainActivity.notes;
    }

}