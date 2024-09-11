package top.zeimao77.product.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zeimao77.product.exception.BaseServiceRunException;
import top.zeimao77.product.util.StringOptional;

import java.util.ArrayList;

public class RootMenu implements MenuStarter {

    private static Logger logger = LoggerFactory.getLogger(RootMenu.class);

    ArrayList<MenuStarter> menuList = new ArrayList<>();
    private String banner = """
                       /\\_/\\
                 _____/ o o \\
               /~_____  =-= /
              (__zm77__)_m_m)         
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
            if(!p.isBlank() && "Q".equals(p.get()))
                break;
            try {
                if(!p.isBlank()) {
                    int selectMenu = p.getInteger();
                    if(selectMenu > 0 && selectMenu <= menuList.size()) {
                        menuList.get(selectMenu-1).start(args);
                    }
                }
            }catch (NumberFormatException e) {

            }catch (BaseServiceRunException e) {
                logger.error("错误:",e);
            }
        }
    }

}
