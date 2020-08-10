package toolbox;

import org.lwjgl.util.vector.Vector3f;

public class CollisionCheck {

    public static boolean check(Vector3f collidor_a, Vector3f collidor_b) {
        float dx = Math.abs(collidor_a.x - collidor_b.x);
        float dy = Math.abs(collidor_a.y - collidor_b.y);

        dx -= collidor_b.z + collidor_a.z;
        dy -= collidor_b.z + collidor_a.z;

        if (dx < 0 && dy < 0) {
            return true;
        } else {
            return false;
        }

    }

}
