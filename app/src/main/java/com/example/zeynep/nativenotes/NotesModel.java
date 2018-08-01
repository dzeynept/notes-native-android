package com.example.zeynep.nativenotes;

import java.util.UUID;

public class NotesModel {
    private String note_uniq_id;
    private String note;
    private String user_id_from_notes;

    public NotesModel(String note, String user_id_from_notes) {
        this.note = note;
        this.user_id_from_notes = user_id_from_notes;
        this.note_uniq_id = UUID.randomUUID().toString();
    }

    public NotesModel() {
    }

    public String getNote_uniq_id() {
        return note_uniq_id;
    }

    public void setNote_uniq_id(String note_uniq_id) {
        this.note_uniq_id = note_uniq_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUser_id_from_notes() {
        return user_id_from_notes;
    }

    public void setUser_id_from_notes(String user_id_from_notes) {
        this.user_id_from_notes = user_id_from_notes;
    }
}
