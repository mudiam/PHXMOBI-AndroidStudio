package com.parse.parsedevday.view;

import com.parse.ParseImageView;
import com.phxmobi.phxmobifestival.R;
import com.parse.parsedevday.model.Favorites;
import com.parse.parsedevday.model.Talk;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * An ArrayAdapter to handle a list of Talks.
 */
public class TalkListAdapter extends ArrayAdapter<Talk> {
  public TalkListAdapter(Context context) {
    super(context, 0);
  }

  @Override
  public View getView(int position, View view, ViewGroup parent) {
    // This is all pretty standard code for setting up an Android view.
    

    view = View.inflate(getContext(), R.layout.list_item_talk, null);

    final Talk talk = getItem(position);

    TextView startDateView = (TextView) view.findViewById(R.id.start_date);
    startDateView.setText(DateFormat.getTimeFormat(getContext()).format(
        talk.getSlot().getStartTime()));

//    TextView endDateView = (TextView) view.findViewById(R.id.end_date);
//    endDateView.setText(DateFormat.getTimeFormat(getContext()).format(talk.getSlot().getEndTime()));

      ParseImageView photo = (ParseImageView) view.findViewById(R.id.photo);

      if (talk.isAlwaysFavorite()){
          if (talk.getIcon()!=null) {
              photo.setParseFile(talk.getIcon());
              photo.loadInBackground();
          }else{
              photo.setVisibility(View.GONE);
          }
      }else if (talk.getSpeakers() !=null && talk.getSpeakers().size() != 0) {
          photo.setParseFile(talk.getSpeakers().get(0).getPhoto());
          photo.loadInBackground();
      }


    TextView titleView = (TextView) view.findViewById(R.id.title);
    TextView roomView = (TextView) view.findViewById(R.id.room);
    titleView.setText(talk.getTitle());
    roomView.setText(talk.getRoom().getName());

    final ImageButton favoriteButton = (ImageButton) view.findViewById(R.id.favorite_button);
    if (Favorites.get().contains(talk)) {
      favoriteButton.setImageResource(R.drawable.light_rating_important);
    } else {
      favoriteButton.setImageResource(R.drawable.light_rating_not_important);
    }
    favoriteButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        Favorites favorites = Favorites.get();
        if (favorites.contains(talk)) {
          favorites.remove(talk);
          favoriteButton.setImageResource(R.drawable.light_rating_not_important);
        } else {
          favorites.add(talk);
          favoriteButton.setImageResource(R.drawable.light_rating_important);
        }
        favorites.save(getContext());
      }
    });
    favoriteButton.setFocusable(false);
    
    if (talk.isAlwaysFavorite()) {
      favoriteButton.setVisibility(View.GONE);
    } else {
      favoriteButton.setVisibility(View.VISIBLE);
    }
    
    if (talk.isAlwaysFavorite()) {
      view.setBackgroundColor(Color.rgb(255, 255, 245));
      view.findViewById(R.id.photo_circle_bg).setBackgroundResource(R.drawable.circlefav);
    } else {
      view.setBackgroundColor(Color.rgb(245, 245, 245));
    }
    
    return view;
  }
}
