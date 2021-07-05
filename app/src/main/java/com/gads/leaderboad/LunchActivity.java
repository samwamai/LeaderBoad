    package com.gads.leaderboad;

    import androidx.appcompat.app.AppCompatActivity;

    import android.animation.Animator;
    import android.animation.AnimatorInflater;
    import android.app.Activity;
    import android.app.AlertDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.os.Bundle;
    import android.os.Handler;
    import android.os.Looper;
    import android.util.Log;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    public class LunchActivity extends AppCompatActivity implements Animator.AnimatorListener {
        ImageView imageView;
        Animator animator;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lunch);

            imageView=findViewById(R.id.image_luncher_view);




            animator= AnimatorInflater.loadAnimator(this,R.animator.luncher_animator);
            animator.setTarget(imageView);
            animator.addListener(this);
            animator.start();

        }





        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
           statActivityNext();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        public  void statActivityNext(){
            Intent intent=(new Intent(LunchActivity.this,MainActivity.class));
            startActivity(intent);
            finish();
        }

    }