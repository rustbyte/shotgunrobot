package com.rustbyte;

import java.util.ArrayList;
import java.util.List;

import com.rustbyte.level.EmptyTile;
import com.rustbyte.level.Level;
import com.rustbyte.level.Tile;

public class PathFinder {
	public class Node {
		public Node parent;
		public Tile tile;
		public int costFromStart;
		public int costToGoal;
		public int totalCost;
		
		public Node(Node p, Tile t) {
			parent = p;
			tile = t;
		}

		public void calcTotalCost() {
			totalCost = costFromStart + costToGoal;
		}
	}
	
	private Level level = null;	
	public List<Node> open = new ArrayList<Node>();
	public List<Node> closed = new ArrayList<Node>();
	private Node startNode = null;
	private Node targetNode = null;
	private boolean targetReached = false;
	private boolean searchCompleted = false;
	
	public PathFinder(Level l) {
		level = l;
	}
	
	private Node nodeInOpenList(Tile t) {
		for(int i=0; i < open.size();i++) {
			Node node = open.get(i);
			if(node.tile == t)
				return node;
		}
		return null;
	}
	private boolean nodeInClosedList(Tile t) {
		for(int i=0; i < closed.size();i++) {
			Node node = closed.get(i);
			if(node.tile == t)
				return true;
		}
		return false;
	}
	private void removeFromOpen(Node n) {
		for(int i=0; i < open.size(); i++) {
			Node node = open.get(i);
			if(node.tile == n.tile){
				open.remove(node);
				return;
			}
		}
	}
	private void removeFromClosed(Tile t) {
		for(int i=0; i < closed.size(); i++) {
			Node node = closed.get(i);
			if(node.tile == t){
				closed.remove(node);
				return;
			}
		}
	}	
	private int calcTargetCost(Node a, Node b) {
		int x = b.tile.tx - a.tile.tx; // num tiles to move on X
		int y = b.tile.ty - a.tile.ty; // num tiles to move on Y	
		
		int cost = 1;					
		return (Math.abs(x) + Math.abs(y)) * cost;		
	}
	private int calculateCost(Node a, Node b) {
		int x = b.tile.tx - a.tile.tx; // num tiles to move on X
		int y = b.tile.ty - a.tile.ty; // num tiles to move on Y	
		
		int cost = 1;					
		if( ((b.tile.tx > a.tile.tx) || (b.tile.tx < a.tile.tx) ) && b.tile.ty < a.tile.ty) {
			Tile t = level.getTile(b.tile.tx, b.tile.ty + 1);			
			if(t != null && !t.blocking)
				cost = 100;
		}
		
		return (Math.abs(x) + Math.abs(y)) * cost;
	}
	private boolean pathBlocked(int xx, int yy) {
		Tile td = level.getTile(xx,yy);
		if(td != null ) {
			if(td.blocking)
				return true;
		}
		return false;
	}
	private List<Node> buildPath(Node target) {
		List<Node> path = new ArrayList<Node>();
		
		Node nextNode = target;
		while(nextNode != null) {
			path.add(nextNode);
			nextNode = nextNode.parent;
		}
		return path;
	}
	public void initSearch(int ax, int ay, int bx, int by) {
		open.clear();
		closed.clear();
		
		targetReached = false;
		searchCompleted = false;
		startNode = new Node(null, level.getTile(ax, ay));
		targetNode = new Node(null, level.getTile(bx, by));				
		startNode.costFromStart = 0;
		startNode.costToGoal = calcTargetCost(startNode, targetNode);
		startNode.calcTotalCost();
		
		open.add(startNode);		
	}	
	public void step() {
		if(!open.isEmpty() && !targetReached) {
			//System.out.println("open: " + open.size() + " closed: " + closed.size());
			
			Node cheapestNodeTotal = open.get(0);
			Node cheapestNodeCostToGoal = open.get(0);
			int idx1 = 0;
			int idx2 = 0;
			for(int i=0; i < open.size(); i++) {
				Node n = open.get(i);
				if(n.totalCost <= cheapestNodeTotal.totalCost)  {					
					cheapestNodeTotal = n;
					idx1 = i;
				}
				if(n.costToGoal <= cheapestNodeCostToGoal.costToGoal){
					cheapestNodeCostToGoal = n;
					idx2 = i;
				}
			}	
			Node node;
			if((cheapestNodeCostToGoal.totalCost >= cheapestNodeTotal.totalCost) &&
			   (cheapestNodeCostToGoal.costToGoal < cheapestNodeTotal.costToGoal)) {
				node = open.get(idx2); 
				removeFromOpen(node);							
			} else {
				node = open.get(idx1); 
				removeFromOpen(node);			
			}
			
			if(node.tile == targetNode.tile) {
				// done, build path and exit
				System.out.println("Path finished. Target reached.");
				searchCompleted = true;
				targetReached = true;
				targetNode = node;
			} else {
				for(int i=0; i < 9; i++) {
					int xo = node.tile.tx;
					int yo = node.tile.ty;
					int xx = (xo - 1) + i % 3;
					int yy = (yo - 1) + i / 3;
					if( xx < 0 || xx >= level.width || yy < 0 || yy >= level.height)
						continue;					
					Tile t = level.getTile(xx, yy);
					if(t == node.tile) {						
						continue;
					}
					if(t.blocking)
						continue;
					
					if( xx > xo && yy > yo ) {
						if(pathBlocked(xx, yy - 1))
							continue;
					}
					if( xx > xo && yy < yo ) {
						if(pathBlocked(xx, yy + 1))
							continue;						
					}
					if( xx < xo && yy > yo ) {
						if(pathBlocked(xx, yy - 1))
							continue;
					}
					if( xx < xo && yy < yo ) {
						if(pathBlocked(xx, yy + 1))
							continue;						
					}
					
					Node n = nodeInOpenList(t);
					if(n != null) {
						int newCost = node.costFromStart + calculateCost(node, n );
						if(newCost <= n.costFromStart){
							removeFromOpen(n);
							n.costFromStart = newCost;
							n.costToGoal = calcTargetCost(n, targetNode);
							n.parent = node;
							n.calcTotalCost();
							open.add(n);
						}
					} else {						
						if(!nodeInClosedList(t)) {													
							Node newNode = new Node(node, t);
							newNode.costFromStart = node.costFromStart + (calculateCost(node, newNode ));
							newNode.costToGoal = calcTargetCost(newNode, targetNode);
							newNode.calcTotalCost();	
							open.add(newNode);
						} 
					}
				}
				closed.add(node);
			}
		} else {
			searchCompleted = true;
		}
	}
	public List<Node> findPath(int ax, int ay, int bx, int by) {		
		int steps = 0;
		initSearch(ax, ay, bx, by);
		
		while(!searchCompleted) {
			step();
			steps++;
		}
		
		System.out.println("pathfinder completed in " + steps + " steps.");
		if(targetReached)
			return buildPath(targetNode);
		else
			return closed;
	}	
}
