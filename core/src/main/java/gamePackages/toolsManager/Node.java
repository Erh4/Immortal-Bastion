package gamePackages.toolsManager;


/**
 * Represents a node in a grid-based pathfinding system.
 *
 * The `Node` class is used to store information about individual nodes in a grid,
 * including their position, costs, and state in the pathfinding process.
 */
public class Node {
    protected Node parent;
    protected int X;
    protected int Y;
    protected double gCost;
    protected double hCost;
    protected double fCost;
    protected boolean start;
    protected boolean goal;
    protected boolean solid;
    protected boolean open;
    protected boolean checked;
    protected boolean path;

    /**
     * Constructs a `Node` with specified grid coordinates.
     *
     * @param X The X-coordinate of the node in the grid.
     * @param Y The Y-coordinate of the node in the grid.
     */
    public Node(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    /**
     * Marks this node as the start node.
     */
    public void setAsStart() {
        start = true;
    }


    /**
     * Sets whether this node is the start node.
     *
     * @param start `true` if this node is the start node, `false` otherwise.
     */
    public void setStart(boolean start) {
        this.start = start;
    }


    /**
     * Marks this node as the goal node.
     */
    public void setAsGoal() {
        goal = true;
    }


    /**
     * Sets whether this node is the goal node.
     *
     * @param goal `true` if this node is the goal node, `false` otherwise.
     */
    public void setGoal(boolean goal) {
        this.goal = goal;
    }


    /**
     * Marks this node as solid (not traversable).
     */
    public void setAsSolid() {
        solid = true;
    }


    /**
     * Sets whether this node is solid (not traversable).
     *
     * @param solid `true` if this node is solid, `false` otherwise.
     */
    public void setSolid(boolean solid) {
        this.solid = solid;
    }


    /**
     * Marks this node as open (in the open list).
     */
    public void setAsOpen() {
        open = true;
    }


    /**
     * Sets whether this node is open (in the open list).
     *
     * @param open `true` if this node is open, `false` otherwise.
     */
    public void setOpen(boolean open) {
        this.open = open;
    }


    /**
     * Marks this node as checked (already processed).
     */
    public void setAsChecked() {
        checked = true;
    }


    /**
     * Sets whether this node has been checked.
     *
     * @param checked `true` if this node has been checked, `false` otherwise.
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * Marks this node as part of the final path.
     */
    public void setAsPath() {
        path = true;
    }

    /**
     * Sets whether this node is part of the final path.
     *
     * @param path `true` if this node is part of the final path, `false` otherwise.
     */
    public void setPath(boolean path) {
        this.path = path;
    }


    /**
     * Gets the parent node of this node.
     *
     * @return The parent node.
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Gets the X-coordinate of this node in the grid.
     *
     * @return The X-coordinate.
     */
    public int getX() {
        return X;
    }

    /**
     * Gets the Y-coordinate of this node in the grid.
     *
     * @return The Y-coordinate.
     */
    public int getY() {
        return Y;
    }

    /**
     * Gets the cost from the start node to this node.
     *
     * @return The gCost value.
     */
    public double getGCost() {
        return gCost;
    }

    /**
     * Gets the heuristic cost from this node to the goal node.
     *
     * @return The hCost value.
     */
    public double getHCost() {
        return hCost;
    }

    /**
     * Gets the total cost of this node (gCost + hCost).
     *
     * @return The fCost value.
     */
    public double getFCost() {
        return fCost;
    }

    /**
     * Checks if this node is the start node.
     *
     * @return `true` if this node is the start node, `false` otherwise.
     */
    public boolean isStart() {
        return start;
    }

    /**
     * Checks if this node is the goal node.
     *
     * @return `true` if this node is the goal node, `false` otherwise.
     */
    public boolean isGoal() {
        return goal;
    }

    /**
     * Checks if this node is solid (not traversable).
     *
     * @return `true` if this node is solid, `false` otherwise.
     */
    public boolean isSolid() {
        return solid;
    }

    /**
     * Checks if this node is in the open list.
     *
     * @return `true` if this node is open, `false` otherwise.
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Checks if this node has been checked (processed).
     *
     * @return `true` if this node has been checked, `false` otherwise.
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * Checks if this node is part of the final path.
     *
     * @return `true` if this node is part of the path, `false` otherwise.
     */
    public boolean isPath() {
        return path;
    }
}
