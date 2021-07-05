    package com.gads.leaderboad;

    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.gads.leaderboad.Adapters.LeaderBoardRecyclerviewAdapter;
    import com.gads.leaderboad.Model.LeaderIQ;
    import com.gads.leaderboad.Services.LeaderIQFetchService;
    import com.gads.leaderboad.Services.ServiceBuilder;

    import java.util.ArrayList;
    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    import static android.content.ContentValues.TAG;

    public class LeadersSkillIQFragment extends Fragment {
        private RecyclerView recyclerView;
        private LeaderBoardRecyclerviewAdapter leaderBoardRecyclerviewAdapter;
        private List<LeaderIQ> leaderskill;
        private LoadingDialog loadingDialog;

        //required public constructor
        public LeadersSkillIQFragment(){
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.skill_iq_fragment, container, false);
            loadingDialog=new LoadingDialog(this.getActivity());
            loadingDialog.startDialog();
            recyclerView = view.findViewById(R.id.RecView);
            leaderskill = new ArrayList<>();
            return view;
        }
        @Override
        public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Log.e(TAG, "onViewCreated: ");
            LeaderIQFetchService leaderIQFetchService = ServiceBuilder.buildService(LeaderIQFetchService.class);
            Call<List<LeaderIQ>> leaderIQRequest = leaderIQFetchService.getIQ();

            leaderIQRequest.enqueue(new Callback<List<LeaderIQ>>() {
                @Override
                public void onResponse(Call<List<LeaderIQ>> call, Response<List<LeaderIQ>> response) {

                    Log.e(TAG, "onResponse: "+response.body());
                    leaderBoardRecyclerviewAdapter = new LeaderBoardRecyclerviewAdapter();
                    leaderBoardRecyclerviewAdapter.set_by_LeaderSkillIQ( response.body());
                    recyclerView.setAdapter(leaderBoardRecyclerviewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    loadingDialog.dismissDialog();
                }

                @Override
                public void onFailure(Call<List<LeaderIQ>> call, Throwable t) {
                    loadingDialog.dismissDialog();
                    Log.d(TAG, "onFailure: unable to load data"+ t.getMessage());
                }
            });
        }

    }
