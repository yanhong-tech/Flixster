package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.flixster.DetailActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;


public class PopularMovieAdapter extends RecyclerView.Adapter<PopularMovieAdapter.ViewHolder>{
    //variable for popular movie
    private static final int POPULAR = 1;
    private static final int ORDINARY = 0;

    //where this adapter come from: context
    Context context;
    //The actual data that the adapter will hold on to
    List<Movie> movies;

    //the two variable will get passed in from the construct
    public PopularMovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        /**
         * popular movie create the movie view include the popular movie
         */
        View movieView;
        //on the portrait mode, get the popular view
        if (viewType == POPULAR && context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            movieView = LayoutInflater.from(context).inflate(R.layout.item_movie_popular, parent,false);
            return new PopularMovieAdapter.ViewHolderPopular(movieView);
        } else {
            movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
            return new PopularMovieAdapter.ViewHolder(movieView);
        }
    }

    //Involves populating data into the item through holder(take the data at the position and put in the view holder)
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        /**
         * popular movie: bind the popular view together
         * */
        Movie movie = movies.get(position);
        if (holder.getItemViewType() == ORDINARY || context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            holder.bind(movie);
        } else {
            PopularMovieAdapter.ViewHolderPopular holderPopular = (PopularMovieAdapter.ViewHolderPopular) holder;
            holderPopular.bind_popular(movie);
        }
    }

    //get the view type
    @Override
    public int getItemViewType(int position) {
        if (movies.get(position).getVoteAverage() <= 5) {
            return ORDINARY;
        } else {
            return POPULAR;
        }
    }

    //return the total count of item in the list.
    @Override
    public int getItemCount() {
        return movies.size();
    }

    /**
     * View holder started from here
     * **/
    //viewHolder is the represent the row of the recycle view.
    public class ViewHolder extends RecyclerView.ViewHolder {
        //define the each view in the view holder
        RelativeLayout container;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            //get the each item from the layout xml.
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(final Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;
            //if phone is landscape
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //then imageUrl = backDrop url
                imageUrl = movie.getBackdropPath();
            } else {
                imageUrl = movie.getPosterPath();
            }

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(18)).placeholder(R.drawable.ic_launcher_background);
            Glide.with(context).load(imageUrl).apply(requestOptions).into(ivPoster);

            //1. register click to a new activity on tap
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //2. navigate to a new activity on tab
                    //use intent to navigate the other activity
                    Intent i = new Intent(context, DetailActivity.class);
                    //parcel/pass the whole movie object to the other activity
                    i.putExtra("movie", Parcels.wrap(movie));
                    // ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, (View)ivPoster, "profile");
                    context.startActivity(i);
                }
            });
        }
    }

    /**
     * popular movie: create another viewHolder for the popular movie
     * */
    public class ViewHolderPopular extends PopularMovieAdapter.ViewHolder {
        ImageView ivPoster;

        public ViewHolderPopular(@NonNull View itemView) {
            super(itemView);
            this.ivPoster = itemView.findViewById(R.id.ivPoster);
        }

        public void bind_popular(final Movie movie) {
            Glide.with(context).load(movie.getBackdropPath()).into(ivPoster);

            ivPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(intent);
                }
            });
        }
    }
}
