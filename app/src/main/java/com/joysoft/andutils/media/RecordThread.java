package com.joysoft.andutils.media;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.joysoft.andutils.lg.Lg;

/**
 * 获取麦克风音量 ---吹气泡时用到
 * @author fengmiao
 *
 */
public class RecordThread extends Thread {
	   private AudioRecord ar;  
	    private int bs;  
	    private static int SAMPLE_RATE_IN_HZ = 8000;  
	    private boolean isRun = false;  
	    Handler handler;
	    String TAG = "RecordThread";
	   
	    public RecordThread(Handler handler) { 
	        super();  
	        this.handler = handler;
	        bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,AudioFormat.CHANNEL_CONFIGURATION_MONO,AudioFormat.ENCODING_PCM_16BIT);  
	        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ,  
	                AudioFormat.CHANNEL_CONFIGURATION_MONO,  
	                AudioFormat.ENCODING_PCM_16BIT, bs);  
	    }  
	   
	    public void run() {  
	        super.run();  
	        ar.startRecording();  
	                // 用于读取的 buffer  
	        byte[] buffer = new byte[bs];  
	        isRun = true;  
	        while (isRun) {  
	            int r = ar.read(buffer, 0, bs);  
	            int v = 0;  
	                        // 将 buffer 内容取出，进行平方和运算  
	            for (int i = 0; i < buffer.length; i++) {  
	                // 这里没有做运算的优化，为了更加清晰的展示代码  
	                v += buffer[i] * buffer[i];  
	            }  
	            // 平方和除以数据总长度，得到音量大小。可以获取白噪声值，然后对实际采样进行标准化。  
	            // 如果想利用这个数值进行操作，建议用 sendMessage 将其抛出，在 Handler 里进行处理。  
//	            Log.d("spl", String.valueOf(v / (float) r));  
//	            Message msg = new Message();
//	            msg.arg1 = (int)dB;
//	            handler.sendMessage(msg);
//	            Log.e(TAG,"dB:"+dB);
	        
	            if((v/(float)r)>3000){
	            	Log.e("REcirdThread:","音量："+(v/(float)r));
//					 Message msg = handler.obtainMessage(1,String.valueOf(v / (float) r));
					 Message msg = new Message();
					 msg.arg1 =(int) (v / (float) r/100);
					 handler.sendMessage(msg);
				}		 
	        
	        }  
	        ar.stop();  
	        ar.release();
	    }  
	   
	    public void pause() {  
	                // 在调用本线程的 Activity 的 onPause 里调用，以便 Activity 暂停时释放麦克风  
	    	Lg.d("=====  暂停线程  ======");
	        isRun = false;  
	    }  
	    
	    public void onResume(){
	    	isRun = true;
	    	run();
	    }
	   
	    public void startRe() {  
	                // 在调用本线程的 Activity 的 onResume 里调用，以便 Activity 恢复后继续获取麦克风输入音量  
	        if (!isRun) {  
	        	Lg.d("====  start() 线程 =====");
	            super.start();  
	        }  
	    }
	    
	    public boolean getPermission(Context context){
	    	PackageManager pm = context.getPackageManager();
	        boolean permission = (PackageManager.PERMISSION_GRANTED == 
	                pm.checkPermission("android.permission.RECORD_AUDIO", "packageName"));
	        return permission;
	    }

		
}

