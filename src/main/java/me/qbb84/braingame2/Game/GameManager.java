package me.qbb84.braingame2.Game;

import me.qbb84.braingame2.Inventory.CustomInventory;
import org.bukkit.entity.Player;

public class GameManager {

  private static volatile GameManager INSTANCE;

  private GameManager() {}

  public static GameManager getInstance() {
    GameManager result = INSTANCE;
    if (result == null) {
      synchronized (GameManager.class) {
        result = INSTANCE;
        if (result == null) {
          INSTANCE = result = new GameManager();
        }
      }
    }
    return result;
  }

  public CustomInventory openInventory(Player handler) {
    return new CustomInventory(handler);
  }

  public void initializeItems() {
    new DualNBack();
    new Corsi();
    new CWM();
    new Memory();
    new MentalMath();
    new PASAT();
  }
}
