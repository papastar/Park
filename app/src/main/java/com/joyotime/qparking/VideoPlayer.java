package com.joyotime.qparking;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.VideoView;

import com.ansai.uparking.R;

public class VideoPlayer extends Activity implements OnErrorListener, OnCompletionListener {
	public static final String TAG = "VideoPlayer";
	private VideoView mVideoView;
	private Uri mUri;
	private int mPositionWhenPaused = -1;

	// private MediaController mMediaController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_voidview);

		// Set the screen to landscape.
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);

		mVideoView = (VideoView) findViewById(R.id.video_view);

		// Video file
		mUri = Uri.parse(Environment.getExternalStorageDirectory() + "/1.mp4");

		// Create media
		// controller，组件可以控制视频的播放，暂停，回复，seek等操作，不需要你实现setScreenOnWhilePlaying
		// mMediaController = new MediaController(this);

		// mVideoView.setMediaController(mMediaController);

	}

	@Override
	public void onStart() {
		// Play Video
		mVideoView.setVideoURI(mUri);
		mVideoView.start();

		super.onStart();
	}

	@Override
	public void onPause() {
		// Stop video when the activity is pause.
		mPositionWhenPaused = mVideoView.getCurrentPosition();
		mVideoView.stopPlayback();

		super.onPause();
	}

	@Override
	public void onResume() {
		// Resume video player
		if (mPositionWhenPaused >= 0) {
			mVideoView.seekTo(mPositionWhenPaused);
			mPositionWhenPaused = -1;
		}

		super.onResume();
	}

	@Override
	public boolean onError(MediaPlayer player, int arg1, int arg2) {
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		this.finish();
	}
}