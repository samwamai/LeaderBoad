    package com.gads.leaderboad;

    import android.app.Activity;
    import android.app.AlertDialog;
    import android.view.LayoutInflater;

    public class LoadingDialog {
        private Activity activity;
        private AlertDialog alertDialog;

         LoadingDialog(Activity activity){
            this.activity=activity;
        }

        public void startDialog(){
            AlertDialog.Builder builder=new AlertDialog.Builder(activity);

            LayoutInflater inflater=activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.custom_loading_dialog,null));
            builder.setCancelable(false);

            alertDialog=builder.create();
            alertDialog.show();
        }
        public void dismissDialog(){
          alertDialog.dismiss();
        }


    }
