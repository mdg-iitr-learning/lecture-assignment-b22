package com.amishgarg.wartube.Adapter;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amishgarg.wartube.PicassoUtil;
import com.amishgarg.wartube.Model.Post;
import com.amishgarg.wartube.R;
import com.amishgarg.wartube.TimeDisplay;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class PostsRecyclerAdapter extends FirebaseRecyclerAdapter<Post, RecyclerView.ViewHolder> {


    String postKey;
    ProgressBar progressBar;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PostsRecyclerAdapter(@NonNull FirebaseRecyclerOptions<Post> options, ProgressBar progressBar) {
        super(options);
        this.progressBar =progressBar;
    }


    public static class TextTypeViewHolder extends RecyclerView.ViewHolder {

        ImageView authorIcon;
        TextView authorName;
        TextView timestamp;
        TextView postText;
        TextView postNumLikes;
        TextView postNumComments;

        LinearLayout baseLayout;

        public TextTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            authorIcon = itemView.findViewById(R.id.post_author_icon);
            authorName = itemView.findViewById(R.id.post_author_name);
            timestamp = itemView.findViewById(R.id.post_timestamp);
            postText = itemView.findViewById(R.id.post_text);
            postNumLikes = itemView.findViewById(R.id.post_num_likes);
            postNumComments = itemView.findViewById(R.id.post_num_com);

            baseLayout = itemView.findViewById(R.id.base_layout);
        }
    }

    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        ImageView authorIcon;
        TextView authorName;
        TextView timestamp;
        ImageView postPhoto;
        TextView postText;
        TextView postNumLikes;
        TextView postNumComments;

        LinearLayout baseLayout;

        public ImageTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            authorIcon = itemView.findViewById(R.id.post_author_icon);
            authorName = itemView.findViewById(R.id.post_author_name);
            timestamp = itemView.findViewById(R.id.post_timestamp);
            postPhoto = itemView.findViewById(R.id.post_photo);
            postText = itemView.findViewById(R.id.post_text);
            postNumLikes = itemView.findViewById(R.id.post_num_likes);
            postNumComments = itemView.findViewById(R.id.post_num_com);

            baseLayout = itemView.findViewById(R.id.base_layout);
        }



    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case Post.TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_text_item, parent, false);
              progressBar.setVisibility(View.GONE);
                return new TextTypeViewHolder(view);
            case Post.IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_item, parent, false);
             progressBar.setVisibility(View.GONE);
                return new ImageTypeViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (getItem(position).type) {
            case 0:
                return Post.TEXT_TYPE;
            case 1:
                return Post.IMAGE_TYPE;
            default:
                return -1;
        }
    }


    @Override
    protected void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int i, @NonNull final Post post) {



        if (post != null) {
            switch (post.type) {

                case Post.TEXT_TYPE:
                    ((TextTypeViewHolder) holder).authorName.setText(post.getAuthor().getDisplay_name());
                    ((TextTypeViewHolder) holder).postText.setText(post.getText());
                    ((TextTypeViewHolder) holder).timestamp.setText(new TimeDisplay((Long)post.getTimestamp()).getTimeDisplay());
                    PicassoUtil.loadImagePicasso(post.getAuthor().getProfile_pic(), ((TextTypeViewHolder) holder).authorIcon);
                    ((TextTypeViewHolder)holder).postNumLikes.setText(post.getLikes()+"");
                    ((TextTypeViewHolder)holder).postNumComments.setText(post.getComments()+"");


                    ((TextTypeViewHolder)holder).baseLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle args = new Bundle();
                            postKey = post.getAuthor().getUid()+post.getTimestamp();
                            args.putString("postKey", postKey);
                            Navigation.findNavController(v).navigate(R.id.post_detail_dest, args);
                        }
                    });
                    break;
                case Post.IMAGE_TYPE:

                    ((ImageTypeViewHolder) holder).authorName.setText(post.getAuthor().getDisplay_name());
                    ((ImageTypeViewHolder) holder).postText.setText(post.getText());
                    ((ImageTypeViewHolder) holder).timestamp.setText(new TimeDisplay((Long)post.getTimestamp()).getTimeDisplay());
                    PicassoUtil.loadImagePicasso(post.getAuthor().getProfile_pic(), ((ImageTypeViewHolder) holder).authorIcon);
                    PicassoUtil.loadImagePicasso(post.getFull__url(), ((ImageTypeViewHolder) holder).postPhoto);
                    ((ImageTypeViewHolder)holder).postNumLikes.setText(post.getLikes() +"");
                    ((ImageTypeViewHolder)holder).postNumComments.setText(String.valueOf(post.getComments() +""));
                    ((ImageTypeViewHolder)holder).baseLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle args = new Bundle();
                            postKey = post.getAuthor().getUid()+post.getTimestamp();
                            args.putString("postKey", postKey);
                            Navigation.findNavController(v).navigate(R.id.post_detail_dest, args);
                        }
                    });
                    break;

            }

        }
    }

}
