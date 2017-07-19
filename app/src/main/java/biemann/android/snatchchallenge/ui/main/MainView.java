package biemann.android.snatchchallenge.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import biemann.android.snatchchallenge.BuildConfig;
import biemann.android.snatchchallenge.MainApplication;
import biemann.android.snatchchallenge.R;
import biemann.android.snatchchallenge.data.api.MediawikiGeosearchModel;
import biemann.android.snatchchallenge.ui.main.di.DaggerMainComponent;
import biemann.android.snatchchallenge.ui.main.di.MainModule;
import biemann.android.snatchchallenge.utils.Utilities;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alexander on 7/19/2017.
 */

public class MainView extends AppCompatActivity implements MainContract.View, ListInteractionListener
{
    private ListRecyclerViewAdapter adapter;

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
                .build().inject(this);

        adapter = new ListRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //make initial API call
        presenter.loadGeosearchData(10000, 51.508164, -0.106511);//TODO make dynamic
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

    }

    @Override
    public void showComplete()
    {

    }

    @Override
    public void showGpsError()
    {
        Snackbar.make(coordinator, "Location settings requirements not satisfied. Showing last known location if available.", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        presenter.startLocationRefresh();
                    }})
                .show();
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


}
