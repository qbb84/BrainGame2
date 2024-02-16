package me.qbb84.braingame2.Game;

import me.qbb84.braingame2.Game.BrainGames.DualNBack;
import me.qbb84.braingame2.Inventory.CustomInventory;
import org.bukkit.entity.Player;

public class GameManager {

  private static GameManager INSTANCE = null;


  private GameManager() {}

  public static GameManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new GameManager();
    }
    return INSTANCE;
  }

  public CustomInventory openInventory(Player handler) {
    return new CustomInventory(handler);
  }

  public void initializeItems(){
    new DualNBack();
  }
}
