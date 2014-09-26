package se.bachstatter.contactlabb3.fragments;

/**
 * Created by Jocek on 2014-09-17.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import se.bachstatter.contactlabb3.R;
import se.bachstatter.contactlabb3.adapters.ContactAdapter;
import se.bachstatter.contactlabb3.models.Contact;

import static se.bachstatter.contactlabb3.activity.ContactListActivity.CONTACT_POSITION_CODE;
import static se.bachstatter.contactlabb3.models.Contact.staticContactList;

public class ContactListFragment extends ListFragment implements AdapterView.OnItemLongClickListener, DialogInterface.OnClickListener {
    public static ContactAdapter contactAdapter;
    private static final int FIRST_ITEM = 0;
    private Callbacks mCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    int currentPosForDialog;

    /**
     * Create an interface that will be used to call parent activity
     * OnItemSelected with teh chosen position.
     */
    public interface Callbacks {
        public void onItemSelected(int position);
    }

    /**
     * Create a static contact list and save it to Contacs staticContactList variable.
     * It needs to be static since we will use it in static callback function.
     * Add some content.
     * Create a new Contactadapter with the staticContactList
     * then run setListadapter with the contactAdpater
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(staticContactList == null) {
            staticContactList = new ArrayList<Contact>();
            staticContactList.add(new Contact("http://developer.fortnox.se/wp-content/uploads/2014/03/nakleh_jocke.jpg", "Jocke", "trevligt typ", 25));
            staticContactList.add(new Contact("https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-xpa1/v/t1.0-9/10325400_10152110180717896_2268770918259889556_n.jpg?oh=f56b8b4f0425573484f23f30f441f7fc&oe=5496A104&__gda__=1418816786_2ff62edef154eb1e3df7b0f23bacfe13", "Linn Sandström", "En snäll pys", 25));
            staticContactList.add(new Contact("http://developer.fortnox.se/wp-content/uploads/2014/03/nakleh_jocke.jpg", "Jocke", "trevligt typ", 25));
        }
        contactAdapter = new ContactAdapter(getActivity(), staticContactList);
        setListAdapter(contactAdapter);
    }

    /**
     * If savedInstance is null and we are in landscape: show first item
     *
     * if savedInstance is null and it containsKey STATE_ACTIVATED_POSITION:
     * setActivatedPosition to STATE_ACTIVATED_POSITION
     * run setLongClickListener
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View detailsFrame = getActivity().findViewById(R.id.contact_detail_container);
        if (savedInstanceState==null &&  getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE){
            mActivatedPosition = FIRST_ITEM;
            mCallbacks.onItemSelected(FIRST_ITEM);
        }
        setLongClickListener();
        if (savedInstanceState != null && savedInstanceState.containsKey(CONTACT_POSITION_CODE)) {
            setActivatedPosition(savedInstanceState
                    .getInt(CONTACT_POSITION_CODE));
        }
    }

    /**
     * Sets OnItemLongClickListener on listview.
     *
     */
    private void setLongClickListener() {
        ListView listView = getListView();
        listView.setOnItemLongClickListener(this);
    }

    /**
     * Check that parent activity implements Callbacks else throw error.
     * then sets mCallsback to the parents activitys callback.
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    /**
     * OnDetach set mCallback to null since the event wont be relevant anymore and can cause
     * unexpected problems.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * On ListItemClick send the position to parent activity with the help of our callback interface.
     * @param listView
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onListItemClick(ListView listView, View view, int position,
                                long id) {
        super.onListItemClick(listView, view, position, id);
        mActivatedPosition = position;
        mCallbacks.onItemSelected(position);
    }

    /**
     * if mActivated not is Invalid
     * give outstate mActivatedPosition with putInt
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(CONTACT_POSITION_CODE, mActivatedPosition);
        }
        super.onSaveInstanceState(outState);

    }

    /**
     * This sets the choicemode to CHOICE_MODE_SINGLE.
     * By setting the choiceMode to CHOICE_MODE_SINGLE, the
     * List allows up to one item to  be in a chosen state
     * we also call the parents activities onItemSelected and send it 0 so that the first
     * item shows when we startup the app.
     *
     */
    public void setActivateOnItemClick(int position) {
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    /**
     * Sets the checked state of ListView the specified position.
     * Also saves teh position to mActivatedPosition
     * @param position
     */
    private void setActivatedPosition(int position) {
        getListView().setItemChecked(position, true);
        mActivatedPosition = position;
    }

    /**
     * On item long click
     * save posiotion to current position.
     * Create a new alertdialog with AlertDialog builder.
     * set message, title, positivebtn and negative. btn.
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        currentPosForDialog = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.ok, this);
        builder.setNegativeButton(R.string.cancel, this);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    /**
     * Onclick on positivebtn remove the cosen contact and notify the adapter
     * on both negative and positive click dismiss the dialog.
     * @param dialog
     * @param which
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == AlertDialog.BUTTON_POSITIVE){
            staticContactList.remove(currentPosForDialog);
            contactAdapter.notifyDataSetChanged();
        }
        dialog.dismiss();
    }
}