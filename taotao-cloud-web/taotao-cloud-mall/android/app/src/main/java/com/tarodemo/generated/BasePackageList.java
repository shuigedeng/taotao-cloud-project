package com.tarodemo.generated;

import java.util.Arrays;
import java.util.List;
import org.unimodules.core.interfaces.Package;

public class BasePackageList {
  public List<Package> getPackageList() {
    return Arrays.<Package>asList(
        new expo.modules.av.AVPackage(),
        new expo.modules.barcodescanner.BarCodeScannerPackage(),
        new expo.modules.camera.CameraPackage(),
        new expo.modules.constants.ConstantsPackage(),
        new expo.modules.filesystem.FileSystemPackage(),
        new expo.modules.brightness.BrightnessPackage(),
        new expo.modules.imageloader.ImageLoaderPackage(),
        new expo.modules.imagepicker.ImagePickerPackage(),
        new expo.modules.keepawake.KeepAwakePackage(),
        new expo.modules.location.LocationPackage(),
        new expo.modules.permissions.PermissionsPackage(),
        new expo.modules.sensors.SensorsPackage()
    );
  }
}
