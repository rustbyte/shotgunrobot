package com.rustbyte;

public class HumanMobFactory extends MobFactory {
	private static HumanMobFactory humanMobFactory;
	
	private HumanMobFactory(){}
	
	public static HumanMobFactory getInstance(){
		if(humanMobFactory == null){
			humanMobFactory = new HumanMobFactory();
		}
		return humanMobFactory;
	}
	
	@Override
	public Mob spawnMob(int x, int y, int w, int h, Entity ent, Game game) {
		// TODO Auto-generated method stub
		return new Human(x, y, w, h, ent, game);
	}

}