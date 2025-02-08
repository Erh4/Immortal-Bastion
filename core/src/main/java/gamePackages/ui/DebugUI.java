package gamePackages.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import gamePackages.entities.Clam_Monster;
import gamePackages.entities.Entity;
import gamePackages.entities.Player;
import gamePackages.toolsManager.Node;
import gamePackages.toolsManager.PathfindingManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DebugUI {

    public void renderHitbox(Entity entity) {
        float X = entity.getCollisionRectangle().getX();
        float Y = entity.getCollisionRectangle().getY();
        float W = entity.getCollisionRectangle().getWidth();
        float H = entity.getCollisionRectangle().getHeight();

        entity.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        entity.getShapeRenderer().setColor(1f, 0f, 0f, 1f);
        entity.getShapeRenderer().rect(X, Y, W, H);
        entity.getShapeRenderer().end();
    }


    public void renderAttackZone(Entity entity) {
        float X = entity.getAttackRectangle().getX();
        float Y = entity.getAttackRectangle().getY();
        float W = entity.getAttackRectangle().getWidth();
        float H = entity.getAttackRectangle().getHeight();

        entity.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        entity.getShapeRenderer().setColor(0f, 1f, 0f, 1f);
        entity.getShapeRenderer().rect(X, Y, W, H);
        entity.getShapeRenderer().end();
    }

    public void renderMeleeRange(Entity entity) {
        entity.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        entity.getShapeRenderer().setColor(0f, 0.5f, 1f, 0.3f);
        entity.getShapeRenderer().circle(entity.getPosX()+entity.getWidth()/2f, entity.getPosY()+entity.getHeight()/2f, entity.getMeleeAttackRange());
        entity.getShapeRenderer().end();
    }

    public void entityHitboxRenderer(OrthographicCamera camera, Entity entity, Entity entityOther, PathfindingManager PathfindingMap) {
        if (entity.getDebugDisplayState()) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            entity.getShapeRenderer().setProjectionMatrix(camera.combined);
            renderHitbox(entity);
            renderDetectionShape(entity);
            renderMeleeRange(entity);
            renderAttackZone(entity);
            renderPathfinding(camera, entity, entityOther, PathfindingMap);
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    public void entityHitboxRenderer(OrthographicCamera camera, Player entity, PathfindingManager PathfindingMap) {
        if (entity.getDebugDisplayState()) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            entity.getShapeRenderer().setProjectionMatrix(camera.combined);
            renderHitbox(entity);
            renderDetectionShape(entity);
            renderMeleeRange(entity);
            renderAttackZone(entity);
            renderPathfinding(camera, entity, PathfindingMap);
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    public void renderDetectionShape(Entity entity) {
        entity.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        entity.getShapeRenderer().setColor(1,1,1,1);
        entity.getShapeRenderer().circle(entity.getPosX()+entity.getWidth()/2f, entity.getPosY()+entity.getHeight()/2f, entity.getDetectionRadius());
        entity.getShapeRenderer().end();
    }

    public void renderPathfinding(OrthographicCamera camera, Entity entity, PathfindingManager PathfindingMap) {

                entity.getShapeRenderer().setProjectionMatrix(camera.combined);
                int tempAX = (int)entity.getCenterX()/16- (int)(PathfindingMap.getMaxCol()/2f);
                int tempAY = (int)entity.getCenterY()/16- (int)(PathfindingMap.getMaxRow()/2f);
                entity.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
                entity.getShapeRenderer().setColor(Color.WHITE);
                int i=0, j =0;
                for (Node[] row : PathfindingMap.getNode()) {
                    for (Node node : row) {
                        if (node.isSolid()) {
                            entity.getShapeRenderer().setColor(Color.RED);
                        }
                        entity.getShapeRenderer().circle((i+tempAX)*16,(j+tempAY)*16, 1);
                        if (!entity.getShapeRenderer().getColor().equals(Color.WHITE))
                        {
                            entity.getShapeRenderer().setColor(Color.WHITE);
                        }
                        j++;

                    }
                    j=0;
                    i++;
                }

                entity.getShapeRenderer().end();

    }

    public void renderPathfinding(OrthographicCamera camera, Entity entity, Entity entityDest, PathfindingManager PathfindingMap) {
        if (entity.getPlayerDetected() && entity.getDebugDisplayState()) {
            entity.getShapeRenderer().setProjectionMatrix(camera.combined);
            entity.getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
            entity.getShapeRenderer().setColor(0,1,1f, 1);
            //System.out.println(entity.getPathNodes().size());
            if (!entity.getPathNodes().isEmpty()) {

                int xTempDest = (int)entityDest.getCenterX()/16;
                int yTempDest = (int)entityDest.getCenterY()/16;

                Iterator<Node> NodeIterator = entity.getPathNodes().iterator();
                Node old_node = NodeIterator.next();
                while (NodeIterator.hasNext()) {
                    Node node = NodeIterator.next();


                    float sourceX = xTempDest+old_node.getX()-  (int)(PathfindingMap.getMaxCol()/2f);
                    float sourceY = old_node.getY()+yTempDest-(int)((PathfindingMap.getMaxRow()-2)/2f)-1;

                    float targetX = xTempDest+node.getX()-(int)(PathfindingMap.getMaxCol()/2f);
                    float targetY = node.getY()+yTempDest-(int)((PathfindingMap.getMaxRow()-2)/2f)-1;

                    entity.getShapeRenderer().line(sourceX*16, sourceY*16,  targetX*16, targetY*16);
                    old_node = node;
                }
            }

        }
        entity.getShapeRenderer().end();

    }

}
