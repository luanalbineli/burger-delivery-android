package com.burgerdelivery.firebase;

import com.burgerdelivery.BurgerDeliveryApplication;
import com.burgerdelivery.dagger.component.DaggerInjectorComponent;
import com.burgerdelivery.repository.BurgerRepository;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Inject;

import timber.log.Timber;

public class FirebaseMessagingEvent extends FirebaseMessagingService {

    @Inject
    BurgerRepository mBurgerRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerInjectorComponent.builder()
                .applicationComponent(BurgerDeliveryApplication.getApplicationComponent(getApplication()))
                .build()
                .inject(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.i("From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().isEmpty()) {
            Timber.w("A message was received, but data is empty");
            return;
        }

        if (!remoteMessage.getData().containsKey(KEY_SERVER_ID) || !remoteMessage.getData().containsKey(KEY_STATUS)) {
            Timber.w("A message was received, but the data is incomplete: " +
                    "\n\nremoteMessage.getData().containsKey(KEY_SERVER_ID): " + remoteMessage.getData().containsKey(KEY_SERVER_ID) +
                    "\n\nremoteMessage.getData().containsKey(KEY_STATUS): " + remoteMessage.getData().containsKey(KEY_STATUS));
            return;
        }

        int serverId = Integer.parseInt(remoteMessage.getData().get(KEY_SERVER_ID));
        int status = Integer.parseInt(remoteMessage.getData().get(KEY_STATUS));

        Timber.i("Notification: " + remoteMessage.getNotification());
        Timber.i("Received an update order status message - Status: " + status + " | Server Id: " + serverId);
/*        mBurgerRepository.updateOrderStatusByServerId(serverId, status)
                .subscribe(() -> Timber.i("Updated the order " + serverId + " to the status: " + status),
                        throwable -> Timber.e(throwable, "An error occurred while tried to update the order " + serverId + " to the status: " + status)
                );*/
    }

    private static final String KEY_SERVER_ID = "id";
    private static final String KEY_STATUS = "status";
}
