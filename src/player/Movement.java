package player;

import audio.AudioHandler;
import entities.Camera;
import entities.Entity;
import gui.GuiHandler;
import level.LevelHandler;
import level.LevelLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import toolbox.*;

public class Movement {

    public static Vector3f velocity = new Vector3f(0,0,0);
    private static float speed = 0.0012f;
    private static float gravity = 0.00005f;
    private static float jumpVelocity = 0.002f;

    public static boolean shootDirection;

    private static int totalPowerIndex = 400;

    public static Vector3f position = new Vector3f(0,0,0);

    private static Power power;
    private static BulletHandler bulletHandler;
    private static Health health;

    private static int toDecrese = 0;

    public static void create(Loader loader) {
        power = new Power(loader);
        bulletHandler = new BulletHandler(loader);
        health = new Health(loader,new Vector3f(-0.95f,0.5f,-1.49999f));
    }

    public static void move(LevelLoader levelLoader, Camera camera, Player player, Renderer renderer, StaticShader shader) {
        toDecrese++;

        for (int i=0;i<levelLoader.getEnemies().size();i++) {

            EnemyPlayerLoader enemyPlayerLoader = levelLoader.getEnemies().get(i);

            Entity entity_a = enemyPlayerLoader.run.get(0);
            Vector3f collidor_a = new Vector3f(entity_a.getPosition().x, entity_a.getPosition().y, 0.02f);
            if (CollisionCheck.check(collidor_a, new Vector3f(0, 0, 0.05f)) && toDecrese > 200) {
                System.out.println("Collision Detected");
                toDecrese = 0;
                health.health--;
            }

            if (enemyPlayerLoader.shouldRemove) {
                levelLoader.getEnemies().remove(i);
            }

            checkBulletCollision(collidor_a, enemyPlayerLoader, levelLoader);
            enemyPlayerLoader.render(renderer,shader);
        }

        if (!Keyboard.isKeyDown(Keyboard.KEY_SPACE) && totalPowerIndex < 400) {
            totalPowerIndex++;
        }
        power.totalPower = totalPowerIndex / 80;
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            shootDirection = true;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            shootDirection = false;
        }
        if (health.health <= 0) {
            velocity.x = 0;
            velocity.y = 0;
            if (player.index >= 399) {
                AudioHandler.pauseSound("LEVEL_" + LevelHandler.currentLevel + "_MAIN");
                LevelHandler.isPlayRequested = false;
                GuiHandler.whichMenuToRender = "GAME_COMPLETE";
                GuiHandler.shouldRenderGUI = true;
            }
            player.die(renderer,shader);
        } else if (Mouse.isButtonDown(0)) {
            bulletHandler.addBullet(new Vector3f(0, 0, -1.5f));
            if (shootDirection) {
                player.shoot(renderer, shader);
            } else {
                player.shootReverse(renderer, shader);
            }
            velocity.x = 0;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && totalPowerIndex <= 400 && totalPowerIndex > 0) {
            totalPowerIndex -= 2;
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                player.jump(renderer, shader);
                velocity.x = -speed;
                velocity.y = -jumpVelocity;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                player.jumpReverse(renderer, shader);
                velocity.x = speed;
                velocity.y = -jumpVelocity;
            } else {
                player.jump(renderer, shader);
                velocity.y = -jumpVelocity;
            }
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            velocity.x = -speed;
            player.run(renderer, shader);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            velocity.x = speed;
            player.runRevere(renderer, shader);
        } else {
            velocity.x = 0;
            player.idle(renderer, shader);
        }
        for (Entity entity : levelLoader.getCollidors()) {
            if (CheckCollisionAndPush.checkCollision(entity)) {
                CheckCollisionAndPush.push(entity);
            }
        }
        for (Entity entity : levelLoader.getToRender()) {
            entity.increasePosition(velocity.x, velocity.y, velocity.z);
        }
        for (Entity entity : levelLoader.getCollidors()) {
            entity.increasePosition(velocity.x, velocity.y, velocity.z);
        }
        for (Entity entity : power.toRender) {
            entity.increasePosition(velocity.x, velocity.y, velocity.z);
        }
        for (Entity entity : health.toRender) {
            entity.increasePosition(velocity.x, velocity.y, velocity.z);
        }
        power.text.increasePosition(velocity.x, velocity.y, velocity.z);
        health.text.increasePosition(velocity.x, velocity.y, velocity.z);
        camera.increasePosition(velocity.x, velocity.y, velocity.z);
        position.x += velocity.x;
        position.y += velocity.y;
        position.z += velocity.z;
        increaseVelocity(0, gravity, 0);
        bulletHandler.render(renderer, shader);
        power.render(renderer, shader);
        health.render(renderer, shader);
    }

    public static void increaseVelocity(float dx, float dy, float dz) {
        velocity.x += dx;
        velocity.y += dy;
        velocity.z += dz;
    }

    public static Vector3f getVelocity() {
        return velocity;
    }

    public static void setGravity(float value) {
        gravity = value;
    }

    public static float getGravity() {
        return gravity;
    }

    private static void checkBulletCollision(Vector3f collidor_a, EnemyPlayerLoader enemyPlayerLoader, LevelLoader levelLoader) {
        for (int i=0;i<bulletHandler.bullets.size();i++) {
            Entity bullet = bulletHandler.bullets.get(i);
            for (int j=0;j<levelLoader.getCollidors().size();j++) {
                Entity entity = levelLoader.getCollidors().get(j);
                if (bullet.getPosition().x > entity.getPosition().x - entity.getScale() && bullet.getPosition().x < entity.getPosition().x + entity.getScale()
                        && bullet.getPosition().y > entity.getPosition().y - entity.getScale() &&
                        bullet.getPosition().y < entity.getPosition().y + entity.getScale()) {
                    bulletHandler.bullets.set(i, bulletHandler.bullets.get(bulletHandler.bullets.size() - 1));
                    bulletHandler.bullets.remove(bulletHandler.bullets.size() - 1);
                    AudioHandler.playSound("PLAYER_BULLET_HIDDEN");
                }
            }
            if (bullet.getPosition().y < collidor_a.y + 4*collidor_a.z && bullet.getPosition().y > collidor_a.y && bullet.getPosition().x > collidor_a.x - collidor_a.z && bullet.getPosition().x < collidor_a.x + collidor_a.z) {
                bulletHandler.bullets.set(i, bulletHandler.bullets.get(bulletHandler.bullets.size() - 1));
                bulletHandler.bullets.remove(bulletHandler.bullets.size() - 1);
                enemyPlayerLoader.health = enemyPlayerLoader.health - 1;
            }
        }

        for (int i=0;i<bulletHandler.bulletsF.size();i++) {
            Entity bullet = bulletHandler.bulletsF.get(i);
            for (int j=0;j<levelLoader.getCollidors().size();j++) {
                Entity entity = levelLoader.getCollidors().get(j);
                if (bullet.getPosition().x > entity.getPosition().x - entity.getScale() && bullet.getPosition().x < entity.getPosition().x + entity.getScale()
                        && bullet.getPosition().y > entity.getPosition().y - entity.getScale() &&
                        bullet.getPosition().y < entity.getPosition().y + entity.getScale()) {
                    bulletHandler.bulletsF.set(i, bulletHandler.bulletsF.get(bulletHandler.bulletsF.size() - 1));
                    bulletHandler.bulletsF.remove(bulletHandler.bulletsF.size() - 1);
                    AudioHandler.playSound("PLAYER_BULLET_HIDDEN");
                }
            }
            if (bullet.getPosition().y < collidor_a.y + 4*collidor_a.z && bullet.getPosition().y > collidor_a.y && bullet.getPosition().x > collidor_a.x - collidor_a.z && bullet.getPosition().x < collidor_a.x + collidor_a.z) {
                bulletHandler.bulletsF.set(i, bulletHandler.bulletsF.get(bulletHandler.bulletsF.size() - 1));
                bulletHandler.bulletsF.remove(bulletHandler.bulletsF.size() - 1);
                enemyPlayerLoader.health = enemyPlayerLoader.health - 1;
            }
        }
    }
}
