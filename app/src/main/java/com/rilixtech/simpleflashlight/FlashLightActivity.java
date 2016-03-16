package com.rilixtech.simpleflashlight;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class FlashLightActivity extends Activity implements ToggleButton.OnClickListener {
  private Camera cam = Camera.open();          // used to open
  private Parameters params = cam.getParameters();  // the camera and flashlight

  protected void onCreate(Bundle savedInstanceState) {
    //boolean flash = params.getFlashMode().equals(Parameters.FLASH_MODE_TORCH);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_window);
    ToggleButton tb = (ToggleButton) findViewById(R.id.main_toggle_btn);
    tb.setOnClickListener(this);
    tb.setChecked(false);

    Button aboutBtn = (Button) findViewById(R.id.main_about_btn);
    aboutBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        AlertDialog(getString(R.string.app_name), getString(R.string.about_content), "OK");
      }
    });
  }

  // turns the flashlight on:
  private boolean turnOn() {
    cam.startPreview();
    params.setFlashMode(Parameters.FLASH_MODE_TORCH);
    cam.setParameters(params);
    return true;
  }

  // turns the flashlight off:
  private void turnOff() {
    params.setFlashMode(Parameters.FLASH_MODE_OFF);
    cam.setParameters(params);
  }

  private void AlertDialog(String title, String msg, String btn) {
    SpannableString txt = new SpannableString(msg);
    Linkify.addLinks(txt, Linkify.WEB_URLS);
    final AlertDialog ad = new AlertDialog.Builder(this).setTitle(title)
        .setMessage(txt)
        .setIcon(R.mipmap.ic_launcher)
        .setPositiveButton(btn, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
          }
        })
        .create();
    ad.show();
    ((TextView) ad.findViewById(android.R.id.message)).setMovementMethod(
        LinkMovementMethod.getInstance());
  }

  // when the app is closed:
  protected void onDestroy() {
    turnOff();
    cam.stopPreview();
    cam.setPreviewCallback(null);
    cam.release();
    super.onDestroy();
  }

  @Override public void onClick(View v) {
    boolean on = ((ToggleButton) v).isChecked();
    if (on) {
      turnOn();
    } else {
      turnOff();
    }
  }
}
