package top.zeimao77.product.cmd;

import java.util.ArrayList;
import java.util.Scanner;

public class RootMenu implements MenuStarter {

    ArrayList<MenuStarter> menuList = new ArrayList<>();

    public RootMenu() {}

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
        try(Scanner scanner = new Scanner(System.in)) {
            while(startFlag) {
                System.out.println("           /\\_/\\");
                System.out.println("     _____/ o o \\");
                System.out.println("   /~_____  =-= /");
                System.out.println("  (__zm77__)_m_m)");
                for (int i = 0; i < menuList.size(); i++)
                    System.out.printf("%d. %s\n",i+1,menuList.get(i).name());
                System.out.println("请选择[退出输入:Q]:");
                String s = scanner.nextLine();
                if("Q".equals(s))
                    break;
                try {
                    int selectMenu = Integer.parseInt(s);
                    if(selectMenu > 0 && selectMenu <= menuList.size()) {
                        menuList.get(selectMenu-1).start(args);
                    }
                }catch (NumberFormatException e) {}
            }
        }
    }

}
