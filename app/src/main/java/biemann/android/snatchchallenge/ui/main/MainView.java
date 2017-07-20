package biemann.android.snatchchallenge.ui.main;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.List;

import javax.inject.Inject;

import biemann.android.snatchchallenge.BuildConfig;
import biemann.android.snatchchallenge.MainApplication;
import biemann.android.snatchchallenge.R;
import biemann.android.snatchchallenge.data.api.MediawikiGeosearchModel;
import biemann.android.snatchchallenge.ui.base.BaseActivity;
import biemann.android.snatchchallenge.ui.main.di.DaggerMainComponent;
import biemann.android.snatchchallenge.ui.main.di.MainModule;
import biemann.android.snatchchallenge.utils.Utilities;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

import static biemann.android.snatchchallenge.utils.Utilities.unsubscribe;

/**
 * Created by Alexander on 7/19/2017.
 */

public class MainView extends BaseActivity implements MainContract.View, ListInteractionListener
{
    private final static int REQUEST_CHECK_SETTINGS = 0;

    private ListRecyclerViewAdapter adapter;
    private ReactiveLocationProvider locationProvider;
    private Observable<Location> lastKnownLocationObservable;
    private Observable<Location> locationUpdatesObservable;
    private Subscription lastKnownLocationSubscription;
    private Subscription updatableLocationSubscription;

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.list)
    RecyclerView recyclerView;

    @Inject
    MainPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        DaggerMainComponent.builder()
                .networkComponent(((MainApplication) getApplicationContext()).getNetworkComponent())
                .mainModule(new MainModule(this))
                .build()
                .inject(this);

        adapter = new ListRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        initializeGps();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        unsubscribe(updatableLocationSubscription);
        unsubscribe(lastKnownLocationSubscription);
    }

    @SuppressWarnings({"MissingPermission"})
    private void initializeGps()
    {
        locationProvider = new ReactiveLocationProvider(getApplicationContext());
        lastKnownLocationObservable = locationProvider.getLastKnownLocation();

        final LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(5)
                .setInterval(100);

        locationUpdatesObservable = locationProvider
                .checkLocationSettings(
                        new LocationSettingsRequest.Builder()
                                .addLocationRequest(locationRequest)
                                .setAlwaysShow(true)  //Reference: http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
                                .build()
                )
                .doOnNext(new Action1<LocationSettingsResult>() {
                    @Override
                    public void call(LocationSettingsResult locationSettingsResult) {
                        Status status = locationSettingsResult.getStatus();
                        if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                            try {
                                status.startResolutionForResult(MainView.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException th) {
                                th.printStackTrace();
                            }
                        }
                    }
                })
                .flatMap(new Func1<LocationSettingsResult, Observable<Location>>() {
                    @Override
                    public Observable<Location> call(LocationSettingsResult locationSettingsResult) {
                        return locationProvider.getUpdatedLocation(locationRequest);
                    }
                });
    }


    //
    // --- MainContract.View Implementation ---

    @Override
    public void showList(List<MediawikiGeosearchModel.QueryListItem> listItems)
    {
        adapter.setItemList(listItems);
    }

    @Override
    public void showError(String message)
    {
        Snackbar snackbar = Snackbar.make(coordinator, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void showComplete()
    {
        // Something could be done here like a progress bar hidden - demo doesn't need it
    }


    //
    // --- ListInteractionListener Implementation ---

    @Override
    public void onListClick(MediawikiGeosearchModel.QueryListItem data)
    {
        // open the associated Wikipedia article in an external browser
        final String url = BuildConfig.WIKIPEDIAURL+data.getPageid();
        Utilities.shareUrl(this, url);
    }

    //
    // --- Permissions Handling ---

    @Override
    protected void onLocationPermissionGranted() {
        lastKnownLocationSubscription = lastKnownLocationObservable
                .subscribe(new Action1<Location>(){
                    @Override
                    public void call(Location location)
                    {
                        presenter.loadGeosearchData(BuildConfig.DEFAULT_GPS_RANGE, location);
                    }
                }, new ErrorHandler());

        updatableLocationSubscription = locationUpdatesObservable
                .subscribe(new Action1<Location>(){
                    @Override
                    public void call(Location location)
                    {
                        presenter.loadGeosearchData(BuildConfig.DEFAULT_GPS_RANGE, location);
                    }
                }, new ErrorHandler());
    }

    private class ErrorHandler implements Action1<Throwable>
    {
        @Override
        public void call(Throwable throwable)
        {
            Toast.makeText(MainView.this, "GPS Error occurred.", Toast.LENGTH_SHORT).show();
        }
    }
}
