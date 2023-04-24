package com.softwareengineeringproject.studymonster;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.softwareengineeringproject.studymonster.data.FlashcardEntity;

import java.util.List;

public class FlashcardsAdapter extends RecyclerView.Adapter<FlashcardsAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public Button flashcardButton;

        public ViewHolder(View item) {
            super(item);

            flashcardButton = item.findViewById(R.id.flashcard_fragment_flashcard_button);
            flashcardButton.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(
                @NonNull ContextMenu contextMenu,
                @NonNull View view,
                ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuInflater inflater = activity.getMenuInflater();
            inflater.inflate(R.menu.context_menu, contextMenu);
        }
    }

    private FragmentActivity activity;
    private List<FlashcardEntity> flashcards;
    private int currentPosition;

    public FlashcardsAdapter(FragmentActivity activity, List<FlashcardEntity> flashcards) {
        this.activity = activity;
        this.flashcards = flashcards;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.fragment_flashcard, parent, false);

        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        FlashcardEntity flashcard = flashcards.get(position);

        Button button = viewHolder.flashcardButton;
        button.setText(flashcard.term);

        button.setOnClickListener(view -> {
            if (flashcard.isFlipped) {
                button.setText(flashcard.term);
            }
            else {
                button.setText(flashcard.content);
            }

            flashcard.isFlipped = !flashcard.isFlipped;
        });

        button.setOnLongClickListener(view -> {
            currentPosition = viewHolder.getAdapterPosition();

            return false;
        });
    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }

    public int GetPosition() {
        return currentPosition;
    }
}
