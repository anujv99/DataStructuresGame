package audio;

import org.lwjgl.openal.AL10;

public class Source {

    private int sourceId;
    private int buffer;

    public Source(int buffer) {
        sourceId = AL10.alGenSources();
        this.buffer = buffer;
        AL10.alSourcef(sourceId,AL10.AL_GAIN,1);
        AL10.alSourcef(sourceId,AL10.AL_PITCH,1);
        AL10.alSource3f(sourceId,AL10.AL_POSITION,0,0,0);
    }

    public void play() {
        stop();
        AL10.alSourcei(sourceId,AL10.AL_BUFFER,buffer);
        continuePlaying();
    }

    public void pause() {
        AL10.alSourcePause(sourceId);
    }

    public void continuePlaying() {
        AL10.alSourcePlay(sourceId);
    }

    public void setVolume(float volume) {
        AL10.alSourcef(sourceId,AL10.AL_GAIN,volume);
    }

    public float getVolume() {
        return AL10.alGetSourcef(sourceId,AL10.AL_GAIN);
    }

    public void setPitch(float pitch) {
        AL10.alSourcef(sourceId,AL10.AL_PITCH,pitch);
    }

    public void stop() {
        AL10.alSourceStop(sourceId);
    }

    public void setLooping(boolean loop) {
        AL10.alSourcei(sourceId,AL10.AL_LOOPING,loop ? AL10.AL_TRUE : AL10.AL_FALSE);
    }

    public void delete() {
        stop();
        System.out.println("Source Removed");
        AL10.alDeleteSources(sourceId);
    }

}
