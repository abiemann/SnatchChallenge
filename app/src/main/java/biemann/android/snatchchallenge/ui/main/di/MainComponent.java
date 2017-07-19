package biemann.android.snatchchallenge.ui.main.di;

import biemann.android.snatchchallenge.data.di.NetworkComponent;
import biemann.android.snatchchallenge.di.CustomScope;
import biemann.android.snatchchallenge.ui.main.MainView;
import dagger.Component;

/**
 * Created by Alexander on 7/19/2017.
 */
@CustomScope
@Component(dependencies = NetworkComponent.class, modules = MainModule.class)
public interface MainComponent
{
    void inject(MainView activity);
}
