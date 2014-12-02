package com.rustbyte;

public abstract class MobFactory {
	public abstract Mob spawnMob(int x, int y, int w, int h, Entity ent, Game game); 
}