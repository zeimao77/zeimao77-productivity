package top.zeimao77.product.cmd;

import top.zeimao77.product.util.StringOptional;

import java.util.ArrayList;

public class RootMenu implements MenuStarter {

    ArrayList<MenuStarter> menuList = new ArrayList<>();
    private String banner = """
                       /\\_/\\\n
                 _____/ o o \\\n
               /~_____  =-= /\n
              (__zm77__)_m_m)\n             
            """;

    public RootMenu() {}

    public RootMenu(String banner) {
        this.banner = banner;
    }

    public boolean addMenu(MenuStarter menuStarter) {
        return this.menuList.add(menuStarter);
    }

    @Override
    public String name() {
        return "ROOT";
    }

    public void start(String[] args,Integer selectMenu) {
        if(selectMenu != null && selectMenu > 0 && selectMenu <= menuList.size())
            menuList.get(selectMenu-1).start(args);
        else
            doStart(args);
    }

    @Override
    public void doStart(String[] args) {
        boolean startFlag = true;
        while(startFlag) {
            System.out.println(this.banner);
            for (int i = 0; i < menuList.size(); i++)
                System.out.printf("%d. %s\n",i+1,menuList.get(i).name());
            StringOptional p = CommandParamUtil.getParamFromSystemIn("请选择[退出输入:Q]:");
            if(!p.isBlack() && "Q".equals(p.get()))
                break;
            try {
                if(!p.isBlack()) {
                    int selectMenu = p.getInteger();
                    if(selectMenu > 0 && selectMenu <= menuList.size()) {
                        menuList.get(selectMenu-1).start(args);
                    }
                }
            }catch (NumberFormatException e) {}
        }
    }

}
