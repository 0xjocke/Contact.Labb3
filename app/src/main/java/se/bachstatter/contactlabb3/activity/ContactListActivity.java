package se.bachstatter.contactlabb3.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import se.bachstatter.contactlabb3.R;
import se.bachstatter.contactlabb3.fragments.ContactDetailFragment;
import se.bachstatter.contactlabb3.fragments.ContactListFragment;
import se.bachstatter.contactlabb3.models.Contact;

public class ContactListActivity extends Activity implements
        ContactListFragment.Callbacks {
    /**
     * Variable for checking if two pane layout is active.
     */
    private boolean mTwoPane;
    /**
     * Request and Code Constants
     */
    public static final String CONTACT_POSITION_CODE = "item_position";
    public static final String MODE_CODE = "mode";
    public static String REQUEST_CODE = "requestCode";
    public static String CONTACT_CODE = "contact";
    public static final int NEW_CONTACT_REQUEST_CODE = 123 ;
    private static final int DETAIL_REQUEST_CODE = 234;
    public static final int REMOVE_CODE = 999;
    public static final int EDIT_CODE = 998;
    public static final int LANDSCAPE_CODE = 857;

    /**
     * Check if contact detail is not null/ visible then set mTwoPane to true.
     * It will be visible is screen is larger than 500dp
     * Either case run ContactListFragment setActivateOnItemClick function.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        if (findViewById(R.id.contact_detail_container) != null) {
            mTwoPane = true;
        }
    }

    /**
     * We can use this method cause we implements ContactListFragment.Callbacks
     * When the ListFragments gets a click onListItemClick will run this function and send the position.
     *
     * if mTwoPane is true:
     * Create new Bundle named arguments,
     * save the position with putString
     * Create a new DetailFragment and call setArgument with the bundle.
     * Then call fragmentmanager, begintransaction and replace.
     * We call replace cause we want to show the current position.
     * And finally call commit()
     *
     * Else create a new detailIntent
     * and send position with put extra
     *
     * @param position
     */
    public void onItemSelected(int position) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putInt(CONTACT_POSITION_CODE, position);
            ContactDetailFragment fragment = new ContactDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.contact_detail_container, fragment).commit();
        } else {
            Intent detailIntent = new Intent(this, ContactDetailActivity.class);
            detailIntent.putExtra(CONTACT_POSITION_CODE, position);
            startActivityForResult(detailIntent, DETAIL_REQUEST_CODE);
        }
    }

    /**
     * Inflate the menu layout with the actionbar menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * If user clicks on add contact start a new EditContactActivity
     * Send requestCode NEW_CONTACT_REQUEST_CODE with put extra.
     * and startActivityForResult.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addContact) {
            Intent addContactIntent = new Intent(this, EditContactActivity.class);
            addContactIntent.putExtra(REQUEST_CODE, NEW_CONTACT_REQUEST_CODE);
            startActivityForResult(addContactIntent, NEW_CONTACT_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * If requestCode is NEW_CONTACT_REQUEST_CODE and ResultCode == Result_OK
     * Get the parcable contact and put it in to the staticContactList
     * Then notify the adapter
     *
     * If the requestcode is DETAIL_REQUEST_CODE and  ResultCode == Result_OK
     * Check the MODE_CODE sent with putExtra
     *
     * If mode is REMOVE_CODE: get position with putExtra,remove contact from list and notify adapter
     *
     * If mode is EDIT_CODE: run set on contactList. get position, and contact from putExtra
     * and notify the adapter.
     *
     *  If mode is LAND_SCAPE:
     * Create new Bundle named arguments,
     * save the position with putString
     * Create a new DetailFragment and call setArgument with the bundle.
     * Then call fragmentmanager, begintransaction and replace.
     * We call replace cause we want to show the current position.
     * And finally call commit()
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_CONTACT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Contact.staticContactList.add((Contact) data.getParcelableExtra(CONTACT_CODE));
                ContactListFragment.contactAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == DETAIL_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                switch (data.getIntExtra(MODE_CODE, 0)){
                    case REMOVE_CODE:
                        Contact.staticContactList.remove(data.getIntExtra(CONTACT_POSITION_CODE, 0));
                        ContactListFragment.contactAdapter.notifyDataSetChanged();
                        break;
                    case EDIT_CODE:
                        Contact.staticContactList.set(
                                data.getIntExtra(CONTACT_POSITION_CODE, 0),
                                (Contact) data.getParcelableExtra(CONTACT_CODE));
                        ContactListFragment.contactAdapter.notifyDataSetChanged();
                        break;
                    case LANDSCAPE_CODE:
                        Bundle arguments = new Bundle();
                        arguments.putInt(CONTACT_POSITION_CODE, data.getIntExtra(CONTACT_POSITION_CODE,0));
                        ContactDetailFragment fragment = new ContactDetailFragment();
                        fragment.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.contact_detail_container, fragment).commit();
                        break;
                }
            }
        }
    }
}