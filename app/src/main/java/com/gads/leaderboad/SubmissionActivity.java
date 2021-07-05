    package com.gads.leaderboad;

    import androidx.appcompat.app.AppCompatActivity;

    import android.app.Activity;
    import android.app.AlertDialog;
    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.Toast;

    import com.gads.leaderboad.Model.Submit_info;
    import com.gads.leaderboad.Services.GoogleFormsFetchService;
    import com.gads.leaderboad.Services.ServiceBuilder;
    import com.gads.leaderboad.Services.ServiceBuilderForm;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    import static android.content.ContentValues.TAG;

    public class SubmissionActivity extends AppCompatActivity {
        private Activity activity;
        private AlertDialog alertDialog;
        private EditText fname,lname,email,gitlink;
        private Button submit,confirmsubmit;
        private ImageView cancle;
        private String fnamev,lnamev,emailv,gitlinkv;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_submission);

            activity=this;
            inti();

        }


        void inti(){
            fname=findViewById(R.id.textInputfname);
            lname=findViewById(R.id.textInputlname);
            email=findViewById(R.id.textInputemail);
            gitlink=findViewById(R.id.textInputgitlink);
            submit=findViewById(R.id.button_submit_2);



            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 chech_fields();
                }
            });

        }


        private void chech_fields(){
            fnamev=fname.getText().toString();
            lnamev=lname.getText().toString();
            emailv=email.getText().toString();
            gitlinkv=gitlink.getText().toString();


            if (fnamev.equals(""))
            {
                Toast.makeText(this,"first name cant be empty",Toast.LENGTH_SHORT).show();
            }
            else if (lnamev.equals(""))
            {
                Toast.makeText(this,"last name cant be empty",Toast.LENGTH_SHORT).show();

            }
            else if (emailv.equals(""))
            {
                Toast.makeText(this,"email name cant be empty",Toast.LENGTH_SHORT).show();

            }
            else if (gitlinkv.equals(""))
            {
                Toast.makeText(this,"github link name cant be empty",Toast.LENGTH_SHORT).show();

            }else {
                start_submit_Dialog();
            }
        }


        public void start_submit_Dialog(){
            AlertDialog.Builder builder=new AlertDialog.Builder(activity);
            LayoutInflater inflater=activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.custom_confirm_sub_dialog,null));
            builder.setCancelable(false);
            alertDialog=builder.create();
            alertDialog.show();
        }

        public void start_responce_Dialog(boolean success){
            AlertDialog.Builder builder=new AlertDialog.Builder(activity);
            LayoutInflater inflater=activity.getLayoutInflater();
            if (success){
                builder.setView(inflater.inflate(R.layout.custom_success_dialog,null));
            }else {
                builder.setView(inflater.inflate(R.layout.custom_fail_dialog,null));
            }
            builder.setCancelable(true);
            alertDialog=builder.create();
            alertDialog.show();
        }

        public void dismissDialog(){
            alertDialog.dismiss();
        }


        void submit_form(){
            GoogleFormsFetchService googleFormsFetchService = ServiceBuilderForm.buildService(GoogleFormsFetchService.class);
            Call<Submit_info> googlerequest = googleFormsFetchService.submit_form(fnamev, lnamev, emailv, gitlinkv);

            googlerequest.enqueue(new Callback<Submit_info>() {
                @Override
                public void onResponse( Call<Submit_info> call, Response<Submit_info> response) {

                    Log.e(TAG, "onResponse: "+response);
                    start_responce_Dialog(true);
                }

                @Override
                public void onFailure( Call<Submit_info> call, Throwable t) {
                    Log.d(TAG, "onFailure: unable to load data"+ t.getMessage());
                    start_responce_Dialog(false);
                }
            });
        }


        public void back_button_press(View view) {
            Intent intent=(new Intent(SubmissionActivity.this,MainActivity.class));
            startActivity(intent);
        }

        public void cancle_submit_dialog(View view) {
            dismissDialog();
        }

        public void confirm_submit_button(View view) {
            submit_form();
            dismissDialog();
        }
    }