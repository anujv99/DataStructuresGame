package audio;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class AudioHandler {

    private static HashMap<String, Source> sounds = new HashMap<>();
    private static ArrayList<String> keys = new ArrayList<>();

    public static void loadSounds() {
        AudioMaster.init();
        loadData();
    }

    public static void playSound(String name) {
        sounds.get(name).play();
    }

    public static void pauseSound(String name) {
        sounds.get(name).pause();
    }

    public static void setVolume(String name, float value) {
        sounds.get(name).setVolume(value);
    }

    public static void continuePlaying(String name) {
        sounds.get(name).continuePlaying();
    }

    public static void stopSound(String name) {
        sounds.get(name).stop();
    }

    private static void loadData() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/audio/AudioData.txt")));
            String data;
            while ((data = br.readLine()) != null) {
                String[] currentLine = data.split(" ");
                int buffer = AudioMaster.loadSound("audio/audioEffects/" + currentLine[1] + ".wav");
                Source source = new Source(buffer);
                sounds.put(currentLine[0], source);
                keys.add(currentLine[0]);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setVolume("PLAYER_BULLET_HIDDEN",0.3f);
    }

    public static void changeVolume(float volume) {
        for (String string:keys) {
            Source source = sounds.get(string);
            source.setVolume(source.getVolume() + volume);
            if (source.getVolume() > 1) {
                source.setVolume(1);
            } else if (source.getVolume() < 0) {
                source.setVolume(0);
            }
        }
    }

    public static void deleteEverything() {
        for (String key:keys) {
            System.out.print("Sound: " + key + " ");
            sounds.remove(key).delete();
        }
        keys.clear();
        AudioMaster.cleanUP();
    }

}
