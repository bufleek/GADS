package com.example.notekeeper;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notekeeper.NoteKeeperDatabaseContract.CourseInfoEntry;
import com.google.android.material.snackbar.Snackbar;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.Viewolder>{
    private final Context mContext;
    private Cursor mCursor;
    private final LayoutInflater mLayoutInflater;
    private int mIdPos;
    private int mTitlePos;

    public CourseRecyclerAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mLayoutInflater = LayoutInflater.from(mContext);
        getCursorColumnPositions();
    }

    private void getCursorColumnPositions() {
        if (mCursor == null)
            return;
        mIdPos = mCursor.getColumnIndex(CourseInfoEntry._ID);
        mTitlePos = mCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);
    }

    public void changeCursor(Cursor cursor){
        if (mCursor != null)
            mCursor.close();

        mCursor = cursor;
        getCursorColumnPositions();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Viewolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_course_list, parent, false);
        return new Viewolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewolder holder, int position) {
        mCursor.moveToPosition(position);
        String course = mCursor.getString(mTitlePos);
        holder.mTextCourse.setText(course);
        holder.mCurrentPosition = position;
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0: mCursor.getCount();
    }

    public class Viewolder extends RecyclerView.ViewHolder{

        public final TextView mTextCourse;
        public int mCurrentPosition;

        public Viewolder(@NonNull View itemView) {
            super(itemView);
            mTextCourse = (TextView) itemView.findViewById(R.id.text_course);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Snackbar.make(v, mCourses.get(mCurrentPosition).getTitle(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }
}
