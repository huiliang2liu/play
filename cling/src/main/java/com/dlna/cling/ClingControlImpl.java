package com.dlna.cling;

import android.os.Handler;
import android.os.Looper;

import com.dlna.action.DlnaAction;
import com.dlna.action.avTransport.AVTransportAction;
import com.dlna.action.renderingControl.RenderingControlAction;
import com.dlna.iface.FailureRunnable;
import com.dlna.iface.IControl;
import com.dlna.iface.SuccessRunnable;
import com.dlna.listener.ControlListener;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.model.types.UDAServiceType;

public class ClingControlImpl implements IControl {
    public static final ServiceType AV_TRANSPORT_SERVICE = new UDAServiceType("AVTransport");
    /**
     * 控制服务
     */
    public static final ServiceType RENDERING_CONTROL_SERVICE = new UDAServiceType("RenderingControl");
    private Device mDevice;
    private AndroidUpnpService upnpService;
    private Handler handler = new Handler(Looper.myLooper());

    public ClingControlImpl(Device device, AndroidUpnpService upnpService) {
        mDevice = device;
        this.upnpService = upnpService;
    }

    @Override
    public void avTransport(AVTransportAction action, final ControlListener listener) {
        Service service = findService(AV_TRANSPORT_SERVICE);
        if (service == null) {
            runMain(new FailureRunnable(listener));
            return;
        }
        control(service, action, listener);
    }

    @Override
    public void renderingControl(RenderingControlAction action, final ControlListener listener) {
        Service service = findService(RENDERING_CONTROL_SERVICE);
        if (service == null) {
            runMain(new FailureRunnable(listener));
            return;
        }
        control(service, action, listener);
    }

    private void control(Service service, DlnaAction action, final ControlListener listener) {
        upnpService.getControlPoint().execute(new ClingAction(service, action) {
            @Override
            public void success(ActionInvocation invocation) {
                runMain(new SuccessRunnable(listener));
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                runMain(new FailureRunnable(listener));
            }
        });
    }

    public Service findService(ServiceType serviceType) {
        if (mDevice == null || serviceType == null)
            return null;
        return mDevice.findService(serviceType);
    }

    private void runMain(Runnable runnable) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread())
            runnable.run();
        else
            handler.post(runnable);
    }
}
