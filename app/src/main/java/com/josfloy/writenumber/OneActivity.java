package com.josfloy.writenumber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.josfloy.writenumber.util.CustomProgressDialog;
import com.josfloy.writenumber.util.MusicPool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jos on 2018/7/31 0031.
 * You can copy it anywhere you want
 */
public class OneActivity extends Activity {
    private ImageView iv_frame;
    int i = 1;
    float x1;
    float y1;
    float x2;
    float y2;
    float x3;
    float y3;
    int igvx;
    int igvy;
    int type = 0;
    int widthPixels;
    int heightPixels;
    float scaleWidth;
    float scaleHeight;
    Timer touchTimer = null;
    Bitmap arrdown;
    boolean typedialog = true;
    private LinearLayout linearLayout = null;
    MusicPool mMusicPool;
    public CustomProgressDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        mMusicPool = new MusicPool();
        mMusicPool.initMediaPlayer(this, R.raw.music1);

        if (MainActivity.isPlay) {
            mMusicPool.playMusic();
        }
        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMusicPool.stopMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMusicPool.destroyMusic();
    }

    //递减显示帧图片的handler消息头部
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    jlodimge();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void initView() {
        iv_frame = findViewById(R.id.iv_frame);
        linearLayout = findViewById(R.id.LinearLayout1);
        LinearLayout write_Layout = findViewById(R.id.LinearLayout_number);
        write_Layout.setBackgroundResource(R.drawable.bg1);

        widthPixels = this.getResources().getDisplayMetrics().widthPixels;
        heightPixels = this.getResources().getDisplayMetrics().heightPixels;

        //因为图片等资源是按1280*720准备的，如果是其他分辨率，适应屏幕做准备
        scaleWidth = ((float) widthPixels / 720);
        scaleHeight = ((float) heightPixels / 1280);

        try {
            //通过输入流打开第一张图片
            InputStream is = getResources().getAssets().open("on1_1.png");
            //使用Bitmap 解析第一张图片
            arrdown = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //获取布局的宽高信息
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) iv_frame
                .getLayoutParams();
        //获取图片缩放后的宽高
        layoutParams.width = (int) (arrdown.getWidth() * scaleWidth);
        layoutParams.height = (int) (arrdown.getHeight() * scaleHeight);

        iv_frame.setLayoutParams(layoutParams);
        loadImage(1); //调用loadImage()方法，进入页面加载第一个图片

        //无意中把linearLayout从类属性改成局部属性后发现报onPerformClick错误
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        y1 = event.getY();
                        igvx = iv_frame.getLeft();
                        igvy = iv_frame.getTop();
                        //判断手指按下的坐标大于按下图片的坐标时，证明手指移动 开启书写
                        if (x1 >= igvx && x1 <= igvx + (int) (arrdown.getWidth() * scaleWidth)
                                && y1 >= igvy & y1 <= igvy + (int) (arrdown.getWidth() * scaleWidth)) {
                            type = 1;
                        } else {
                            type = 0;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        igvx = iv_frame.getLeft();
                        igvy = iv_frame.getTop();
                        x2 = event.getX();
                        y2 = event.getY();
                        //下边 是根据比划已经手势做图片的处理 滑动到不同位置加载不同图片
                        if (type == 1) {
                            drawNumber();
                        } else {
                            type = 0;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        type = 0;
                        //当手指离开的时候
                        if (touchTimer != null) {
                            touchTimer.cancel();
                            touchTimer = null;
                        }

                        setTimer();
                }
                return true;
            }
        });
    }

    /**
     * 设置定时器，定时发送消息刷新图片
     */
    private void setTimer() {
        touchTimer = new Timer();
        touchTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //创建Message用于发送消息
                        Message message = new Message();
                        message.what = 2;
                        mHandler.sendMessage(message);
                    }
                });
                thread.start();
            }
        }, 300, 200);
    }

    private void drawNumber() {
        // 如果手指按下的X坐标大于等于图片的X坐标，或者小于等于缩放图片的X坐标时
        if (x2 >= igvx && x2 <= igvx + (int) (arrdown.getWidth() * scaleWidth)) {
            // 如果当前手指按下的Y坐标小于等于缩放图片的Y坐标，或者大于等于图片的Y坐标时
            if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 && y2 >= igvy) {
                loadImage(1);            // 调用loadImage()方法，加载第一张显示图片
            }
            // 如果当前手指按下的Y坐标小于等于缩放图片的Y坐标
            else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 2) {
                loadImage(2);            // 调用loadImage()方法，加载第二张显示图片
            }
            // 如果当前手指按下的Y坐标小于等于缩放图片的Y坐标
            else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 3) {
                loadImage(3);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 4) {
                loadImage(4);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 5) {
                loadImage(5);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 6) {
                loadImage(6);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 7) {
                loadImage(7);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 8) {
                loadImage(8);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 9) {
                loadImage(9);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 10) {
                loadImage(10);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 11) {
                loadImage(11);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 12) {
                loadImage(12);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 13) {
                loadImage(13);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 14) {
                loadImage(14);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 15) {
                loadImage(15);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 16) {
                loadImage(16);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 17) {
                loadImage(17);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 18) {
                loadImage(18);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 19) {
                loadImage(19);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 20) {
                loadImage(20);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 21) {
                loadImage(21);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 22) {
                loadImage(22);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 23) {
                loadImage(23);
            } else if (y2 <= igvy + (int) (arrdown.getHeight() * scaleHeight) / 24 * 24) {
                loadImage(24);   //加载最后一张图片时，将在lodimagep()方法中调用书写完成对话框
            } else {
                type = 0;         // 手指离开 设置书写关闭
            }
        }
    }


    private synchronized void loadImage(int j) {
        i = j;
        if (i < 25) {
            getResourceImage();
            i++;
        }

        if (j == 24) {
            if (typedialog) {
                dialog();
            }
        }
    }

    private void jlodimge() {
        if (i == 25) {
        } else if (i < 25) {
            if (i > 1) {
                i--;
            } else if (i == 1) {
                i = 1;
                if (touchTimer != null) {
                    touchTimer.cancel();
                    touchTimer = null;
                }
            }
            getResourceImage();
        }
    }

    private void getResourceImage() {
        String name = "on1_" + i;
        //获取图片资源
        int imgid = getResources().getIdentifier(name, "drawable",
                "com.josfloy.writenumber");
        iv_frame.setBackgroundResource(imgid);
    }

    protected void dialog() {
        typedialog = false;
        //实例化对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(OneActivity.this);
        builder.setMessage("太棒了，书写完成!")
                .setTitle("提示")
                .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        typedialog = true;
                        finish();
                    }
                })
                .setPositiveButton("再来一次", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        typedialog = true;
                        i = 1;
                        loadImage(i);
                    }
                })
                .create()
                .show();
    }

    public void OnYS(View view) {
        if (mDialog == null) {
            mDialog = new CustomProgressDialog(this, "演示中单击边缘取消", R.drawable.frame1);
        }

        mDialog.show();
    }
}
