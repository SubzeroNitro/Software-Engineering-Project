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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.softwareengineeringproject.studymonster.data.FlashcardCollectionEntity;

import java.util.List;

public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public Button collectionButton;

        public ViewHolder(View item) {
            super(item);

            collectionButton = item.findViewById(R.id.flashcard_collection_fragment_flashcard_collection_button);
            collectionButton.setOnCreateContextMenuListener(this);
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
    private List<FlashcardCollectionEntity> flashcardCollections;
    private int currentPosition;

    public CollectionsAdapter(FragmentActivity activity, List<FlashcardCollectionEntity> flashcardCollections) {
        this.activity = activity;
        this.flashcardCollections = flashcardCollections;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.fragment_flashcard_collection_button, parent, false);

        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        FlashcardCollectionEntity flashcardCollection = flashcardCollections.get(position);

        Button button = viewHolder.collectionButton;
        button.setText(flashcardCollection.name);

        button.setOnClickListener(view -> {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FlashcardCollectionFragment flashcardCollectionFragment = FlashcardCollectionFragment.Instantiate(flashcardCollection.name, flashcardCollection.id);

            fragmentManager.beginTransaction()
                    .replace(R.id.main_activity_fragment_container, flashcardCollectionFragment, flashcardCollection.name)
                    .commit();
        });

        button.setOnLongClickListener(view -> {
            currentPosition = viewHolder.getAdapterPosition();

            return false;
        });
    }

    @Override
    public int getItemCount() {
        return flashcardCollections.size();
    }

    public int GetPosition() {
        return currentPosition;
    }
}
