package com.burgerdelivery;

import android.app.FragmentManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.burgerdelivery.burgerlist.BurgerListFragment;
import com.burgerdelivery.orderitemlist.OrderItemListActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.flMainContent, BurgerListFragment.getInstance())
                    .commit();
        }

        getFragmentManager().addOnBackStackChangedListener(this);

        configureOrderNotificationUpdates();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_cart) {
            Intent intent = OrderItemListActivity.getIntent(this);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void onBackStackChanged() {
        checkShouldDisplayBackButton();
    }

    private void checkShouldDisplayBackButton() {
        boolean shouldDisplayBackButton = getFragmentManager().getBackStackEntryCount() > 0;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(shouldDisplayBackButton);
        }
    }

    private void configureOrderNotificationUpdates() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        // Create channel to show notifications about the order updates.
        String channelId  = getString(R.string.default_notification_channel_id);
        String channelName = getString(R.string.default_notification_channel_name);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }
    }
}
