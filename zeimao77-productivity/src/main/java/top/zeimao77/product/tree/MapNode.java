package top.zeimao77.product.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map类型树节点
 */
public class MapNode implements TreeNode<MapNode> {

    protected Map node;
    protected List<MapNode> childs = new ArrayList<>();

    public MapNode(Map<String,Object> node) {
        this.node = node;
    }

    @Override
    public boolean addChild(MapNode t) {
        if(t instanceof MapNode) {
            return childs.add(t);
        }
        return false;
    }

    public static MapNode build(TreeLine line) {
        MapNode mapNode = new MapNode(new HashMap<>());
        mapNode.node.put("parentId",line.__getParentId());
        mapNode.node.put("nodeId",line.__getNodeId());
        return mapNode;
    }

    @Override
    public List<MapNode> childs() {
        return childs;
    }

    @Override
    public String __getParentId() {
        return null;
    }

    @Override
    public String __getNodeId() {
        return null;
    }

    @Override
    public MapNode __parent() {
        return null;
    }
}
