package net.estra.EstraCore.model.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDmEvent extends Event {

    private Player sender;
    private Player reciever;
    private String message;

    public PlayerDmEvent(Player sender, Player reciever, String msg) {
        this.sender = sender;
        this.reciever = reciever;
        this.message = msg;
    }

    public Player getReciever() {
        return reciever;
    }

    public Player getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
