package me.qbb84.braingame2.Utils;

import net.md_5.bungee.api.ChatColor;

public record Color() {

  public static String to(String message) {
    return ChatColor.translateAlternateColorCodes('&', message);
  }
}
