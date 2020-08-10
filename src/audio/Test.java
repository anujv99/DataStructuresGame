package audio;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        AudioMaster.init();

        int buffer = AudioMaster.loadSound("audio/levelTwo.wav");
        Source source = new Source(buffer);

//        char c = ' ';
//        while (c!='q') {
//            c = (char)System.in.read();
//            if (c == 'p'){
//                source.play(buffer);
//            }
//        }
        while (true) {
            char c = (char)System.in.read();
            if (c == 'a') {
                break;
            } else if (c == 'q') {
                source.play();
            } else if (c == 'r') {
                source.pause();
            } else if (c == 'e') {
                source.continuePlaying();
            } else if (c == 't') {
                source.stop();
            } else if (c == 'l') {
                source.play();
            }
        }

        source.delete();
        AudioMaster.cleanUP();

    }

}
