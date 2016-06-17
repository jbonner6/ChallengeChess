package com.jnbonner.familymap.Filter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.jnbonner.familymap.Model.AllModel;
import com.jnbonner.familymap.R;

import java.util.List;


public class FilterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FilterAdapter mAdapter;
    private RecyclerView mFilterRecyclerView;
    private TextView filterListItemTitle;
    private TextView filterListItemDescription;
    private Switch filterListItemSwitch;

    public FilterFragment() {
        // Required empty public constructor
    }


    public static FilterFragment newInstance(String param1, String param2) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        // create the recycler view
        mFilterRecyclerView = (RecyclerView) view
                .findViewById(R.id.filter_recycler_view);
        mFilterRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        //populate and set the adapter for the recycler view
        List<FilterListItem> eventTypes = AllModel.getEventTypes();

        mAdapter = new FilterAdapter(eventTypes);
        mFilterRecyclerView.setAdapter(mAdapter);
    }

    // The view holder class for the recycler view
    private class FilterHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{
        private FilterListItem mFilterListItem;
        public FilterHolder(View itemView) {
            super(itemView);
            filterListItemTitle = (TextView)itemView
                    .findViewById(R.id.filter_list_item_title_text_view);
            filterListItemDescription = (TextView)itemView
                    .findViewById(R.id.filter_list_item_description_text_view);
            filterListItemSwitch = (Switch)itemView
                    .findViewById(R.id.filter_list_item_switch);

            filterListItemSwitch.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mFilterListItem.setFilterShown(!mFilterListItem.isFilterShown());
        }

        public void bindFilterItem(FilterListItem filterListItem){
            mFilterListItem = filterListItem;
            filterListItemTitle.setText(AllModel.toUp(mFilterListItem.getFilterTitle()));
            filterListItemDescription.setText(mFilterListItem.getFilterDescription());
            filterListItemSwitch.setChecked(filterListItem.isFilterShown());
        }

    }

    // The adapter class for the recycler view
    private class FilterAdapter extends RecyclerView.Adapter<FilterHolder> {

        private List<FilterListItem> mFilterOptions;

        public FilterAdapter(List<FilterListItem> filterOptions) {
            mFilterOptions = filterOptions;
        }

        @Override
        public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.filter_list_item, parent, false);
            return new FilterHolder(view);
        }

        @Override
        public void onBindViewHolder(FilterHolder holder, int position) {
            FilterListItem filterItem = mFilterOptions.get(position);
            holder.bindFilterItem(filterItem);
        }
        @Override
        public int getItemCount() {
            return mFilterOptions.size();
        }
    }
}
