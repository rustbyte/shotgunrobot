package com.rustbyte;

public class ZombieMobFactory extends MobFactory {
	private static ZombieMobFactory zombieMobFactory;
	
	private ZombieMobFactory(){}
	
	public static ZombieMobFactory getInstance(){
		if(zombieMobFactory == null){
			zombieMobFactory = new ZombieMobFactory();
		}
		return zombieMobFactory;
	}
	
	@Override
	public Mob spawnMob(int x, int y, int w, int h, Entity ent, Game game) {
		// TODO Auto-generated method stub
		return new Zombie(x, y, w, h, ent, game);
	}

}