package com.softwareengineeringproject.studymonster;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.softwareengineeringproject.studymonster.data.AppDatabase;
import com.softwareengineeringproject.studymonster.data.FlashcardCollectionDAO;
import com.softwareengineeringproject.studymonster.data.FlashcardCollectionEntity;
import com.softwareengineeringproject.studymonster.data.FlashcardDAO;
import com.softwareengineeringproject.studymonster.data.FlashcardEntity;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;

public class FlashcardCollectionFragment extends Fragment {
    private FragmentActivity activity;
    private AppDatabase database;
    private FlashcardDAO flashcardDAO;
    private List<FlashcardEntity> flashcards;

    private String name;
    private int collectionId;

    private static final String ARG_NAME = "name";
    private static final String ARG_COLLECTION_ID = "collection_id";

    public static FlashcardCollectionFragment Instantiate(String name, int collectionId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_NAME, name);
        arguments.putInt(ARG_COLLECTION_ID, collectionId);

        FlashcardCollectionFragment fragment = new FlashcardCollectionFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        if (arguments != null) {
            name = arguments.getString(ARG_NAME);
            collectionId = arguments.getInt(ARG_COLLECTION_ID);
        }

        activity = getActivity();
        activity.setTitle(R.string.app_flashcard_collection);

        ChipNavigationBar navbar = activity.findViewById(R.id.navbar);
        navbar.setItemSelected(navbar.getSelectedItemId(), false);

        database = AppDatabase.GetInstance(activity);
        flashcardDAO = database.FlashcardDAO();

        Executors.newSingleThreadExecutor().execute(() -> {
            List<FlashcardEntity> test = flashcardDAO.GetFlashcards();
            flashcards = flashcardDAO.GetFlashcardsInCollection(collectionId);

            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            for (FlashcardEntity flashcard : flashcards) {
                FlashcardFragment flashcardFragment = FlashcardFragment.Instantiate(flashcard.term, flashcard.content);
                transaction.add(R.id.flashcard_collection_fragment_flashcard_container, flashcardFragment, flashcard.term);
            }

            transaction.commit();
        });
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_flashcard_collection, container, false);

        TextView title = layout.findViewById(R.id.flashcard_collection_fragment_title);
        title.setText(name);

        FloatingActionButton addFlashcardCollectionButton = layout.findViewById(R.id.flashcard_collection_fragment_add_flashcard);
        addFlashcardCollectionButton.setOnClickListener(view -> AddFlashcard());

        return layout;
    }

    public void AddFlashcard() {
        final int maxLength = 100;

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText termText = new EditText(activity);
        termText.setHint(R.string.flashcard_collection_fragment_flashcard_term_hint);
        termText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });

        EditText contentText = new EditText(activity);
        contentText.setHint(R.string.flashcard_collection_fragment_flashcard_content_hint);
        contentText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });

        layout.addView(termText);
        layout.addView(contentText);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        alertDialog.setTitle(R.string.flashcard_collection_fragment_add_flashcard_dialog_title);
        alertDialog.setView(layout);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton(R.string.create_button, null);
        alertDialog.setNegativeButton(R.string.cancel_button, (dialog, which) -> dialog.cancel());

        AlertDialog dialog = alertDialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(dialogView -> {
            String term = termText.getText().toString();
            String content = contentText.getText().toString();

            if (term.trim().isEmpty()) {
                termText.setError("Invalid Flashcard Term");
                return;
            }

            if (content.trim().isEmpty()) {
                contentText.setError("Invalid Flashcard Content");
                return;
            }

            FlashcardEntity flashcard = new FlashcardEntity();
            flashcard.term = term;
            flashcard.content = content;
            flashcard.collectionId = collectionId;

            Executors.newSingleThreadExecutor().execute(() -> {
                FlashcardEntity result = flashcardDAO.GetFlashcardByTerm(term, collectionId);

                if (result != null) {
                    activity.runOnUiThread(() -> {
                        termText.setError("Flashcard Already Exists");
                    });

                    return;
                }

                flashcardDAO.InsertFlashcard(flashcard);

                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FlashcardFragment flashcardFragment = FlashcardFragment.Instantiate(term, content);

                fragmentManager.beginTransaction()
                        .add(R.id.flashcard_collection_fragment_flashcard_container, flashcardFragment, term)
                        .commit();

                dialog.dismiss();
            });
        });
    }
}