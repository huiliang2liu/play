package com.dlna.cybergarage;

import android.os.Handler;
import android.os.Looper;

import com.dlna.action.DlnaAction;
import com.dlna.action.avTransport.AVTransportAction;
import com.dlna.action.renderingControl.RenderingControlAction;
import com.dlna.iface.FailureRunnable;
import com.dlna.iface.IControl;
import com.dlna.iface.SuccessRunnable;
import com.dlna.listener.ControlListener;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;

public class CybergarageControlImpl implements IControl {
    public static final String AV_TRANSPORT_SERVICE = "urn:schemas-upnp-org:service:AVTransport:1";
    /**
     * 控制服务
     */
    public static final String RENDERING_CONTROL_SERVICE = "urn:schemas-upnp-org:service:RenderingControl:1";
    private Device device;
    private Handler handler = new Handler(Looper.myLooper());

    public CybergarageControlImpl(Device device) {
        this.device = device;
    }


    @Override
    public void avTransport(AVTransportAction action, final ControlListener listener) {
        final Service service = findService(AV_TRANSPORT_SERVICE);
        if (service == null) {
            runMain(new FailureRunnable(listener));
            return;
        }
        control(service, action, listener);
    }

    @Override
    public void renderingControl(RenderingControlAction action, final ControlListener listener) {
        final Service service = findService(RENDERING_CONTROL_SERVICE);
        if (service == null) {
            runMain(new FailureRunnable(listener));
            return;
        }
        control(service, action, listener);
    }

    private void control(Service service, DlnaAction action, final ControlListener listener) {
        new Thread(new CybergarageAction(service, action) {
            @Override
            public void success() {
                runMain(new SuccessRunnable(listener));
            }

            @Override
            public void failure() {
                runMain(new FailureRunnable(listener));
            }
        }).start();
    }

    private void runMain(Runnable runnable) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread())
            runnable.run();
        else
            handler.post(runnable);
    }

    private Service findService(String serviceType) {
        if (device == null || serviceType == null || serviceType.isEmpty())
            return null;
        return device.getService(serviceType);
    }


}
