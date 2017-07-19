package biemann.android.snatchchallenge;

import android.app.Application;

import biemann.android.snatchchallenge.data.di.ApplicationModule;
import biemann.android.snatchchallenge.data.di.DaggerNetworkComponent;
import biemann.android.snatchchallenge.data.di.NetworkComponent;
import biemann.android.snatchchallenge.data.di.NetworkModule;

/**
 * Created by Alexander on 7/19/2017.
 */
public class MainApplication extends Application
{
    private NetworkComponent networkComponent;

    @Override
    public void onCreate()
    {
        super.onCreate();
        networkComponent = DaggerNetworkComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule(BuildConfig.BASEURL))
                .build();
    }

    public NetworkComponent getNetworkComponent()
    {
        return networkComponent;
    }
}
