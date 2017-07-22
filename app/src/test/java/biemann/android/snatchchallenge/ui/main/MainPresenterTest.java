package biemann.android.snatchchallenge.ui.main;

import android.location.Location;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import biemann.android.snatchchallenge.data.api.MediawikiGeosearchApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertFalse;
import static org.mockito.Mockito.verify;

/**
 * Created by Alexander on 7/20/2017.
 */
public class MainPresenterTest
{
    private final String INVALID_URL = "http://example.com";
    private MainPresenter presenter;

    @Mock MainContract.View view;
    @Mock Location loc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(INVALID_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Override IO Scheduler that is being used to subscribe (Schedulers.io())
        RxJavaHooks.setOnIOScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.immediate();
            }
        });

//        RxJavaHooks.setOnComputationScheduler(new Func1<Scheduler, Scheduler>() {
//            @Override
//            public Scheduler call(Scheduler scheduler) {
//                return Schedulers.immediate();
//            }
//        });
//
//        RxJavaHooks.setOnNewThreadScheduler(new Func1<Scheduler, Scheduler>() {
//            @Override
//            public Scheduler call(Scheduler scheduler) {
//                return Schedulers.immediate();
//            }
//        });
//
//        // Override RxAndroid schedulers
//        final RxAndroidPlugins rxAndroidPlugins = RxAndroidPlugins.getInstance();
//        rxAndroidPlugins.registerSchedulersHook(new RxAndroidSchedulersHook() {
//            @Override
//            public Scheduler getMainThreadScheduler() {
//                return Schedulers.immediate();
//            }
//        });

        MediawikiGeosearchApi mediawikiGeosearchApi = retrofit.create(MediawikiGeosearchApi.class);
        presenter = new MainPresenter(mediawikiGeosearchApi, view);
    }


    @Test
    public void testLoadGeosearchDataBadUrl() throws Exception
    {
        presenter.loadGeosearchData(1, loc);
        verify(view).showError("HTTP 404 Not Found");
    }

    @Test
    public void testLoadGeosearchDataRapidCallsNotServiced() throws Exception
    {
        presenter.loadGeosearchData(1, loc);
        boolean api_busy2 = presenter.loadGeosearchData(1, loc);
        assertFalse(api_busy2);
    }

    @After
    public void tearDown() throws Exception {
        RxJavaHooks.reset();
        RxAndroidPlugins.getInstance().reset();
    }

}