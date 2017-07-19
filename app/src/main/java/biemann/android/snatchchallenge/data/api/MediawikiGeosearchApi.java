package biemann.android.snatchchallenge.data.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * API call
 *
 * example:
 * https://en.wikipedia.org/w/api.php?action=query&list=geosearch&gsradius=10000&gscoord=51.508164|-0.106511&format=json
 */

public interface MediawikiGeosearchApi
{
    @GET("/w/api.php")
    Observable<MediawikiGeosearchModel> getGeosearchFromApi
            (
            @Query("action") String action,
            @Query("list") String appId,
            @Query("gsradius") Integer radius,
            @Query("gscoord") String coordinates,
            @Query("format") String format
            );
}
