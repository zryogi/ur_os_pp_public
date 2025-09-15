package ur_os;

public class Node<T extends Comparable<T>> {

    T data;
    RBTColor color;

    Node<T> left;
    Node<T> right;
    Node<T> parent;

    public Node(T data, RBTColor color) {
        this.data = data;
        this.color = color;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public RBTColor getColor() {
        return color;
    }

    public void setColor(RBTColor color) {
        this.color = color;
    }

    public Node<T> getLeft() {
        return left;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }

    public Node<T> getParent() {
        return parent;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }
}
