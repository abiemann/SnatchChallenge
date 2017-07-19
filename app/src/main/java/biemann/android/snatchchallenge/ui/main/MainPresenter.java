package biemann.android.snatchchallenge.ui.main;

import android.location.Address;
import android.location.Location;

import javax.inject.Inject;

import biemann.android.snatchchallenge.data.api.MediawikiGeosearchApi;
import biemann.android.snatchchallenge.data.api.MediawikiGeosearchModel;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Alexander on 7/19/2017.
 */

public class MainPresenter implements MainContract.Presenter
{
    private Retrofit retrofit;
    private MainContract.View view;
    private boolean apiRequestInProgress;


    @Inject
    public MainPresenter(Retrofit retrofit, MainContract.View view)
    {
        this.retrofit = retrofit;
        this.view = view;
    }

    //
    // --- MainContract.Presenter Implementation ---
    //

    @Override
    public void loadGeosearchData(Integer range, Double lat, Double lon)
    {
        if (!apiRequestInProgress)
        {
            apiRequestInProgress = true;
            final String coordinates = lat + "|" + lon;

            retrofit.create(MediawikiGeosearchApi.class)
                    .getGeosearchFromApi("query", "geosearch", range, coordinates, "json")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Observer<MediawikiGeosearchModel>()
                    {
                        @Override
                        public void onCompleted()
                        {
                            view.showComplete();
                            apiRequestInProgress = false;
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            view.showError(e.getMessage());
                            apiRequestInProgress = false;
                        }

                        @Override
                        public void onNext(MediawikiGeosearchModel mediawikiGeosearchModel)
                        {
                            view.showList(mediawikiGeosearchModel.getQuery().getGeosearch());
                        }
                    });
        }
    }


    @Override
    public void onLocationUpdate(Location location)
    {
        loadGeosearchData(10000, location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onAddressUpdate(Address address)
    {

    }

    @Override
    public void onLocationSettingsUnsuccessful()
    {
        view.showGpsError();
    }
}
