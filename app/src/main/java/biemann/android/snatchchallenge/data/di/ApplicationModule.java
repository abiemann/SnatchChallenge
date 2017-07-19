package biemann.android.snatchchallenge.data.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alexander on 7/19/2017.
 */

@Module
public class ApplicationModule
{
    Application application;

    public ApplicationModule(Application application)
    {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication()
    {
        return application;
    }
}
