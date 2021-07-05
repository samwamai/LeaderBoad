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
    import com.gads.leaderboad.Model.LeaderHours;
    import com.gads.leaderboad.Services.LeaderHoursFetchService;
    import com.gads.leaderboad.Services.ServiceBuilder;

    import java.util.ArrayList;
    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    import static android.content.ContentValues.TAG;

    public class LeadersHoursFragment extends Fragment {
        private RecyclerView recyclerView;
        private LeaderBoardRecyclerviewAdapter leaderBoardRecyclerviewAdapter;
        private List<LeaderHours> leaderHours;
        private LoadingDialog loadingDialog;

        //add a public constructor
        public LeadersHoursFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Log.e(TAG, "onCreateView: ");
            final View view = inflater.inflate(R.layout.leadershours_fragment, container, false);
            loadingDialog=new LoadingDialog(this.getActivity());
            loadingDialog.startDialog();
            recyclerView = view.findViewById(R.id.RecView);
            leaderHours = new ArrayList<>();
             return view;
        }

        @Override
        public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Log.e(TAG, "onViewCreated: ");
            LeaderHoursFetchService leaderHoursFetchService = ServiceBuilder.buildService(LeaderHoursFetchService.class);
            Call<List<LeaderHours>> leaderBoardRequest = leaderHoursFetchService.getHours();

            leaderBoardRequest.enqueue(new Callback<List<LeaderHours>>() {
                @Override
                public void onResponse(Call<List<LeaderHours>> call, Response<List<LeaderHours>> response) {

                    Log.e(TAG, "onResponse: "+response.body());
                    leaderBoardRecyclerviewAdapter = new LeaderBoardRecyclerviewAdapter();
                    leaderBoardRecyclerviewAdapter.set_by_LeaderHours( response.body());
                    recyclerView.setAdapter(leaderBoardRecyclerviewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    loadingDialog.dismissDialog();
                }

                @Override
                public void onFailure(Call<List<LeaderHours>> call, Throwable t) {
                    loadingDialog.dismissDialog();
                    Log.d(TAG, "onFailure: unable to load data"+ t.getMessage());
                }
            });
        }


    }
