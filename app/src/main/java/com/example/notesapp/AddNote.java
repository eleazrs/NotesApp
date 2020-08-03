package com.example.notesapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddNote extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        textView = findViewById(R.id.noteTextView);
        textView.setTag(MainActivity.notePosition);

        if(MainActivity.notePosition >= 0)
            textView.setText(MainActivity.getNote(MainActivity.notePosition));
    }

    public void saveNote(View view) {
        int position = Integer.parseInt(textView.getTag().toString());
        MainActivity.saveNote(textView.getText().toString(), getApplicationContext(), position);
        finish();
    }
}