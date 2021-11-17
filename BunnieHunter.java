package me.remie.runex.hween;

import simple.api.coords.WorldPoint;
import simple.api.events.ChatMessageEvent;
import simple.api.listeners.SimpleMessageListener;
import simple.api.script.Category;
import simple.api.script.Script;
import simple.api.script.ScriptManifest;
import simple.api.script.interfaces.SimplePaintable;
import simple.api.wrappers.SimpleGameObject;
import simple.api.wrappers.SimpleGroundItem;
import simple.api.wrappers.SimpleNpc;

import java.awt.*;

/**
 * Created by Seth on November 11/17/2021, 2021 at 2:14 PM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707
 */
@ScriptManifest(author = "Reminisce", name = "RHween' Bunnies", category = Category.MONEYMAKING,
        version = "1.0", description = "Mines rubble and saves bunnies afterwards.", discord = "Reminisce#1707", servers = { "RuneX" })
public class BunnieHunter extends Script implements SimplePaintable, SimpleMessageListener {

    public String status;
    public long startTime;
    private int startTokens, startRocks;

    @Override
    public boolean onExecute() {
        this.status = "Waiting to start...";
        this.startTime = System.currentTimeMillis();
        this.startTokens = ctx.inventory.populate().filter(23819).population(true);
        this.startRocks = ctx.inventory.populate().filter(23824).population(true);
        return true;
    }

    @Override
    public void onProcess() {
        if (!ctx.pathing.reachable(new WorldPoint(3551, 3558, 0))) {
            return;
        }
        if (!ctx.npcs.populate().filter(3609).filter((n) -> n.getInteracting().equals(ctx.players.getLocal())).isEmpty()) {
            final SimpleNpc ghast = ctx.npcs.nextNearest();
            if (ghast != null) { // redundant check
                if (ctx.players.getLocal().getInteracting() == null) {
                    ghast.interact(20);
                    ctx.onCondition(() -> ctx.players.getLocal().getInteracting() != null, 250, 10);
                }
            }
            return;
        }
        if (!ctx.groundItems.populate().filter(23823).isEmpty()) {
            final SimpleGroundItem plasm = ctx.groundItems.nextNearest();
            if (plasm != null) { // redundant check
                final int cached = ctx.inventory.populate().filter(23823).population(true);
                plasm.interact();
                ctx.onCondition(() -> ctx.inventory.populate().filter(23823).population(true) > cached, 250, 10);
            }
            return;
        }
        if (ctx.players.getLocal().getAnimation() != 624) {
            final SimpleGameObject rock = (SimpleGameObject) ctx.objects.populate().filter(12749).nextNearest();
            if (rock != null) { // redundant check
                rock.interact(502);
                ctx.onCondition(() -> ctx.players.getLocal().getAnimation() == 624, 250, 10);
            }
        }
    }

    @Override
    public void onTerminate() {

    }

    @Override
    public void onPaint(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(5, 2, 192, 86);
        g.setColor(Color.decode("#ea411c"));
        g.drawRect(5, 2, 192, 86);
        g.drawLine(8, 24, 194, 24);

        g.setColor(Color.decode("#e0ad01"));
        g.drawString("RHween' Bunnies                       v. " + "1.0", 12, 20);
        g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 14, 42);
        g.drawString("Status: " + status, 14, 56);
        g.drawString("Tokens: " + startTokens + " (" + ctx.paint.formatValue(ctx.paint.valuePerHour(startTokens, startTime)) + ")", 14, 70);
        g.drawString("Rocks: " + startRocks + " (" + ctx.paint.valuePerHour(startRocks, startTime) + ")", 14, 84);
    }

    @Override
    public void onChatMessage(ChatMessageEvent event) {

    }

}
