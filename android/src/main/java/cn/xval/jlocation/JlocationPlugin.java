package cn.xval.jlocation;

import cn.xval.plugin.PluginPermissionHelper;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** JlocationPlugin */
public class JlocationPlugin implements PluginPermissionHelper.OnPermissionStatusChangedListener {
  /** PluginPermissionHelper registration. */
  public static void registerWith(Registrar registrar) {
    new JlocationPlugin(registrar);
  }


  private PluginPermissionHelper mPermission;
  private JLocation mLocation;
  private JLocationUtil mLocationUtil;
  private Registrar mRegistrar;

  JlocationPlugin(Registrar registrar) {
    mRegistrar = registrar;
    mPermission = new PluginPermissionHelper(this, registrar.activity());
    mLocationUtil = new JLocationUtil(this);
    mLocation = new JLocation(this);
  }

  public PluginPermissionHelper getPermission() {
    return mPermission;
  }

  public Registrar getRegistrar() {
    return mRegistrar;
  }

  public JLocation getLocation() {
    return mLocation;
  }

  @Override
  public void onPermissionStatus(String permission, PluginPermissionHelper.PermissionStatus status) {
    if (status == PluginPermissionHelper.PermissionStatus.GRANTED) {
      mLocation.reStartListen();
    }
    else if (status == PluginPermissionHelper.PermissionStatus.NEED_OPEN_SETTINGS) {
      // 提示打开设置
      mLocation.responseError("PERMISSION_DENIED_NEVER_ASK",
              "Location permission denied forever - please open app settings", null);
    }
    else {
      // 没有获得权限
      mLocation.responseError("PERMISSION_DENIED",
              "Location permission denied", null);
    }
  }
}
