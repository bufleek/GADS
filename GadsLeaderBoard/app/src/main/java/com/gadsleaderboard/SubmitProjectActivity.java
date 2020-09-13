package com.gadsleaderboard;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gadsleaderboard.utils.ApiEndPoints;
import com.gadsleaderboard.utils.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitProjectActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private EditText etFirstName, etLastName, etProjectLink, etEmail;
    private Button btnSubmit;
    private ProgressBar mProgressBar;

    private String name, lastName;
    private String projectLink;
    private String email;
    private final String submitUrl = "https://docs.google.com/forms/d/e/1FAIpQLSf9d1TcNU6zc6KR8bSEM41Z1g1zl35cwZr2xyjIhaMAz8WChQ/formResponse";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_project);

        mToolbar = findViewById(R.id.toolbar);
        etFirstName = findViewById(R.id.et_firstName);
        etLastName = findViewById(R.id.et_lastName);
        etProjectLink = findViewById(R.id.et_projectLink);
        etEmail = findViewById(R.id.et_email);
        btnSubmit = findViewById(R.id.btn_submit);
        mProgressBar = findViewById(R.id.progressBar);

        btnSubmit.setOnClickListener(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_submit:{
                Toast.makeText(getApplicationContext(), "Submit", Toast.LENGTH_SHORT).show();
                Dialog dialog = new Dialog(SubmitProjectActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.submit_confirmation_dialog);
                dialog.findViewById(R.id.close_dialog).setOnClickListener(view12 -> dialog.cancel());
                dialog.findViewById(R.id.btn_confirmDialog).setOnClickListener(view1 -> {
                    dialog.dismiss();
                    submitProject();
                });
                dialog.show();
            }
        }
    }

    private void submitProject() {
        if(validateInput()){
            mProgressBar.setVisibility(View.VISIBLE);
            ApiEndPoints service = RetrofitBuilder.buildSerVice(ApiEndPoints.class);
            Call<Void> call = service.submitProject(submitUrl, email, name, lastName, projectLink);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        mProgressBar.setVisibility(View.INVISIBLE);
                        Dialog dialog = new Dialog(SubmitProjectActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.success_submit_dialog);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 3000);
                        dialog.show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Dialog dialog = new Dialog(SubmitProjectActivity.this);
                    dialog.setContentView(R.layout.submit_fail_dialog);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 3000);
                    dialog.show();
                }
            });

        }
        else{
        }

    }

    private boolean validateInput() {
        name = etFirstName.getText().toString().trim();
        lastName = etLastName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        projectLink = etProjectLink.getText().toString().trim();

        if (name.isEmpty()){
            etFirstName.setError("");
            return false;
        }
        if (lastName.isEmpty()){
            etLastName.setError("");
            return false;
        }
        if (email.isEmpty()){
            etEmail.setError("");
           return false;
        }
        if (projectLink.isEmpty()){
            etProjectLink.setError("");
            return false;
        }
        return true;
    }
}