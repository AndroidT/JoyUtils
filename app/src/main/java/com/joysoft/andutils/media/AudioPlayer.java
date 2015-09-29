package com.joysoft.andutils.media;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.text.TextUtils;

import com.joysoft.utils.lg.Lg;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AudioPlayer {
	
	private static AudioPlayer audioPlayer;
	private ArrayList<PlayListener> listenerList = new ArrayList<PlayListener>();
	private MediaPlayer mediaPlayer;
	private Timer timer;
	private ProgressTimerTask timerTask;
	private boolean isPrepared = false;
	// private boolean isPaused=false;
	private int duration = -1;
	// 正在播放歌曲的url
//	public static String PLAYINGID_HOME="";
//	public static String PLAYINGID_OTHER="";
	
	private String tag = "";
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	private AudioPlayer() {
		initMediaPlayer();
	}

	public static AudioPlayer newInstance() {
		if (audioPlayer == null) {
			audioPlayer = new AudioPlayer();
		}
		return audioPlayer;
	}

	public void addPlayListener(PlayListener playListener) {
		listenerList.add(playListener);
	}

	public void removePlayerListener(PlayListener listener) {
		listenerList.remove(listener);
	}

	private String musicUrl = "";
	/**
	 * 添加音乐的url(如果有歌曲在播放，新的url会将正在播放的资源顶替掉)
	 * 
	 * @param url
	 */
	public void setAudioUrl(String url) {
		try {
			isPrepared = false;
			mediaPlayer.reset();
			musicUrl = url;
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
				
				@Override
				public void onBufferingUpdate(MediaPlayer mp, int percent) {
					for (PlayListener listener : listenerList) {
						listener.onBuffering(true,percent);
					}
				}
			});
			
		} catch (Exception e) {
			for (PlayListener listener : listenerList) {
				listener.onErr("歌曲格式错误，无法播放.");
			}
			Lg.e(e.toString());
		}
	}
	
	public String getMusicUrl(){
		return musicUrl;
	}

	/**
	 * 播放
	 */
	public void play() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!isPrepared) {
				}
				// if(!isPaused){
				Lg.e("start");
				mediaPlayer.start();
				// isPaused=false;
				// }
			}
		}).start();
	}

	/**
	 * 暂停
	 */
	public void pause() {
		if (isPlaying()) {
			mediaPlayer.pause();
			// isPaused=true;
		}
	}

	/**
	 * 是否处于播放状态
	 */
	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	/**
	 * 是否正在准备资源
	 * 
	 * @return
	 */
	public boolean isPreparingResource() {
		return !TextUtils.isEmpty(musicUrl) && !isPrepared;
	}

	/**
	 * 停止播放
	 */
	public void stop() {
		isPrepared = false;
		duration = -1;
		if (mediaPlayer != null) {
			mediaPlayer.reset();
		}
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	/*
	 * 释放所有资源
	 */
	public void release() {
		stop();
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
		listenerList.clear();
		listenerList = null;
		audioPlayer = null;
		System.gc();
	}
	
	/**
	 * 当前音乐的时间
	 * @return
	 */
	public int getDuration(){
		return mediaPlayer == null ? -1 : mediaPlayer.getDuration();
	}
	
	public void seekTo(int position){
		if(mediaPlayer == null)
			return;
		mediaPlayer.seekTo(position);
	}

	/**
	 * 播放状态的监听器
	 * 
	 * @author dongbo 2014-1-20
	 */
	public interface PlayListener {
		/** 播放异常，可将错误信息直接展示给用户 */
		public void onErr(String msg);

		/** 是否在缓冲。true:缓冲中 false:没处于缓冲状态 */
		public void onBuffering(boolean isBuffering, int bufferingProgress);

		/** 播放进度。duration:总时长(单位ms)；curPosition:播放的当前位置(单位ms) */
		public void onPlayingProgress(int duration, int curPositon);

		/** 当前音乐播放完毕 */
		public void onCompleted();
		
	}

	private void initMediaPlayer() {
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Lg.e("onError");
				isPrepared = true;
				for (PlayListener listener : listenerList) {
					listener.onErr("歌曲格式错误，无法播放.");
					listener.onBuffering(false,0);
				}
				return true;
			}
		});
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				Lg.e("onPrepared");
				isPrepared = true;
				duration = mp.getDuration();
				for (PlayListener listener : listenerList) {
					listener.onBuffering(false,-1);
				}
				// 监听播放进度
				if (timer == null) {
					timer = new Timer();
				}
				if (timerTask == null) {
					timerTask = new ProgressTimerTask();
					timer.schedule(timerTask, 0, 1000);
				}
			}
		});
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				Lg.e("onCompletion");
				for (PlayListener listener : listenerList) {
					listener.onCompleted();
				}
			}
		});
	}
	
	
	private class ProgressTimerTask extends TimerTask {
		@Override
		public void run() {
			if (duration - mediaPlayer.getCurrentPosition() < 1000) {// 微调
				for (PlayListener listener : listenerList) {
					listener.onPlayingProgress(duration, duration);
				}
				timerTask.cancel();
				timerTask = null;
			} else {
				for (PlayListener listener : listenerList) {
					listener.onPlayingProgress(duration,
							mediaPlayer.getCurrentPosition());
				}
			}
		}
	}
}
