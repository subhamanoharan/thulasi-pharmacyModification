package com.thoughtworks.karthikintern.ThulasiShowcase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PullUpdate extends Service {
    public PullUpdate() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
