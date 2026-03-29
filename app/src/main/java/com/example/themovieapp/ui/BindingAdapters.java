package com.example.themovieapp.ui;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.themovieapp.data.DataState;
import com.example.themovieapp.model.Movie;
import com.example.themovieapp.ui.list.MovieListAdapter;
import java.util.List;

public final class BindingAdapters {

    private BindingAdapters() {
    }

    @BindingAdapter("imageResource")
    public static void setImageResourceBinding(ImageView view, @Nullable Integer resId) {
        if (resId != null && resId != 0) {
            view.setImageResource(resId);
        }
    }

    @BindingAdapter("submitMovieList")
    public static void setSubmitMovieList(RecyclerView recyclerView, @Nullable List<Movie> items) {
        RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
        if (adapter instanceof MovieListAdapter) {
            ((MovieListAdapter) adapter).submitList(items != null ? items : java.util.Collections.emptyList());
        }
    }

    @BindingAdapter("goneUnlessLoading")
    public static void goneUnlessLoading(View view, @Nullable DataState.State state) {
        view.setVisibility(state == DataState.State.Loading ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("goneUnlessSuccess")
    public static void goneUnlessSuccess(View view, @Nullable DataState.State state) {
        view.setVisibility(state == DataState.State.Success ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("goneUnlessError")
    public static void goneUnlessError(View view, @Nullable DataState.State state) {
        view.setVisibility(state == DataState.State.Error ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("detailShowWhenReady")
    public static void detailShowWhenReady(View view, @Nullable DataState.State state) {
        view.setVisibility(state == DataState.State.Success ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("detailLoadingInProgress")
    public static void detailLoadingInProgress(View view, @Nullable DataState.State state) {
        view.setVisibility(state == DataState.State.Loading ? View.VISIBLE : View.GONE);
    }
}
