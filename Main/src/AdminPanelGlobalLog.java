public class AdminPanelGlobalLog {
    private static LogPanel logPanel = new LogPanel();

    public static LogPanel getLogPanel() {
        return logPanel;
    }

    public static void setLogPanel(LogPanel lp) {
        logPanel = lp;
    }
}
