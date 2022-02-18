package cool.thejiangbf.flashlight;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * @author theJiangBF
 */
public class FullscreenActivity extends Activity {

    private ImageView ivBg;
    private RelativeLayout rlBg;

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fullscreen);

        ivBg = findViewById(R.id.iv_bg);
        rlBg = findViewById(R.id.rl_bg);

        RadioButton rbModeDark = findViewById(R.id.rb_mode_dark);
        RadioButton rbModeLight = findViewById(R.id.rb_mode_light);
        RadioButton rbModeColor = findViewById(R.id.rb_mode_color);

        ivBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if (flag){
                //如果开启时点击了按钮
                ivBg.setImageResource(R.drawable.ic_flashlight_off2);
            }else {
                ivBg.setImageResource(R.drawable.ic_flashlight_on2);
            }
            flashLight(!flag);
            flag = !flag;
            }
        });

        rbModeDark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rlBg.setBackgroundColor(Color.parseColor("#000000"));
                }
            }
        });

        rbModeLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rlBg.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });

        rbModeColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rlBg.setBackgroundColor(Color.parseColor("#0099cc"));
                }
            }
        });

        ivBg.performClick();

    }

    public void flashLight(boolean state){
        //判断API是否大于24（安卓7.0系统对应的API）
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
                    mCameraManager.setTorchMode(id, state);
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            ivBg.performClick();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO 关闭手电筒
        flashLight(false);
    }
}
