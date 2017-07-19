package biemann.android.snatchchallenge.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Alexander on 7/19/2017.
 */

final public class Utilities
{
    public static void shareUrl(Context context, String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(Intent.createChooser(intent, "Open with..."));
    }

    private Utilities() {}
}
