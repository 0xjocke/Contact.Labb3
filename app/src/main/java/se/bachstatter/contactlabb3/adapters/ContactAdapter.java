package se.bachstatter.contactlabb3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import se.bachstatter.contactlabb3.R;
import se.bachstatter.contactlabb3.models.Contact;


/**
 * Created by Jocek on 2014-09-14.
 */
public class ContactAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<Contact> contactList;

    /**
     * Constructor that takes context and an Arraylist and sets it to class variables
     * @param context
     * @param contactList
     */
    public ContactAdapter(Context context, ArrayList contactList) {
        super(context, R.layout.list_row_contact, contactList);

        this.context = context;
        this.contactList = contactList;
    }

    /**
     * getView get called for every row
     * Initialize inner Class ViewHolder
     * if converView is null we need to create a new view.
     * The LayoutInflater takes the chosen layout XML-file and creates View-objects from its contents.
     * Then I make a new instance on ViewHolder and saves my progressbar and
     * text- and imagesviews in class variables and finally sets a tag on the viewHolder.
     * else if convertView not is null we dont have to create a new.
     * we get our viewHolder by calling getTag on convertView (and casting it)
     *
     * Now we can get our contact for this specific row and by calling get on contactList with our position.
     * We get our contact name by getName() and puts it to our TextView which we access with help from ViewHolder.
     * We also get our imageView from viewHolder. and our imgUrl with getUrl.
     * Then Picasso helps us get the image from internet, convert it to a bitmap and puts it to the imageview.
     * Picasso also helps with caching, display image on error and hiding the progressbar.
     * Both onSuccess and onError we hide the progressBar and show the imageView.
     * if succuess the image from our url will be show on error the default person image.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_row_contact, parent, false);

                viewHolder = new ViewHolder();

                viewHolder.nameHolder = (TextView) convertView.findViewById(R.id.textViewContact);
                viewHolder.imageHolder = (ImageView) convertView.findViewById(R.id.imageViewContact);
                viewHolder.progressBarHolder = (ProgressBar) convertView.findViewById(R.id.progressBar);

                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder)convertView.getTag();
            }

            Contact contact = contactList.get(position);
            viewHolder.nameHolder.setText(contact.getName());
            Picasso.with(context)
                    .load(contact.getImgUrl())
                    .error(R.drawable.person)
                    .into(viewHolder.imageHolder, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            viewHolder.progressBarHolder.setVisibility(View.GONE);
                            viewHolder.imageHolder.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onError() {
                            viewHolder.progressBarHolder.setVisibility(View.GONE);
                            viewHolder.imageHolder.setVisibility(View.VISIBLE);
                        }
                    });
        return convertView;
    }

    /**
     * ViewHolder is used to store Progressbar, Text- and imageView
     */
    public static class ViewHolder{
        public TextView nameHolder;
        public ImageView imageHolder;
        public ProgressBar progressBarHolder;
    }
}