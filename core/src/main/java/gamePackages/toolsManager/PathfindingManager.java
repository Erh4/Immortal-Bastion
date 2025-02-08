package gamePackages.toolsManager;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import gamePackages.entities.Entity;
import gamePackages.entities.Player;

import java.util.*;


/**
 * Manages pathfinding using a grid-based system.
 *
 * The `PathfindingManager` class is responsible for managing nodes, calculating paths,
 * and updating pathfinding maps for entities in the game world.
 */
public class PathfindingManager {

    protected int maxCol;
    protected int maxRow;
    protected Node[][] node;
    protected Node startNode, goalNode, currentNode;
    protected ArrayList<Node> openList;
    protected ArrayList<Node> checkedList;
    protected boolean goalReached;
    protected int step;
    protected List<List<Integer>> myPathMap;
    protected List<Node> closed_list;
    protected List<Node> usedNodes;


    protected int numberOfStartNode;


    /**
     * Constructs a `PathfindingManager` with the given pathfinding map.
     *
     * @param PathfindingMap A 2D list of integers representing the pathfinding map.
     */
    public PathfindingManager(List<List<Integer>> PathfindingMap) {
        myPathMap = new ArrayList<>();
        closed_list = new ArrayList<>();

        step = 0;
        openList = new ArrayList<>();
        checkedList = new ArrayList<>();
        goalReached = false;
        maxCol = PathfindingMap.size();
        maxRow = PathfindingMap.getFirst().size();
        usedNodes = new ArrayList<>();

    }

    /**
     * Initializes the pathfinding grid and sets up default start and goal nodes.
     */
    public void setPathMap() {
        node = new Node[maxCol][maxRow];
        for (int col = 0; col < maxCol; col++) {
            for (int row = 0; row < maxRow; row++) {
                node[col][row] = new Node(col, row);
            }
        }

        int coordBX = (int)(maxCol/2f);
        int coordBY = (int)((maxRow-2)/2f)+1;


        setStartNode(0,0);
        setGoalNode(coordBX, coordBY);
        //SET COST
        setCostOnNodes();

    }


    /**
     * Updates the pathfinding map based on the positions of the source and target entities, and the walls.
     *
     * @param entitySource The source entity.
     * @param entityTarget The target entity.
     * @param walls        A list of rectangles representing walls in the map.
     */
    public void updatePathMap(Entity entitySource, Entity entityTarget, List<Rectangle> walls) {

        goalReached = false;
        step = 0;
        resetUsedNodes();
        int xTempDest = (int)entityTarget.getCenterX()/16;
        int yTempDest = (int)entityTarget.getCenterY()/16;

        int xTempSrc = Math.round(entitySource.getCenterX()/16);
        int yTempSrc = Math.round(entitySource.getCenterY()/16);

        for (Rectangle wall : walls) {
            float width = wall.getWidth();
            float height = wall.getHeight();
            float x = wall.getX();
            float y = wall.getY();
            for (int i = 0; i < Math.ceil(width/16); i++)
            {
                for (int j = 1; j <= Math.ceil(height/16); j++) { //j commence à 1 pour gestion pratique des collisions sur le haut et bas des murs
                    int tempx = Math.round(x/16) + i;
                    int tempy = Math.round(y/16) + j;

                    int tempNodex = -xTempDest+tempx+(int)(maxCol/2f);
                    int tempNodey = -yTempDest + tempy+(int)((maxRow-2)/2f)+1;
                    try  {
                        setSolidNode(tempNodex,tempNodey);
                        usedNodes.add(node[tempNodex][tempNodey]);
                    }
                    catch (Exception e) {
                    }
                }
            }
        }

        int coordAX = -xTempDest + xTempSrc+(int)(maxCol/2f);
        int coordAY = -yTempDest + yTempSrc +(int)((maxRow-2)/2f)+1;

        int coordBX = (int)(maxCol/2f);
        int coordBY = (int)((maxRow-2)/2f)+1;

        setStartNode(coordAX,coordAY);
        if (!node[coordBX][coordBY].isSolid()) {
            setGoalNode(coordBX, coordBY);
        }
        else {
            setGoalNode(coordAX,coordAY);
        }

        currentNode.setAsChecked();
        checkedList.add(currentNode);
        usedNodes.add(currentNode);
        //System.out.println(startNode.getX()+ " " + startNode.getY());

        setCostOnNodes();

    }

    /**
     * Resets the used nodes for a new pathfinding calculation.
     *
     * This method clears all nodes that were marked as used during the previous pathfinding session,
     * including their states such as `goal`, `checked`, `path`, and `open`. It also clears the open
     * and closed lists.
     */
    private void resetUsedNodes() {

        for (Node node : usedNodes) {
            node.setGoal(false);
            node.setChecked(false);
            node.setPath(false);
            node.setOpen(false);
            node.setStart(false);
            node.setSolid(false);
        }
        for (Node node : closed_list) {
            node.setPath(false);
        }
        closed_list.clear();
        usedNodes.clear();
        /*System.out.println("Reset Used Node");
        System.out.println("openList size :" + openList.size());*/
        openList.clear();
    }

    /**
     * Sets a specific node as the start node.
     *
     * @param col The column index of the start node.
     * @param row The row index of the start node.
     */
    private void setStartNode(int col, int row) {
        node[col][row].setAsStart();
        startNode = node[col][row];
        currentNode = startNode;
        openList.add(currentNode);
    }


    /**
     * Sets a specific node as the goal node.
     *
     * @param col The column index of the goal node.
     * @param row The row index of the goal node.
     */
    private void setGoalNode(int col, int row) {
        node[col][row].setAsGoal();
        goalNode = node[col][row];
    }


    /**
     * Marks a specific node as solid (non-traversable).
     *
     * @param col The column index of the solid node.
     * @param row The row index of the solid node.
     */
    private void setSolidNode(int col, int row) {
        node[col][row].setAsSolid();
    }

    /**
     * Gets the grid of nodes.
     *
     * @return A 2D array of `Node` objects representing the grid.
     */
    public Node[][] getNode() {
        return node;
    }

    /**
     * Calculates the costs (gCost, hCost, and fCost) for all nodes in the grid.
     *
     * This method calculates the cost of moving to each node from the start node
     * and the heuristic cost to the goal node, then sums them to get the total cost.
     */
    private void setCostOnNodes() {
        //System.out.println("------------------------------------");

        for (int col = 0; col < maxCol; col++) {
            for (int row = 0; row < maxRow; row++) {
                getCost(node[col][row]);
            }
        }
    }


    /**
     * Calculates the costs (gCost, hCost, fCost) for a specific node.
     *
     * @param node The `Node` object to calculate costs for.
     */
    private void getCost(Node node) {
        // GET G COST (Distance from start node)
        //System.out.println(node.getX() + " " + node.getY());
        int xDistance = node.getX() - startNode.getX();
        int yDistance = node.getY() - startNode.getY();
        node.gCost = Math.sqrt(xDistance*xDistance + yDistance*yDistance);


        // GET H COST (Distance from the goal node)

        xDistance = node.getX() - goalNode.getX();
        yDistance = node.getY() - goalNode.getY();
        node.hCost = Math.sqrt(xDistance*xDistance + yDistance*yDistance);


        node.fCost = node.gCost + node.hCost;
        //System.out.println(node.fCost);
    }


    /**
     * Displays the current pathfinding map in the console.
     *
     * This method prints the pathfinding map to the console for debugging purposes.
     */
    public void displayPathMap() {
        String m = "";
        for(int i = 0; i <maxCol; i++) {
            for(int j = 0; j < maxRow; j++) {
                m+="[" + myPathMap.get(i).get(j)+"]";
            }
            m+="\n";
        }
        System.out.println(m);
    }

    /**
     * Performs the A* pathfinding algorithm to find the shortest path.
     *
     * @param entitySource The source entity (starting point).
     * @param entityTarget The target entity (ending point).
     * @param walls        A list of rectangles representing obstacles.
     *
     * This method executes the A* algorithm to determine the shortest path
     * between `entitySource` and `entityTarget`. It uses an open list to track
     * nodes to be explored and a closed list for nodes that have already been processed.
     * The path is determined when the goal node is reached.
     */
    public void search(Entity entitySource, Entity entityTarget, List<Rectangle> walls) {
        updatePathMap(entitySource, entityTarget, walls);
        /*System.out.println("=====================");
        System.out.println("Searching for the path");
        System.out.println("openList size :" + openList.size());*/

        while (!goalReached && step < 5000) {

            int col = currentNode.getX();
            int row = currentNode.getY();


            currentNode.setAsChecked();
            checkedList.add(currentNode);

            //System.out.println();
            //openList.remove(currentNode);



            // OPEN 8 NEIGHBOURS NODES OF CURRENT NODE
            //System.out.println("Current node :  (" + currentNode.getX() + "," + currentNode.getY()+")");
            for (int i = -1; i<2;i++) {
                for (int j = -1; j<2;j++) {
                    if (j!=0 || i!=0){
                        if (0<= col+i && col+i< maxCol && 0<= row+j && row+j< maxRow){
                            //System.out.println("("+(col+i)+","+(row+j)+")");
                            openNode(node[col+i][row+j]);
                        }
                    }
                }
            }


            /*System.out.println("Intermdiate");
            System.out.println("openList size :" + openList.size());*/
            int bestNodeIndex = 0;
            double bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++) {

                //if F cost is better
                if (openList.get(i).getFCost() < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).getFCost();
                }

                //if F cost is equal
                else if (openList.get(i).getFCost() == bestNodefCost) {
                    if (openList.get(i).getGCost() < openList.get(bestNodeIndex).getGCost()) {
                        bestNodeIndex = i;
                    }
                }
            }


            // Get the best node for next step
            /*System.out.println("Search cycle " + step);
            System.out.println("openList size :" + openList.size());*/
            currentNode = openList.get(bestNodeIndex);
            openList.remove(currentNode);
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
                entitySource.setPathNodes(closed_list);
            }
            step++;
        }
    }


    /**
     * Opens a specific node for exploration.
     *
     * @param node The node to open.
     *
     * Checks if the node is not already open, checked, or solid,
     * then adds it to the open list and marks its parent node.
     */
    private void openNode(Node node) {
        /*System.out.println("Node state");
        System.out.println("\tisOpen -> "+node.isOpen());
        System.out.println("\tisSolid -> "+node.isSolid());
        System.out.println("\tisChecked -> "+node.isChecked());*/
        if (!node.isOpen() && !node.isChecked() && !node.isSolid()) {
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
            usedNodes.add(node);
        }
    }


    /**
     * Traces the path from the goal node back to the start node.
     *
     * This method follows the parent nodes from the goal node
     * back to the start node, marking nodes as part of the final path.
     */
    private void trackThePath() {
        Node current = goalNode;
        closed_list.add(goalNode);
        while (current != startNode) {
            current = current.getParent();
            if(current != startNode) {
                current.setAsPath();
                closed_list.add(current);
            }
        }

        //closed_list.add(startNode);

    }


    /**
     * Returns the maximum number of columns in the pathfinding grid.
     *
     * @return The number of columns.
     */
    public int getMaxCol() {
        return maxCol;
    }

    /**
     * Returns the maximum number of rows in the pathfinding grid.
     *
     * @return The number of rows.
     */
    public int getMaxRow() {
        return maxRow;
    }


    /**
     * Returns the pathfinding map as a 2D list.
     *
     * @return A 2D list representing the pathfinding map.
     */
    public List<List<Integer>> getPathMap() {
        return myPathMap;
    }

    /**
     * Returns the list of closed nodes.
     *
     * @return A list of explored (closed) nodes.
     */
    public List<Node> getClosedList() {
        return closed_list;
    }
}
