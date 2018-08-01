package com.example.zeynep.nativenotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {
    Context context;
    NoteInterface noteInterface;
    List<NotesModel> notes_list;
    int i;
    boolean isFromSearch;

    NotesAdapter(List<NotesModel> notes_list, Context context, NoteInterface noteInterface) {
        this.context = context;
        this.notes_list = notes_list;
        this.noteInterface = noteInterface;
    }
    NotesAdapter(boolean isFromSearch, int i,List<NotesModel> notes_list, Context context, NoteInterface noteInterface) {
        this.context = context;
        this.notes_list = notes_list;
        this.noteInterface = noteInterface;
        this.i = i;
        this.isFromSearch = isFromSearch;
    }

    @Override
    public NotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.notes_item, parent, false);
        return new NotesHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull NotesHolder holder, final int position) {

        holder.notes.setText(notes_list.get(position).getNote());
        holder.note_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFromSearch) {
                    noteInterface.showUpdateNotePopup(i);
                } else {
                    noteInterface.showUpdateNotePopup(position);

                }
            }
        });
        // todo: create dialog nd btns
    }

    @Override
    public int getItemCount() {
        return notes_list.size();
    }

    public class NotesHolder extends RecyclerView.ViewHolder {
        TextView notes;
        ImageView note_img;
        public NotesHolder(View itemView) {
            super(itemView);
            notes = itemView.findViewById(R.id.notes_item_txt);
            note_img = itemView.findViewById(R.id.notes_item_img);
        }
    }
}
