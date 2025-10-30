package com.exam.brnquiz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment {

    public ScoreFragment() { super(R.layout.fragment_score);}


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BRNViewModel viewModel = new ViewModelProvider(requireActivity()).get(BRNViewModel.class);
        TextView scoreNumber = view.findViewById(R.id.score_number);

        viewModel.score.observe(getViewLifecycleOwner(), value -> {
            scoreNumber.setText(String.valueOf(value));
        });
    }
}