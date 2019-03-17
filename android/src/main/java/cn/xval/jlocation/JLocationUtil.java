package cn.xval.jlocation;

import android.Manifest;

import cn.xval.plugin.PluginPermissionHelper;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class JLocationUtil implements MethodChannel.MethodCallHandler  {
    private static final String METHOD_CHANNEL_NAME = "xval.cn/jlocation";

    private final MethodChannel channel;

    private PluginPermissionHelper mPermission;
    private JlocationPlugin mPlugin;
    private PluginRegistry.RequestPermissionsResultListener mPermissionsResultListener;

    public JLocationUtil(JlocationPlugin plugin) {
        mPlugin = plugin;
        PluginRegistry.Registrar registrar = plugin.getRegistrar();
        mPermission = plugin.getPermission();

        channel = new MethodChannel(registrar.messenger(), METHOD_CHANNEL_NAME);
        channel.setMethodCallHandler(this);
        registrar.addRequestPermissionsResultListener(mPermission);
    }

    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
        if (call.method.equals("hasPermission")) {
            if(mPermission.checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION)) {
                result.success(1);
            } else {
                result.error("PERMISSION_DENIED", "The user explicitly denied the use of location services for this app or location services are currently disabled in Settings.", null);
            }
        }
        if ("setProperty".equals(call.method)) {
            // 初始化
            if (call.hasArgument("minTime")) {
                int minTime = call.argument("minTime");
                mPlugin.getLocation().setMinTime((long) minTime);
            }
            else {
                mPlugin.getLocation().setMinTime(null);
            }

            if (call.hasArgument("minDistance")) {
                double minDistance = call.argument("minDistance");
                mPlugin.getLocation().setMinDistance((float) minDistance);
            }
            else {
                mPlugin.getLocation().setMinDistance(null);
            }

            mPlugin.getLocation().reStartListen();
            result.success(1);
        }
        else {
            result.notImplemented();
        }
    }

}
