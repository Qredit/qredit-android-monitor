package com.vrlcrypt.qreditmonitor.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrlcrypt.qreditmonitor.R;
import com.vrlcrypt.qreditmonitor.MainActivity;
import com.vrlcrypt.qreditmonitor.adapters.VotesAdapter;
import com.vrlcrypt.qreditmonitor.models.Settings;
import com.vrlcrypt.qreditmonitor.models.Votes;
import com.vrlcrypt.qreditmonitor.services.QreditService;
import com.vrlcrypt.qreditmonitor.services.RequestListener;
import com.vrlcrypt.qreditmonitor.utils.Utils;

public class VotesFragment extends Fragment implements RequestListener<Votes> {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public VotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_votes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.votes_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        if (Utils.isOnline(getActivity())) {
            loadVotes();
        } else {
            Utils.showMessage(getResources().getString(R.string.internet_off), view);
        }
    }

    @Override
    public void onDestroy() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.hideLoadingIndicatorView();
        }
        super.onDestroy();
    }

    private void loadVotes() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.showLoadingIndicatorView();
        }

        Settings settings = Utils.getSettings(getActivity());

        QreditService.getInstance().requestVotes(settings, this);
    }

    @Override
    public void onFailure(final Exception e) {
        if (!isAdded()) {
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.showMessage(getString(R.string.unable_to_retrieve_data), getView());

                mSwipeRefreshLayout.setRefreshing(false);

                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.hideLoadingIndicatorView();
                }
            }
        });
    }

    @Override
    public void onResponse(final Votes votes) {
        if (!isAdded()) {
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                View view = getView();

                RecyclerView rvVotes = (RecyclerView) view.findViewById(R.id.rvVotes);

                VotesAdapter adapter = new VotesAdapter(votes);
                rvVotes.setAdapter(adapter);
                rvVotes.setLayoutManager(new LinearLayoutManager(getActivity()));

                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.hideLoadingIndicatorView();
                }

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void refreshContent() {
        Settings settings = Utils.getSettings(getActivity());
        QreditService.getInstance().requestVotes(settings, this);
    }

}
