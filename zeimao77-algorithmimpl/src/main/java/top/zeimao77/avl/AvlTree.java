package top.zeimao77.avl;

import java.util.Comparator;

public class AvlTree<T> {

    private AvlTree<T> lnode;
    private AvlTree<T> rnode;
    private int height;
    private T data;

    public AvlTree() {
    }


    public void add(T t,Comparator<T> comparator) {
        if(data == null) {
            data = t;
        } else if(comparator.compare(data,t) > 0) {
            if(lnode == null)
                lnode = new AvlTree<T>();
            lnode.add(t,comparator);
        }else if(comparator.compare(data,t) < 0) {
            if(rnode == null)
                rnode = new AvlTree<T>();
            rnode.add(t,comparator);
        } else if(comparator.compare(data,t) == 0) {
            throw new IllegalStateException("数据存在");
        }
    }

    public void show() {
        if(lnode != null) {
            lnode.show();
        }
        System.out.print(this.data.toString() + ",");
        if(rnode != null) {
            rnode.show();
        }
        System.out.println();
    }

    public boolean isEmpty() {
        return data == null;
    }

    public AvlTree<T> getLnode() {
        return lnode;
    }

    public void setLnode(AvlTree<T> lnode) {
        this.lnode = lnode;
    }

    public AvlTree<T> getRnode() {
        return rnode;
    }

    public void setRnode(AvlTree<T> rnode) {
        this.rnode = rnode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
