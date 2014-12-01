package com.rustbyte;

public class BatBossMobFactory extends MobFactory {
	private static BatBossMobFactory batBossMobFactory;
	
	private BatBossMobFactory(){}
	
	public static BatBossMobFactory getInstance(){
		if(batBossMobFactory == null){
			batBossMobFactory = new BatBossMobFactory();
		}
		return batBossMobFactory;
	}
	
	@Override
	public Mob spawnMob(int x, int y, int w, int h, Entity ent, Game game) {
		return new BatBoss(x, y, 62, 41, ent, game);
	}

}