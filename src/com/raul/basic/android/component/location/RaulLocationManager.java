package com.raul.basic.android.component.location;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.raul.basic.android.util.Log;

import android.content.Context;
import android.location.Location;
import android.os.Looper;


/**
 * 
 * 获取位置信息的管理类
 * @author raulxiao
 */
public class RaulLocationManager
{
    /**
     * Debug Tag
     */
    private static final String TAG = "RaulLocationManager";
    
    /**
     * ExecutorService
     */
    private static ExecutorService sFixedThreadPoolExecutor = Executors.newCachedThreadPool();
    
    /**
     * Context
     */
    private Context mContext;
    
    /**
     * 
     * [构造简要说明]
     * @param context Context
     */
    public RaulLocationManager(Context context)
    {
        mContext = context;
    }
    
    /**
     * 
     * 获取位置信息
     * @param listener 位置信息监听
     * @param needAddressInfo 是否需要地址信息
     */
    public void getLocationInfo(final LocationDataListener listener,
            final boolean needAddressInfo)
    {
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                Looper.prepare();
                LocationInfo result = new LocationInfo();
                listener.onLocationProgress(true);
                try
                {
                    result = LocationService.getLocationImpl(mContext)
                            .getLocationInfo();
                    
                    Location location = result.getLocation();
                    if (location != null && needAddressInfo)
                    {
                        result.setAddressInfo(LocationService.getLocationImpl(mContext)
                                .getAddress(location.getLatitude(),
                                        location.getLongitude()));
                    }
                    
                }
                catch (Exception e)
                {
                    Log.e(TAG, e.toString());
                }
                listener.onLocationResult(result);
                listener.onLocationProgress(false);
                Looper.loop();
                
            }
        };
        sFixedThreadPoolExecutor.execute(runnable);
    }
}
