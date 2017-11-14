/*!!!!!!!DISCLAIMER!!!!!!!
    I did not write this code
    This code was taken from  https://stackoverflow.com/questions/19330731/tree-implementation-in-java-root-parents-and-children
    I just modified the code into something I can work with.
 */

import java.util.ArrayList;
import java.util.List;

public class Node1<T> {
    private List<Node1<T>> children = new ArrayList<Node1<T>>();
    private Node1<T> parent = null;
    private T data = null;
    private int h = 0;
    private boolean continueto = true;
    private int id = 0;

    public Node1(T data, int h) {
        this.data = data;
        this.h = h;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Node1<T> getParent() {
        return parent;
    }

    public Node1(T data, int h, Node1<T> parent) {
        this.data = data;
        this.parent = parent;
        this.h = h;
    }

    public List<Node1<T>> getChildren() {
        return children;
    }

    public void setParent(Node1<T> parent) {
        //parent.addChild(this);
        this.parent = parent;
    }

    public void addChild(T data, int h) {
        Node1<T> child = new Node1<T>(data,h);
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(Node1<T> child) {
        child.setParent(this);
        this.children.add(child);
    }
    public void removeChild(Node1<T> child){
        this.children.remove(child);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        if(this.children.size() == 0)
            return true;
        else
            return false;
    }

    public void removeParent() {
        this.parent = null;
    }

    public boolean isContinueto() {
        return continueto;
    }

    public void setContinueto(boolean continueto) {
        this.continueto = continueto;
    }
}