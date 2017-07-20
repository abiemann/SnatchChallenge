package biemann.android.snatchchallenge.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import rx.Subscription;

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

    public static void unsubscribe(Subscription subscription)
    {
        if (subscription != null) subscription.unsubscribe();
    }

    private Utilities() {}
}
