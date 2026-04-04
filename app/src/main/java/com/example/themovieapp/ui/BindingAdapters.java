package com.example.themovieapp.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import coil.Coil;
import coil.request.ImageRequest;
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

    /** Pôster por URL (Coil); em Java para o Data Binding reconhecer o adapter. */
    @BindingAdapter("posterLoadUrl")
    public static void setPosterLoadUrl(ImageView view, @Nullable String url) {
        ColorDrawable placeholder = new ColorDrawable(Color.DKGRAY);
        ImageRequest request = new ImageRequest.Builder(view.getContext())
                .data(url)
                .crossfade(true)
                .placeholder(placeholder)
                .error(placeholder)
                .target(view)
                .build();
        Coil.imageLoader(view.getContext()).enqueue(request);
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
