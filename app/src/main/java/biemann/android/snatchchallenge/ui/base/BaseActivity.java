package biemann.android.snatchchallenge.ui.base;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

/**
 * Created by Alexander on 7/20/2017.
 */
public abstract class BaseActivity extends AppCompatActivity
{

    @Override
    protected void onStart()
    {
        super.onStart();
        RxPermissions
                .getInstance(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Action1<Boolean>()
                {
                    @Override
                    public void call(Boolean granted)
                    {
                        if (granted)
                        {
                            onLocationPermissionGranted();
                        } else
                        {
                            Toast.makeText(BaseActivity.this, "Sorry, no demo without permission...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    protected abstract void onLocationPermissionGranted();
}
