package biemann.android.snatchchallenge.ui.main;

import android.location.Location;

import javax.inject.Inject;

import biemann.android.snatchchallenge.BuildConfig;
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
   // private Retrofit retrofit;
    private MediawikiGeosearchApi mediawikiGeosearchApi;
    private MainContract.View view;
    private boolean apiRequestInProgress;
    private long lastKnownApiRequestTime;


    @Inject
    public MainPresenter(MediawikiGeosearchApi mediawikiGeosearchApi, MainContract.View view)
    {
        this.mediawikiGeosearchApi = mediawikiGeosearchApi;
        this.view = view;
    }

    //
    // --- MainContract.Presenter Implementation ---
    //

    /**
     * triggers the API request
     * @param range
     * @param loc
     * @return true after API is requested, otherwise false
     */
    @Override
    public boolean loadGeosearchData(Integer range, Location loc)
    {
        if (!apiRequestInProgress)
        {
            // prevent too many requests within a timeframe
            if (System.currentTimeMillis() - lastKnownApiRequestTime > BuildConfig.MIN_TIME_BETWEEN_API_REQS)
            {
                apiRequestInProgress = true;
                lastKnownApiRequestTime = System.currentTimeMillis();
                final String coordinates = loc.getLatitude() + "|" + loc.getLongitude();

                //retrofit.create(MediawikiGeosearchApi.class)
                mediawikiGeosearchApi
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

        return apiRequestInProgress;
    }
}
