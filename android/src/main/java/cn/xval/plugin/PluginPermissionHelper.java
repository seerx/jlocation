package cn.xval.plugin;

import android.app.Activity;
import android.content.pm.PackageManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.core.app.ActivityCompat;

import io.flutter.plugin.common.PluginRegistry;

/**
 * PluginPermissionHelper 实现插件权限相关的操作
 * 包含检查是否有权限以及申请权限
 * 提供了权限状态回调接口
 */
public class PluginPermissionHelper implements PluginRegistry.RequestPermissionsResultListener {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE_START = 100;
    private static final AtomicInteger RequestCodeNum = new AtomicInteger();
    private static final Map<String, Integer> RequestCodeMap = new HashMap<>();

    static {
        RequestCodeNum.set(REQUEST_PERMISSIONS_REQUEST_CODE_START);
    }

    public enum PermissionStatus {
        GRANTED,            // 准许
        NEED_OPEN_SETTINGS, // 设备关闭了该项功能，需要打开设置
        DENIED              // 用户禁止了该权限
    }
    public interface OnPermissionStatusChangedListener {
        void onPermissionStatus(String permission, PermissionStatus status);
    }

    private OnPermissionStatusChangedListener mStatusListener;
    private Activity mActivity;
    private String permission;

    public PluginPermissionHelper(OnPermissionStatusChangedListener mStatusListener, Activity mActivity) {
        this.mStatusListener = mStatusListener;
        this.mActivity = mActivity;
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (permissions.length == 1) {
            String permission = permissions[0];
            Integer code = RequestCodeMap.get(permission);
            if (code != null && requestCode == code) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mStatusListener.onPermissionStatus(permission, PermissionStatus.GRANTED);
                } else {
                    if (! shouldShowRequestPermissionRationale(permission)) {
                        mStatusListener.onPermissionStatus(permission, PermissionStatus.NEED_OPEN_SETTINGS);

                    } else {
                        mStatusListener.onPermissionStatus(permission, PermissionStatus.DENIED);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean checkPermissions(String permission) {
        int permissionState = ActivityCompat.checkSelfPermission(mActivity, permission);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions(String permission) {
        Integer code = RequestCodeMap.get(permission);
        if (code == null) {
            code = RequestCodeNum.incrementAndGet();
            RequestCodeMap.put(permission, code);
        }
        ActivityCompat.requestPermissions(mActivity, new String[]{ permission },
                code);
    }

    private boolean shouldShowRequestPermissionRationale(String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission);
    }

}
