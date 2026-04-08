package viewPackage;

public class BannerThread extends Thread {

    private final BannerPanel bannerPanel;

    public BannerThread(BannerPanel bannerPanel) {
        this.bannerPanel = bannerPanel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(250);
                bannerPanel.moveText();
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}