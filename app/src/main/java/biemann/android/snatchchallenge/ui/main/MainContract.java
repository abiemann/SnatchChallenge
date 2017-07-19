package biemann.android.snatchchallenge.ui.main;

import java.util.List;

import biemann.android.snatchchallenge.data.api.MediawikiGeosearchModel;

/**
 * Created by Alexander on 7/19/2017.
 */

public interface MainContract
{
    interface View
    {
        void showList(List<MediawikiGeosearchModel.QueryListItem> listItems);
        void showError(String message);
        void showComplete();
    }

    interface Presenter
    {
        void loadGeosearchData(Integer range, Double lat, Double lon);
    }
}
