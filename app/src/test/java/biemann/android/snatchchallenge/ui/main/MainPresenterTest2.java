package biemann.android.snatchchallenge.ui.main;

import android.location.Location;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.matchers.ThrowableCauseMatcher;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import biemann.android.snatchchallenge.data.api.MediawikiGeosearchApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;

/**
 * Created by Alexander on 7/20/2017.
 */
public class MainPresenterTest2
{

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
        MainContract.View mockedView;

    @Mock
    MediawikiGeosearchApi mediawikiGeosearchApi;

    @InjectMocks
    MainPresenter mainPresenter;

    @Before
    public void setup()
    {

        // Override IO Scheduler that is being used to subscribe (Schedulers.io())
        RxJavaHooks.setOnIOScheduler(new Func1<Scheduler, Scheduler>()
        {
            @Override
            public Scheduler call(Scheduler scheduler)
            {
                return Schedulers.immediate();
            }
        });
    }


        @Test
    public void testServerDown() throws Exception
    {
        // given
        String fakeError = "fjiehofeh";
        Throwable fakeThrowable = Mockito.mock(Throwable.class);
        Mockito.doReturn(fakeError).when(fakeThrowable).getMessage();
        Location location = Mockito.mock(Location.class);
        Mockito.doReturn(Observable.error(fakeThrowable)).when(mediawikiGeosearchApi).getGeosearchFromApi(Matchers.anyString(),
        Matchers.anyString(),Matchers.anyInt(),Matchers.anyString(), Matchers.anyString()
        );


        // when
        mainPresenter.loadGeosearchData(1, location);
        // then
        Mockito.verify(mockedView, times(1)).showError(fakeError);

    }

    @After
    public void tearDown() throws Exception {
        RxJavaHooks.reset();
        RxAndroidPlugins.getInstance().reset();
    }
}