package com.exam.brnquiz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {

    public QuestionFragment() {
        super(R.layout.fragment_question);
    }

    private BRNViewModel viewModel;
    private int selectedIndex = -1;
    private ArrayAdapter<String> adapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(BRNViewModel.class);

        TextView questionText = view.findViewById(R.id.question_text);
        ListView answerList = view.findViewById(R.id.answer_list);
        Button submitButton = view.findViewById(R.id.submit_button);

        submitButton.setEnabled(false);

        viewModel.question.observe(getViewLifecycleOwner(), questionText::setText);

        viewModel.answers.observe(getViewLifecycleOwner(), list -> {
            adapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_list_item_single_choice, list);
            answerList.setAdapter(adapter);
            answerList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            selectedIndex = -1;
            submitButton.setEnabled(false);
        });

        answerList.setOnItemClickListener((parent, v, position, id) -> {
            selectedIndex = position;
            submitButton.setEnabled(true);
        });

        submitButton.setOnClickListener(v -> {
            if (selectedIndex == -1) {
                Toast.makeText(requireContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.submitAnswer(selectedIndex);

            if (selectedIndex == viewModel.correctIndex) {
                Toast.makeText(requireContext(), "Yay! +1 Point", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Wrong! Use your BRN(AI)", Toast.LENGTH_LONG).show();
            }

            answerList.setItemChecked(viewModel.correctIndex, true);

            submitButton.setEnabled(false);
        });
    }
}