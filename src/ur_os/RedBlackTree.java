package ur_os;

public class RedBlackTree<T extends Comparable<T>> {
    Node<T> NIL;
    Node<T> root;

    public RedBlackTree() {
        NIL = new Node<>(null, RBTColor.Black);
        root = NIL;
    }

    public void rotateLeft(Node<T> node) {
        Node<T> y = node.getRight();
        node.setRight(y.getLeft());

        if(y.getLeft() != this.NIL) {
            y.getLeft().setParent(node);
        }

        y.setParent(node.getParent());
        if (node.getParent() == null) {
            root = y;
        } else if(node == node.getParent().getLeft()) {
            node.getParent().setLeft(y);
        } else {
            node.getParent().setRight(y);
        }

        y.setLeft(node);
        node.setParent(y);
    }

    public void rotateRight(Node<T> node) {
        Node<T> y = node.getLeft();
        node.setLeft(y.getRight());

        if (y.getRight() != this.NIL) {
            y.getRight().setParent(node);
        }

        y.setParent(node.getParent());
        if (node.getParent() == null) {
            root = y;
        } else if (node == node.getParent().getRight()) {
            node.getParent().setRight(y);
        } else {
            node.getParent().setLeft(y);
        }

        y.setRight(node);
        node.setParent(y);
    }

    public void insert(T data) {
        Node<T> newNode = new Node<>(data, RBTColor.Red);
        newNode.setLeft(NIL);
        newNode.setRight(NIL);
        Node<T> y = null;
        Node<T> x = root;

        while (x != NIL) {
            y = x;
            if (data.compareTo(x.getData()) < 0) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }
        newNode.setParent(y);
        if (y == null) {
            root = newNode;
        } else if (data.compareTo(y.getData()) < 0) {
            y.setLeft(newNode);
        } else {
            y.setRight(newNode);
        }
        // Fix up the tree to maintain red-black properties
        insertFixup(newNode);
    }

    private void insertFixup(Node<T> z) {
        while (z.getParent() != null && z.getParent().getColor() == RBTColor.Red) {
            if (z.getParent() == z.getParent().getParent().getLeft()) {
                Node<T> y = z.getParent().getParent().getRight(); // uncle
                if (y != null && y.getColor() == RBTColor.Red) {
                    // Case 1: Uncle is red
                    z.getParent().setColor(RBTColor.Black);
                    y.setColor(RBTColor.Black);
                    z.getParent().getParent().setColor(RBTColor.Red);
                    z = z.getParent().getParent();
                } else {
                    if (z == z.getParent().getRight()) {
                        // Case 2: z is right child
                        z = z.getParent();
                        rotateLeft(z);
                    }
                    // Case 3: z is left child
                    z.getParent().setColor(RBTColor.Black);
                    z.getParent().getParent().setColor(RBTColor.Red);
                    rotateRight(z.getParent().getParent());
                }
            } else {
                Node<T> y = z.getParent().getParent().getLeft(); // uncle
                if (y != null && y.getColor() == RBTColor.Red) {
                    // Case 1: Uncle is red
                    z.getParent().setColor(RBTColor.Black);
                    y.setColor(RBTColor.Black);
                    z.getParent().getParent().setColor(RBTColor.Red);
                    z = z.getParent().getParent();
                } else {
                    if (z == z.getParent().getLeft()) {
                        // Case 2: z is left child
                        z = z.getParent();
                        rotateRight(z);
                    }
                    // Case 3: z is right child
                    z.getParent().setColor(RBTColor.Black);
                    z.getParent().getParent().setColor(RBTColor.Red);
                    rotateLeft(z.getParent().getParent());
                }
            }
        }
        root.setColor(RBTColor.Black);
    }

    public Node<T> minimum(Node<T> node) {
        while (node != NIL && node.getLeft() != NIL) {
            node = node.getLeft();
        }
        return node;
    }

    public void transplant(Node<T> u, Node<T> v) {
        if (u.getParent() == null) {
            root = v;
        } else if (u == u.getParent().getLeft()) {
            u.getParent().setLeft(v);
        } else {
            u.getParent().setRight(v);
        }
        // Always set parent, even for NIL to maintain consistent tree structure
        v.setParent(u.getParent());
    }

    public void delete(T data) {
        Node<T> z = search(root, data);
        if (z == NIL) return; // Node not found

        Node<T> y = z;
        RBTColor yOriginalColor = y.getColor();
        Node<T> x;

        if (z.getLeft() == NIL) {
            x = z.getRight();
            transplant(z, z.getRight());
        } else if (z.getRight() == NIL) {
            x = z.getLeft();
            transplant(z, z.getLeft());
        } else {
            y = minimum(z.getRight());
            yOriginalColor = y.getColor();
            x = y.getRight();
            if (y.getParent() == z) {
                x.setParent(y);
            } else {
                transplant(y, y.getRight());
                y.setRight(z.getRight());
                y.getRight().setParent(y);
            }
            transplant(z, y);
            y.setLeft(z.getLeft());
            y.getLeft().setParent(y);
            y.setColor(z.getColor());
        }
        if (yOriginalColor == RBTColor.Black) {
            deleteFixup(x);
        }
    }

    private void deleteFixup(Node<T> x) {
        while (x != root && x != NIL && x.getParent() != null && x.getColor() == RBTColor.Black) {
            if (x == x.getParent().getLeft()) {
                Node<T> w = x.getParent().getRight();
                if (w != NIL && w.getColor() == RBTColor.Red) {
                    w.setColor(RBTColor.Black);
                    x.getParent().setColor(RBTColor.Red);
                    rotateLeft(x.getParent());
                    w = x.getParent().getRight();
                }
                if (w != NIL && w.getLeft() != NIL && w.getRight() != NIL && 
                    w.getLeft().getColor() == RBTColor.Black && w.getRight().getColor() == RBTColor.Black) {
                    w.setColor(RBTColor.Red);
                    x = x.getParent();
                } else {
                    if (w != NIL && w.getRight() != NIL && w.getRight().getColor() == RBTColor.Black) {
                        if (w.getLeft() != NIL) {
                            w.getLeft().setColor(RBTColor.Black);
                        }
                        w.setColor(RBTColor.Red);
                        rotateRight(w);
                        w = x.getParent().getRight();
                    }
                    if (w != NIL) {
                        w.setColor(x.getParent().getColor());
                    }
                    x.getParent().setColor(RBTColor.Black);
                    if (w != NIL && w.getRight() != NIL) {
                        w.getRight().setColor(RBTColor.Black);
                    }
                    rotateLeft(x.getParent());
                    x = root;
                }
            } else {
                Node<T> w = x.getParent().getLeft();
                if (w != NIL && w.getColor() == RBTColor.Red) {
                    w.setColor(RBTColor.Black);
                    x.getParent().setColor(RBTColor.Red);
                    rotateRight(x.getParent());
                    w = x.getParent().getLeft();
                }
                if (w != NIL && w.getRight() != NIL && w.getLeft() != NIL &&
                    w.getRight().getColor() == RBTColor.Black && w.getLeft().getColor() == RBTColor.Black) {
                    w.setColor(RBTColor.Red);
                    x = x.getParent();
                } else {
                    if (w != NIL && w.getLeft() != NIL && w.getLeft().getColor() == RBTColor.Black) {
                        if (w.getRight() != NIL) {
                            w.getRight().setColor(RBTColor.Black);
                        }
                        w.setColor(RBTColor.Red);
                        rotateLeft(w);
                        w = x.getParent().getLeft();
                    }
                    if (w != NIL) {
                        w.setColor(x.getParent().getColor());
                    }
                    x.getParent().setColor(RBTColor.Black);
                    if (w != NIL && w.getLeft() != NIL) {
                        w.getLeft().setColor(RBTColor.Black);
                    }
                    rotateRight(x.getParent());
                    x = root;
                }
            }
        }
        if (x != NIL) {
            x.setColor(RBTColor.Black);
        }
    }

    public Node<T> search(Node<T> node, T data) {
        while (node != NIL && data.compareTo(node.getData()) != 0) {
            if (data.compareTo(node.getData()) < 0) {
                node = node.getLeft();
            } else {
                node = node.getRight();
            }
        }
        return node;
    }
}
