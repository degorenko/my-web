package client_app;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 4/26/14
 * Time: 7:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String args[]) {
        try {
            /*String logs = "/disk/work/logs/logs-sahara/opt/ci/logs";
            String cond = "/disk/work/projects/project56/src/client_app/JSON/conditions";
            String info = "/disk/work/projects/project56/src/client_app/JSON/info";
            Core core = new Core(logs, cond, info);*/
            if (args.length == 4) {
                Core core = new Core(args[0], args[1],args[2], true);
                core.run();
            }   else {
                Core core = new Core(args[0], args[1],args[2], false);
                core.run();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
