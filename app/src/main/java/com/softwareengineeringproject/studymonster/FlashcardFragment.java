package com.softwareengineeringproject.studymonster;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FlashcardFragment extends Fragment {
    private boolean isFlipped = false;

    private String term;
    private String content;

    private static final String ARG_TERM = "term";
    private static final String ARG_CONTENT = "content";

    public static FlashcardFragment Instantiate(String term, String content) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TERM, term);
        arguments.putString(ARG_CONTENT, content);

        FlashcardFragment fragment = new FlashcardFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        if (arguments != null) {
            term = arguments.getString(ARG_TERM);
            content = arguments.getString(ARG_CONTENT);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_flashcard, container, false);

        Button button = layout.findViewById(R.id.flashcard_fragment_flashcard_button);
        button.setText(term);

        button.setOnClickListener(view -> {
            if (isFlipped) {
                button.setText(term);
            }
            else {
                button.setText(content);
            }

            isFlipped = !isFlipped;
        });

        return layout;
    }
}