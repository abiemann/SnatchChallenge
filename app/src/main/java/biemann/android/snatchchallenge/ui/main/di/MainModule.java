package biemann.android.snatchchallenge.ui.main.di;

import biemann.android.snatchchallenge.annotation.ActivityScope;
import biemann.android.snatchchallenge.data.api.MediawikiGeosearchApi;
import biemann.android.snatchchallenge.ui.main.MainContract;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Alexander on 7/19/2017.
 */
@Module
public class MainModule
{
    private final MainContract.View view;


    public MainModule(MainContract.View view)
    {
        this.view = view;
    }

    @Provides
    @ActivityScope
    MainContract.View providesMainContractView()
    {
        return view;
    }

    @Provides
    MediawikiGeosearchApi providesMediawikiGeosearchApi(Retrofit retrofit) {
        return retrofit.create(MediawikiGeosearchApi.class);
    }
}
