package com.example.notekeeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.Viewolder>{
    private final Context mContext;
    private final List<NoteInfo> mNotes;
    private final LayoutInflater mLayoutInflater;

    public NoteRecyclerAdapter(Context context, List<NoteInfo> notes) {
        mContext = context;
        mNotes = notes;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public Viewolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_note_list, parent, false);
        return new Viewolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewolder holder, int position) {
        NoteInfo note = mNotes.get(position);
        holder.mTextCourse.setText(note.getCourse().getTitle());
        holder.mTextCourse.setText(note.getTitle());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class Viewolder extends RecyclerView.ViewHolder{

        public final TextView mTextCourse;
        public final TextView mTextTitle;

        public Viewolder(@NonNull View itemView) {
            super(itemView);
            mTextCourse = (TextView) itemView.findViewById(R.id.text_course);
            mTextTitle = (TextView) itemView.findViewById(R.id.text_title);
        }
    }
}
