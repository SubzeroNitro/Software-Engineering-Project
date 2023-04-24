package com.softwareengineeringproject.studymonster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputFilter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softwareengineeringproject.studymonster.data.AppDatabase;
import com.softwareengineeringproject.studymonster.data.FlashcardCollectionDAO;
import com.softwareengineeringproject.studymonster.data.FlashcardCollectionEntity;
import com.softwareengineeringproject.studymonster.data.FlashcardDAO;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardFragment extends Fragment {
    private FragmentActivity activity;
    private AppDatabase database;
    private FlashcardCollectionDAO flashcardCollectionDAO;
    private List<FlashcardCollectionEntity> flashcardCollections;
    private CollectionsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        activity.setTitle(R.string.app_dashboard);

        database = AppDatabase.GetInstance(activity);
        flashcardCollectionDAO = database.FlashcardCollectionDAO();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_dashboard, container, false);

        RecyclerView recyclerView = layout.findViewById(R.id.dashboard_fragment_flashcard_collection_container);

        Executors.newSingleThreadExecutor().execute(() -> {
            flashcardCollections = flashcardCollectionDAO.GetFlashcardCollections();
             adapter = new CollectionsAdapter(activity, flashcardCollections);

            activity.runOnUiThread(() -> {
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            });
        });

        FloatingActionButton addFlashcardCollectionButton = layout.findViewById(R.id.dashboard_fragment_add_flashcard_collection);
        addFlashcardCollectionButton.setOnClickListener(view -> AddFlashcardCollection());

        return layout;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.context_menu_edit:
                EditCollection(adapter.GetPosition());
                return true;

            case R.id.context_menu_delete:
                DeleteCollection(adapter.GetPosition());
                return true;

            default:
                return super.onContextItemSelected(menuItem);
        }
    }

    public void AddFlashcardCollection() {
        EditText collectionName = new EditText(activity);
        collectionName.setHint(R.string.dashboard_fragment_collection_name_hint);
        collectionName.setSingleLine();

        final int maxLength = 40;
        collectionName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        alertDialog.setTitle(R.string.dashboard_fragment_add_collection_dialog_title);
        alertDialog.setView(collectionName);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton(R.string.create_button, null);
        alertDialog.setNegativeButton(R.string.cancel_button, (dialog, which) -> dialog.cancel());

        AlertDialog dialog = alertDialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(dialogView -> {
            String name = collectionName.getText().toString();

            if (name.trim().isEmpty()) {
                collectionName.setError("Invalid Collection Name");
                return;
            }

            FlashcardCollectionEntity flashcardCollection = new FlashcardCollectionEntity();
            flashcardCollection.name = name;

            Executors.newSingleThreadExecutor().execute(() -> {
                FlashcardCollectionEntity result = flashcardCollectionDAO.GetFlashcardCollectionByName(name);

                if (result != null) {
                    activity.runOnUiThread(() -> {
                        collectionName.setError("Collection Already Exists");
                    });

                    return;
                }

                flashcardCollectionDAO.InsertFlashcardCollection(flashcardCollection);
                flashcardCollections.add(flashcardCollection);

                activity.runOnUiThread(() -> {
                    adapter.notifyItemInserted(adapter.getItemCount() - 1);
                });

                dialog.dismiss();
            });
        });
    }

    void EditCollection(int position) {
        EditText collectionName = new EditText(activity);
        collectionName.setHint(R.string.dashboard_fragment_collection_name_hint);
        collectionName.setSingleLine();

        final int maxLength = 40;
        collectionName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        alertDialog.setTitle(R.string.dashboard_fragment_edit_collection_dialog_title);
        alertDialog.setView(collectionName);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton(R.string.context_menu_edit, null);
        alertDialog.setNegativeButton(R.string.cancel_button, (dialog, which) -> dialog.cancel());

        AlertDialog dialog = alertDialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(dialogView -> {
            String name = collectionName.getText().toString();

            if (name.trim().isEmpty()) {
                collectionName.setError("Invalid Collection Name");
                return;
            }

            FlashcardCollectionEntity flashcardCollection = flashcardCollections.get(position);
            flashcardCollection.name = name;

            flashcardCollections.set(position, flashcardCollection);
            adapter.notifyItemChanged(position);

            Executors.newSingleThreadExecutor().execute(() -> flashcardCollectionDAO.UpdateFlashcardCollection(flashcardCollection));

            dialog.dismiss();
        });
    }

    void DeleteCollection(int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        alertDialog.setTitle(R.string.dashboard_fragment_delete_collection_dialog_title);
        alertDialog.setMessage(R.string.dashboard_fragment_delete_collection_dialog_description);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton(R.string.context_menu_delete, null);
        alertDialog.setNegativeButton(R.string.cancel_button, (dialog, which) -> dialog.cancel());

        AlertDialog dialog = alertDialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(dialogView -> {
            FlashcardCollectionEntity flashcardCollection = flashcardCollections.get(position);

            flashcardCollections.remove(position);
            adapter.notifyItemRemoved(position);

            Executors.newSingleThreadExecutor().execute(() -> {
                flashcardCollectionDAO.DeleteFlashcardCollection(flashcardCollection.id);
                database.FlashcardDAO().DeleteFlashcardByCollectionId(flashcardCollection.id);
            });

            dialog.dismiss();
        });
    }
}