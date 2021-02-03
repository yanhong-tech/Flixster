package com.example.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    //variable for popular movie
    private static final int POPULAR = 1;
    private static final int ORDINARY = 0;

    //where this adapter come from: context
    Context context;
    //The actual data that the adapter will hold on to
    List<Movie> movies;

    //the two variable will get passed in from the construct
    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    //Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //create the movie view
        //View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        //return new ViewHolder(movieView);

        /**
         * create the movie view include the popular movie
         */
        View movieView;
        //on the portrait mode, get the popular view
        if (viewType == POPULAR && context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            movieView = LayoutInflater.from(context).inflate(R.layout.item_movie_popular, parent,false);
            return new MovieAdapter.ViewHolderPopular(movieView);
        } else {
            movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
            return new MovieAdapter.ViewHolder(movieView);
        }
    }

    //Involves populating data into the item through holder(take the data at the position and put in the view holder)
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //get the movie at the passed in position
        //Movie movie = movies.get(position);
        //bind the movie data into the viewHolder
        //holder.bind(movie);

        /**
         * bind the popular view together
        * */
        Movie movie = movies.get(position);
        if (holder.getItemViewType() == ORDINARY || context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            holder.bind(movie);
        } else {
            MovieAdapter.ViewHolderPopular holderPopular = (MovieAdapter.ViewHolderPopular) holder;
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
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            //get the each item from the layout xml.
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
        }

        public void bind(Movie movie) {
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
            Glide.with(context).load(imageUrl).into(ivPoster);
        }
    }

    //create another viewHolder for the popular movie
    public class ViewHolderPopular extends MovieAdapter.ViewHolder {
        ImageView ivPoster;

        public ViewHolderPopular(@NonNull View itemView) {
            super(itemView);
            this.ivPoster = itemView.findViewById(R.id.ivPoster);
        }

        public void bind_popular(final Movie movie) {
            Glide.with(context).load(movie.getBackdropPath()).into(ivPoster);
        }
    }
}
