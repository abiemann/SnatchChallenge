package biemann.android.snatchchallenge.ui.main;

import android.location.Location;

import java.util.List;

import biemann.android.snatchchallenge.data.api.MediawikiGeosearchModel;

/**
 * Created by Alexander on 7/19/2017.
 */

public interface MainContract
{
    interface View
    {
        // show loaded API data
        void showList(List<MediawikiGeosearchModel.QueryListItem> listItems);
        void showError(String message);
        void showComplete();
    }

    interface Presenter
    {
        // make API call
        boolean loadGeosearchData(Integer range, Location loc);
    }
}
