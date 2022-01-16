package com.organic.simulation;

/*
 * @Description 
 * GOLCell is a representation of a cell in a GOLCell world. 
 */
public class GOLCell {
int state =0;
int x=-1,y=-1;//used for debugging purpose

public GOLCell() {
	
}

public GOLCell(int x, int y) {
	this.x = x;
	this.y = y;
}
/*
 * @Return 0 if dead, 1 if alive
 * 
 */
int getState() {
	return state;
}
/*
 * @Parameter should be 0 if dead, 1 if alive 
 */
public void setState(int state) {
	this.state = state;
}

public String getLocation() {
	return "x:"+x+",y:"+y;
}
public boolean isAlive() {
	if(state==1) {return true;}
	return false;
}
}
