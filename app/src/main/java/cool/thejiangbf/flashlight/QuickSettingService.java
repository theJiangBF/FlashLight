package cool.thejiangbf.flashlight;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

/**
 * @author jiangbaofu
 */
@TargetApi(Build.VERSION_CODES.N)
public class QuickSettingService extends TileService {
    private boolean flag = false;

    @Override
    public void onClick() {
        try {
            //获取CameraManager
            CameraManager mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            //获取当前手机所有摄像头设备ID
            String[] ids  = mCameraManager.getCameraIdList();
            for (String id : ids) {
                CameraCharacteristics c = mCameraManager.getCameraCharacteristics(id);
                //查询该摄像头组件是否包含闪光灯
                Boolean flashAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                Integer lensFacing = c.get(CameraCharacteristics.LENS_FACING);
                if (flashAvailable != null && flashAvailable
                        && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                    //打开或关闭手电筒
                    flag = !flag;
                    mCameraManager.setTorchMode(id, flag);
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        Tile qsTile = getQsTile();
        Icon icon;
        if (flag){
            qsTile.setLabel("极简手电:开");
            qsTile.setState(Tile.STATE_ACTIVE);
            icon = Icon.createWithResource(this, R.drawable.ic_flash_on_black_24dp);
        }else {
            qsTile.setLabel("极简手电:关");
            qsTile.setState(Tile.STATE_INACTIVE);
            icon = Icon.createWithResource(this, R.drawable.ic_flash_off_black_24dp);
        }
        qsTile.setIcon(icon);
        qsTile.updateTile();
    }
}