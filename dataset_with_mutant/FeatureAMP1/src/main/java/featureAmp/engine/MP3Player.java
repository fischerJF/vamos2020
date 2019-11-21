package featureAmp.engine; 

import java.io.File; 
import java.io.IOException; 
import java.util.Observable; 

import javax.sound.sampled.AudioFormat; 
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.BooleanControl; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.FloatControl; 
import javax.sound.sampled.LineEvent; 
import javax.sound.sampled.LineListener; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 

import javazoom.jl.decoder.JavaLayerException; 

import com.mpatric.mp3agic.InvalidDataException; 
import com.mpatric.mp3agic.UnsupportedTagException; 

/**
 * A simple MP3Player, which is able to play an .mp3-file and pause, resume, and stop the playblack.
 */
public  class  MP3Player  extends Observable {
	
	public final static int STATUS_READY 	= 0;

	
	public final static int STATUS_PLAYING 	= 1;

	
	public final static int STATUS_PAUSED 	= 2;

	
	public final static int STATUS_FINISHED = 3;

	

	private int playerStatus = STATUS_READY;

	
	private AudioInputStream audioStream;

	
	private Clip clip;

	
	private FloatControl volumeControl;

	
	private BooleanControl muteControl;

	


	/**
	 * Creates a new MP3Player from given InputStream.
	 * @param filename
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 * @throws LineUnavailableException
	 * @throws InvalidDataException 
	 * @throws UnsupportedTagException 
	 */
	public MP3Player(String filename) throws UnsupportedAudioFileException, IOException, LineUnavailableException  {
		if (specifications.Configuration.base) {
			File file = new File(filename);
	
			audioStream = AudioSystem.getAudioInputStream(file);
			AudioFormat baseFormat = audioStream.getFormat();
			AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
			AudioInputStream stream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);
			clip = AudioSystem.getClip();
			clip.open(stream);
			clip.addLineListener(new LineListener() {
				
				@Override
				public void update(LineEvent event) {
					if(event.getType() == LineEvent.Type.STOP){
						if(clip.getLongFramePosition() >= clip.getFrameLength()){
							stop();
							playerStatus = STATUS_FINISHED;
							setChanged();
							notifyObservers();
						}
					}
				}
			});
			
			volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			muteControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
				}
	}

	

	/**
	 * Starts playback (resumes if paused).
	 * @throws JavaLayerException when there is a problem decoding the file.
	 */
	public void play() {
		synchronized (clip) {
			if(clip != null && clip.isOpen()) {
				clip.start();
				playerStatus = STATUS_PLAYING;
			}
		}
	}

	

	/**
	 * Pauses playback.
	 */
	public void pause() {
		synchronized (clip) {
			if (playerStatus == STATUS_PLAYING) {
				clip.stop();
				playerStatus = STATUS_PAUSED;
			}
		}
	}

	

	/**
	 * Resumes playback.
	 */
	public void resume() {
		synchronized (clip) {
			if (playerStatus == STATUS_PAUSED) {
				clip.start();
				playerStatus = STATUS_PLAYING;
			}
		}
	}

	

	/**
	 * Stops playback. If not playing, does nothing.
	 */
	public void stop() {
		synchronized (clip) {
			clip.stop();
			clip.setFramePosition(0);
			playerStatus = STATUS_FINISHED;
		}
	}

	

	/**
	 * Returns the current player status.
	 * @return the player status
	 */
	public int getPlayerStatus() {
		return this.playerStatus;
	}

	

	/**
	 * Closes the player, regardless of current state.
	 */
	public void close() {
		synchronized (clip) {
			if(clip != null && clip.isOpen()) {
				clip.stop();
				clip.close();
			}
		}
		try {
			audioStream.close();
			audioStream = null;
		} catch (IOException e) {
			// we are terminating, thus ignore exception
		}
	}

	
	
	public long getLength(){
		return clip.getMicrosecondLength()/1000000;
	}

	
	
	public long getPositionInSeconds(){
		return clip.getMicrosecondPosition()/1000000;
	}

	
	
	public FloatControl getVolumeControl(){
		return volumeControl;
	}

	

	public void setMute(boolean mute) {
		muteControl.setValue(mute);
	}


}
