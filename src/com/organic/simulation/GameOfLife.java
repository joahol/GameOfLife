package com.organic.simulation;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

/*
 * Implementation of John Horton Convay's algorithm Game Of Life.
 * 
 * JH 2022
 */
public class GameOfLife extends Application {

	/* Rules 
	 A cell is eighter dead or alive, represented binary as 0 or 1
	 When a living cell touches one or none alive cell it will die
	 If a dead cell or multiple living cells in a neighbourhood is surrounded by living cells they will come alive
	 if a living cell is touching more then 3 living cell it will die
	*/
	private static GOLCell[][] golcells;
	private static Group rectangles;
	static Scene rectScene;
	static int xSize;
	static int ySize;
	
	public GameOfLife() {
		this.xSize = 25;
		this.ySize = 25;
	}

	
	/*
	 * @Description
	 * Checks if all cells surrounding a cell is alive
	 * 
	 * @Deprecated 
	 * No need for this function
	 */
	@Deprecated
	static boolean SurroundingNeighboursAlive(List<GOLCell> neighbours){
		boolean result = true;
		for(GOLCell gc : neighbours) {
			if(!gc.isAlive()) {
				result = false;
				break;
			}
		}
		return result;
	}
	
	
	/*
	 * @description
	 * Count all neighbour cell's that is eighter alive or dying.
	 * @return int alive or dying neighbours 
	 */
	static int countAliveNeighbours(ArrayList<int[]> neighbours) {
		int count=0;
		for(int[] gc: neighbours) {
			if(golcells[gc[0]][gc[1]].getState()==1||golcells[gc[0]][gc[1]].getState()==2) {
				count++;
			}
		}
		return count;
	}

	/*
	 * @Description
	 * Create a list of cells in a grid that is neighbour to the current cell
	 * 
	 * @Return
	 * ArrayList of int[]array, where each intarray concist of a x,y coordinate in the grid
	 * 
	 */
	private static ArrayList<int[]> generateNeighbourList(int xC, int yC) {
		ArrayList<int[]> neighbours = new ArrayList<int[]>();
			for(int x=xC-1; x<=xC+1;x++ ) {
				for(int y=yC-1; y<=yC+1;y++) {
					
					if(isVithinBounds(x,y,25,0)) {
						if(x==xC&&y==yC) {
						}else {
						int[] coord = {x,y};
						neighbours.add(coord);
						}
					}
				}
			}
		return neighbours;
	}
	
	/*
	 * 
	 * @Description
	 * Tool to verify a coordinate is vithin the boundary of the grid
	 * @Return true if point is vithin boundary, othervise false 
	 */
	public static boolean isVithinBounds(int x, int y,int maxBound, int minBound) {
		if(x>=minBound && x<maxBound && y>=minBound && y<maxBound) {
			return true;
		}else {
			return false;
		}
	}



	@Override
	public void start(Stage primaryStage) throws Exception {
	
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gol.fxml"));

        rectangles = new Group();
        for(int i=0;i<25;i++){
            for(int j=0;j<25;j++){
                javafx.scene.shape.Rectangle r = new  javafx.scene.shape.Rectangle();
                r.setWidth(25.0);
                r.setHeight(25.0);
                r.setX(i*25);
                r.setY(25*j);
                r.setStroke(Color.GREEN);
                r.setFill(Color.BLACK);
               // rects.add(r);
                rectangles.getChildren().add((Node)r);
            }
        }
        
        rectScene = new Scene(rectangles,600,600);
        final Timeline tc = new Timeline(
        		new KeyFrame(javafx.util.Duration.ZERO, new EventHandler() {
        		@Override 
        		public void handle(Event event) {
        		
					for(int yC =0;yC<ySize;yC++) {
						for(int xC=0;xC<xSize;xC++) {
							int listpos = calculateListPositionFromXY(xC,yC, 25);
							int[] coord = calculateXYArrayCord(listpos,25);
							//System.out.println("ListPosition:"+listpos+" x:"+coord[0]+", y:"+coord[1]+" xC_"+xC+" yc "+yC);
							//System.out.println(xC+","+yC);
						
							ArrayList<int[]> neighbours = generateNeighbourList(xC,yC);
							//boolean allNeighbourAlive = SurroundingNeighboursAlive(neighbours);
							
							int numAliveNeighbours = countAliveNeighbours(neighbours);
							if(numAliveNeighbours>0) {
								System.out.println("numalive"+numAliveNeighbours+"ListPosition:"+listpos+" x:"+coord[0]+", y:"+coord[1]+" xC_"+xC+" yc "+yC+ " numneighbours:"+neighbours.size());
							}else {
								//System.out.println("nSize"+numAliveNeighbours);
							}
							//The cell is alive
							if(golcells[xC][yC].getState()==1) {
								//Underpopulation. A live cell dies if it connects to only one alive cell
								if(numAliveNeighbours<2) 
								{
									golcells[xC][yC].setState(2);	
									((Rectangle)rectangles.getChildren().get(listpos)).setFill(Color.ALICEBLUE);
								}
								else if(numAliveNeighbours>3) 
								{//overpopulation. This cell dies if it has more then 3 alive neighbours
									golcells[xC][yC].setState(2);
									((Rectangle)rectangles.getChildren().get(listpos)).setFill(Color.YELLOW);
								}else{
									((Rectangle)rectangles.getChildren().get(listpos)).setFill(Color.BLUE);
									golcells[xC][yC].setState(2);
								}
								
							}else if(golcells[xC][yC].getState()==0){//The cell is dead
								if(numAliveNeighbours==3) {//If the cell is dead and has 3 alive neighbour it comes to life
									golcells[xC][yC].setState(1);
									
									((Rectangle)rectangles.getChildren().get(listpos)).setFill(Color.WHITE);
								}else 
								{
									//System.out.println("The cell is dead, alive neighbours:"+numAliveNeighbours);
									
								}
								
							}else {//gocells state should be 2
								golcells[xC][yC].setState(0);
								System.out.println("Cell dies in this iteration");
								((Rectangle)rectangles.getChildren().get(listpos)).setFill(Color.BLACK);
								
							}
						}
					}
				}	
        		}), new KeyFrame(javafx.util.Duration.millis(1000))
        		);

        golcells[5][15].setState(1);
        golcells[5][14].setState(1);
        golcells[9][15].setState(1);
        golcells[9][14].setState(1);
        golcells[10][14].setState(1);
        golcells[9][13].setState(1);
        golcells[9][12].setState(1);
        golcells[3][2].setState(1);
        golcells[3][3].setState(1);
        golcells[3][4].setState(1);
        primaryStage.setTitle("Game Of Life");
        primaryStage.setScene(rectScene);
        primaryStage.show();
        tc.setCycleCount(14);
        tc.setDelay(javafx.util.Duration.millis(1000));
        tc.play();
	}
	
	
	public static void main(String[] args) {
		golcells = new GOLCell[25][25];
		for(int i=0;i<25;i++) {
			for(int j=0;j<25;j++) {
				golcells[i][j] = new GOLCell();
			}
		}
	launch(args);
	}
	
	/*
	 * @Description
	 * Converts a list of rectangles to a 2Dimensional array of rectangles
	 * 
	 * @Param Generic List of type javafx.shape.Rectangle objects
	 * @Return 2Dimensional array of javafx.shape.Rectangle objects
	 */
	public Rectangle[][] convertRectangleListTo2DRectangleArray(List<Rectangle> rectangleList){
		Rectangle rectanglesArr[][]= new Rectangle[xSize][ySize];
		int columns = rectangleList.size()/5;
		int rows = rectangleList.size()/5;
		int counter =0;
		int x=0,y=0;
		for(int i=0;i<rectangleList.size();i++) {
			rectanglesArr[y][x]=rectangleList.get(i);
			x++;
			if(x==24) {
				x=0;
				y++;
			}
		}
		return rectanglesArr;
	}
	
	/*
	 * @Description calculate corresponding coordinate for a given point in a list to a point in a 2Dimensional array
	 * @Param point in the list, and size of the quadratic array
	 */
	public int[] calculateXYArrayCord(int arrayIndex, int size) {
	
		int x=0,y=0;
		int mod = arrayIndex % size;
		if(arrayIndex<size) {
			x=arrayIndex;
			y=0;
		}else {
		y = (int)arrayIndex/size;
		x=mod;
		}
		return new int[] {x,y};	
	}
	
	
	/*
	 *@Description calculate list position from x,y coordinate of a similar 2dimensional representation 
	 * 
	 */
	public int calculateListPositionFromXY(int x, int y, int size) {
		int retval=-1;
			retval = (y*size)+x;
		return retval;
	}
}
