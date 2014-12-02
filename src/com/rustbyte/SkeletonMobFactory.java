package com.rustbyte;

public class SkeletonMobFactory extends MobFactory {
	private static SkeletonMobFactory skeletonMobFactory;
	
	private SkeletonMobFactory(){}

	public static SkeletonMobFactory getInstance(){
		if(skeletonMobFactory == null){
			skeletonMobFactory = new SkeletonMobFactory();
		}
		return skeletonMobFactory;
	}

	@Override
	public Mob spawnMob(int x, int y, int w, int h, Entity ent, Game game) {
		return new Skeleton(x, y, w, h, ent, game);
	}
}