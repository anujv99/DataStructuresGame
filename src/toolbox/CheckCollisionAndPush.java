package toolbox;

import entities.Entity;
import player.Movement;

public class CheckCollisionAndPush {

    private static float scalePlayer_X = 0.05f;
    private static float scalePlayer_y = 0.05f;
    private static float scaleTile = 0.1f;

    public static boolean checkCollision(Entity a) {

        float dx = Math.abs(a.getPosition().x);
        float dy = Math.abs(a.getPosition().y);

        dx -= scalePlayer_X + scaleTile;
        dy -= scalePlayer_y + scaleTile;

        if (dx < 0 && dy < 0) {
            return true;
        } else {
            return false;
        }

    }

    public static void push (Entity collidor) {
        float pushX = collidor.getPosition().x;
        float pushY = collidor.getPosition().y;

        if (Math.abs(pushX) < Math.abs(pushY)) {
            if (pushY < 0) {
                float amountToPush = Math.abs(pushY) - scalePlayer_y;
                amountToPush = scaleTile - amountToPush;
                Movement.increaseVelocity(0,-Movement.getVelocity().y - amountToPush,0);
                Movement.velocity.y = -0.00005f;
            } else {
                float amountToPush = Math.abs(pushY) - scalePlayer_y;
                amountToPush = scaleTile - amountToPush;
                Movement.increaseVelocity(0,Movement.getVelocity().y + amountToPush,0);
            }
        } else {
            if (pushX > 0) {
                float amountToPush = Math.abs(pushX) - scalePlayer_X;
                amountToPush = scaleTile - amountToPush;
                Movement.increaseVelocity(-Movement.getVelocity().x + amountToPush,0,0);
            } else {
                float amountToPush = Math.abs(pushX) - scalePlayer_X;
                amountToPush = scaleTile - amountToPush;
                Movement.increaseVelocity(Movement.getVelocity().x - amountToPush,0,0);
            }
        }
    }
}
