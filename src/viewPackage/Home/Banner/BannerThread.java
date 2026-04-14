package viewPackage.Home.Banner;

import javax.swing.SwingUtilities;

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
                SwingUtilities.invokeLater(bannerPanel::moveText);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}