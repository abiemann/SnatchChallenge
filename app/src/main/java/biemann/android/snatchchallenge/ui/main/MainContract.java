package biemann.android.snatchchallenge.ui.main;

import android.location.Address;
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

        // show GPS error
        void showGpsError();
    }

    interface Presenter
    {
        // make API call
        void loadGeosearchData(Integer range, Double lat, Double lon);

        // get GPS updates
        void onLocationUpdate(Location location);
        void onAddressUpdate(Address address);
        void onLocationSettingsUnsuccessful();
    }
}
