package biemann.android.snatchchallenge.ui.main;


import biemann.android.snatchchallenge.data.api.MediawikiGeosearchModel;

/**
 * Created by Alexander on 6/18/2017.
 */

public interface ListInteractionListener
{
    void onListClick(MediawikiGeosearchModel.QueryListItem data);
}
