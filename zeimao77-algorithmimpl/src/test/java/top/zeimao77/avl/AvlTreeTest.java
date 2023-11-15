package top.zeimao77.avl;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class AvlTreeTest {

    @Test
    void isEmpty() {
        AvlTree<Long> avlTree = new AvlTree<Long>();
        Comparator<Long> COMP = Long::compare;
        avlTree.add(3L,COMP);
        avlTree.add(4L,COMP);
        avlTree.add(5L,COMP);
        avlTree.show();


    }
}