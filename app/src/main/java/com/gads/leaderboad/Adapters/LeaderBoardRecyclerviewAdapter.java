    package com.gads.leaderboad.Adapters;

    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.gads.leaderboad.Model.LeaderHours;
    import com.gads.leaderboad.Model.LeaderIQ;
    import com.gads.leaderboad.R;


    import java.util.ArrayList;
    import java.util.List;

    import static android.content.ContentValues.TAG;
    import static com.gads.leaderboad.R.string.join_learnig_hours_string;

    public class LeaderBoardRecyclerviewAdapter extends RecyclerView.Adapter<LeaderBoardRecyclerviewAdapter.ViewHolder> {

        //initialize the array list
        private List<LeaderHours> leaderHours = new ArrayList<>();
        private List<LeaderIQ> leaderIQ = new ArrayList<>();
        private int view_fragment=0;



        public  LeaderBoardRecyclerviewAdapter(){
            Log.e(TAG, "LeaderBoardRecyclerviewAdapter: ");
         }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //Log.e(TAG, "onCreateViewHolder: ");
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderhours, parent, false);
            if (view_fragment==1){
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skill_iq, parent, false);
            }
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (view_fragment==1){
                holder.bind_IQ( holder, leaderIQ.get(position));
               // Log.e(TAG, "onBindViewHolder: "+ leaderIQ.get(position));

            }else
            holder.bind_hours( holder, leaderHours.get(position));
           // Log.e(TAG, "onBindViewHolder: "+ leaderHours.get(position));
        }

        @Override
        public int getItemCount() {
            if (view_fragment==1){
                return leaderIQ.size();
            }else
            return leaderHours.size();
        }

        public void set_by_LeaderHours(List<LeaderHours> leaderHours) {
            //Log.e(TAG, "setLeaderBoard: "+ leaderHours.toString());
            view_fragment=0;
            this.leaderHours = leaderHours;
            notifyDataSetChanged();
        }
        public void set_by_LeaderSkillIQ(List<LeaderIQ> leaderIQ) {
            //Log.e(TAG, "setLeaderBoard: "+ leaderIQ.toString());
            view_fragment=1;
            this.leaderIQ = leaderIQ;
            notifyDataSetChanged();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
           //initialize the text views
            private TextView txtStudentName, txtHoursEngaged,txtSkillIQ;
            private ImageView image_hours,image_IQ;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                txtStudentName =itemView.findViewById(R.id.student_name);
                txtHoursEngaged =itemView.findViewById(R.id.hours_engaged);
                txtSkillIQ=itemView.findViewById(R.id.SkillIQ);
                image_hours = itemView.findViewById(R.id.image_top_learner);
                image_IQ = itemView.findViewById(R.id.image_skillIQ);


            }

            public void bind_hours(ViewHolder holder, LeaderHours leaderHours){
                //Log.e(TAG, "bind: "+leaderHours.getName()+leaderHours.getHours());
                holder.txtStudentName.setText(leaderHours.getName());
                holder.txtHoursEngaged.setText(new StringBuilder().append(leaderHours.getHours()).append(" learning hours,").append(leaderHours.getCountry()).toString());
               Glide.with( image_hours.getContext())
                       .load(leaderHours.getBadgeUrl())
                       .centerCrop()
                       .into(image_hours);
            }
            public void bind_IQ(ViewHolder holder, LeaderIQ leaderIQ){
                //Log.e(TAG, "bind: "+leaderIQ.getName()+leaderIQ.getScore());
                holder.txtStudentName.setText(leaderIQ.getName());
                holder.txtSkillIQ.setText(new StringBuilder().append(leaderIQ.getScore()).append(" skill IQ Score,").append(leaderIQ.getCountry()).toString());
                Glide.with( image_IQ.getContext())
                        .load(leaderIQ.getBadgeUrl())
                        .centerCrop()
                        .into(image_IQ);
            }

        }


    }