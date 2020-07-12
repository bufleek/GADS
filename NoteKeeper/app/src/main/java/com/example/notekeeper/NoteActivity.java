package com.example.notekeeper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.notekeeper.NoteKeeperDatabaseContract.NoteInfoEntry;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    private static final int POSITION_NOT_SET = -1;
    private final String TAG = getClass().getSimpleName();
    public static final String NOTE_ID = "com.example.notekeeper.NOTE_ID";
    public static final String NOTE_POSITION = "com.example.notekeeper.NOTE_POSITION";
    public static final int ID_NOT_SET = -1;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private EditText mTextNoteText;
    private EditText mTextNoteTitle;
    private Spinner mSpinnerCourses;ImageView mImageView;
    private int mNoteId;
    private boolean mIsCanceling;
    private NoteActivityViewModel mViewModel;
    private NoteKeeperOpenHelper mDbOpenHelper;
    private Cursor mNoteCursor;
    private int mCourseIdPos;
    private int mNoteTitlePos;
    private int mNoteTextPos;
    private int mCurrentPosition;

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbOpenHelper = new NoteKeeperOpenHelper(this);

        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel = viewModelProvider.get(NoteActivityViewModel.class);

        if(mViewModel.mIsNewlyCreated && savedInstanceState != null)
            mViewModel.restoreState(savedInstanceState);
        mViewModel.mIsNewlyCreated = false;

        mSpinnerCourses = findViewById(R.id.spinner_courses);
        List <CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(adapterCourses);
        
        readDisplayStateValues();

        mTextNoteTitle = findViewById(R.id.text_note_title);
        mTextNoteText = findViewById(R.id.text_note_text);
        mImageView = findViewById(R.id.captured_image);

        if(!mIsNewNote)
            loadNoteData();

        saveOriginalNoteValues();
        Log.d(TAG, "onCreate");
    }

    private void loadNoteData() {
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

        String selection = NoteInfoEntry._ID+" = ?";
        String [] selectionArgs = {Integer.toString(mNoteId)};
        String[] noteColumns = {NoteInfoEntry.COLUMN_COURSE_ID,NoteInfoEntry.COLUMN_NOTE_TITLE, NoteInfoEntry.COLUMN_NOTE_TEXT};

        mNoteCursor = db.query(NoteInfoEntry.TABLE_NAME, noteColumns, selection, selectionArgs, null, null, null);
        mCourseIdPos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        mNoteTitlePos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mNoteTextPos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);
        mNoteCursor.moveToNext();
        displayNote();
    }

    private void saveOriginalNoteValues() {
        if(mIsNewNote)
            return;

        mViewModel.mOriginalCourseId = mNote.getCourse().getCourseId();
        mViewModel.mOriginalNoteTitle = mNote.getTitle();
        mViewModel.mOriginalNoteText = mNote.getText();
    }

    private void displayNote() {
        String courseId = mNoteCursor.getString(mCourseIdPos);
        String noteTitle = mNoteCursor.getString(mNoteTitlePos);
        String noteText = mNoteCursor.getString(mNoteTextPos);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        CourseInfo course = DataManager.getInstance().getCourse(courseId);
        int courseIndex = courses.indexOf(course);
        mSpinnerCourses.setSelection(courseIndex);
        mTextNoteTitle.setText(noteTitle);
        mTextNoteText.setText(noteText);

    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        mNoteId = intent.getIntExtra(NOTE_ID, ID_NOT_SET);
        mCurrentPosition = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mIsNewNote = mNoteId == ID_NOT_SET;
        if(mIsNewNote){
            createNewNote();
        }
        Log.i(TAG, "mNotePosition: "+ mNoteId);
        mNote = DataManager.getInstance().getNotes().get(mCurrentPosition);

    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNoteId = dm.createNewNote();
        //mNote = dm.getNotes().get(mNotePosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        }
//        else if (id == R.id.action_use_camera) {
//            useCamera();
//            return true;
//        }
        else if(id == R.id.action_cancel){
            mIsCanceling = true;
            finish();
        }
        else if(id == R.id.action_next){
            moveNext();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_next);
        int lastNoteIndex = DataManager.getInstance().getNotes().size() - 1;
        item.setEnabled(mNoteId < lastNoteIndex);
        return super.onPrepareOptionsMenu(menu);
    }

    private void moveNext() {
        saveNote();

        ++mNoteId;
        mNote = DataManager.getInstance().getNotes().get(mNoteId);
        saveOriginalNoteValues();
        displayNote();
        invalidateOptionsMenu();
    }

    private void useCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }else{
            Toast.makeText(getApplicationContext(), "Your Phone Has No Camera Application Installed", Toast.LENGTH_LONG).show();
        }
    }

    private void sendEmail() {
        CourseInfo course = (CourseInfo) mSpinnerCourses.getSelectedItem();
        String subject = mTextNoteTitle.getText().toString();
        String text = "Check out what i learned in the pluralsight course \"" + course.getTitle() +"\"\n" + mTextNoteText.getText();
//        implicit intent to send email
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mIsCanceling){
            Log.i(TAG, "Cancelling Note at position "+ mNoteId);
            if(mIsNewNote) {
                DataManager.getInstance().removeNote(mNoteId);
            }else{
                storePreviousNoteValues();
            }
        }else{
            saveNote();
        }

        Log.d(TAG, "onPause");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null)
            mViewModel.saveState(outState);
    }

    private void storePreviousNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(mViewModel.mOriginalCourseId);
        mNote.setCourse(course);
        mNote.setTitle(mViewModel.mOriginalNoteTitle);
        mNote.setText(mViewModel.mOriginalNoteText);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) mSpinnerCourses.getSelectedItem() );
        mNote.setTitle(mTextNoteTitle.getText().toString());
        mNote.setText(mTextNoteText.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}