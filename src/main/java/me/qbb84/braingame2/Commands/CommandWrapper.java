package me.qbb84.braingame2.Commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface CommandWrapper {
  String commandName();
}
