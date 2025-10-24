package ir.netpick.scrape.scrapper;

import java.util.List;

import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import ir.netpick.scrape.models.ScrapeJob;
import ir.netpick.scrape.properties.WebProperties;
import ir.netpick.scrape.repositories.ScrapeJobRepository;
import ir.netpick.scrape.services.ScrapeService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Scrape {

    WebProperties webProperties = new WebProperties();

    private String driverLocation = "F:\\chromedriver-win64\\chromedriver.exe";
    private String chromeLocation = "F:\\chrome-win64\\chrome-win64\\chrome.exe";

    private final ScrapeJobRepository scrapeJobRepository;
    private final ScrapeService scrapeService;

    public int getDataCount() {
        return scrapeService.allDatas().size();
    }

    public void webGet() {
        List<ScrapeJob> scrapeJobs = scrapeJobRepository.findByAttemptLessThanEqual(3);

        if (scrapeJobs.size() <= 0) {
            return;
        }

        WebDriver driver = instantiateDriver(false);

        try {
            for (ScrapeJob scrapeJob : scrapeJobs) {
                // Navigate to the desired website
                driver.get(scrapeJob.getLink());

                String pageSource = driver.getPageSource();
                scrapeService.createScrapeData(pageSource, scrapeJob.getId());

                scrapeJob.setAttempt(scrapeJob.getAttempt() + 1);
                scrapeService.updateScrapeJob(scrapeJob, scrapeJob.getId());

            }

            // Wait for a few seconds (for demonstration purposes only)
            Thread.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }

    private ChromeDriver instantiateDriver(boolean headless) {
        ChromeOptions co = new ChromeOptions();
        co.setBinary(chromeLocation);

        System.setProperty("webdriver.chrome.driver", driverLocation);

        co.addArguments("--disable-gpu", "--window-size=1920,1080", "--ignore-certificate-errors",
                "--disable-extensions", "--no-sandbox", "--disable-dev-shm-usage");

        if (headless) {
            co.addArguments("--headless",
                    "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36 Edg/134.0.0.0",
                    "--window-size=1920,1080");
        }

        // HashMap<String, Object> prefs = new HashMap<>();
        // prefs.put("download.prompt_for_download", false);
        // co.setExperimentalOption("prefs", prefs);

        ChromeDriver driver = null;
        try {
            driver = new ChromeDriver(co);
        } catch (SessionNotCreatedException e) {
            // log.error("Driver encountered an issue during initialization...");
            // log.error(LogEnum.FAILED.formatLog("instantiateDriver", e.getMessage()));
            e.printStackTrace();
        }
        return driver;
    }
}
