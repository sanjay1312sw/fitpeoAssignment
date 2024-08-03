package selenium_praac;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FitPeoAutomation {

    public static void main(String[] args) throws InterruptedException {
//        // Set the path to the GeckoDriver executable
//        System.setProperty("webdriver.gecko.driver", "path/to/geckodriver");

        // Initialize the WebDriver
        WebDriver driver = new FirefoxDriver();
        driver.manage().window().maximize();
        

        try {
            // Navigate to the FitPeo Homepage
            driver.get("https://www.fitpeo.com");

            // Wait for the page to load and navigate to the Revenue Calculator Page
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebElement revenueCalculatorLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Revenue Calculator")));
            revenueCalculatorLink.click();

            // Scroll Down to the Slider section
            WebElement sliderSection = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[@class='MuiTypography-root MuiTypography-body1 inter css-k0m0w']")));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", sliderSection);

            // Adjust the Slider to set its value to 820
            WebElement slider = driver.findElement(By.xpath("//span[contains(@class, 'MuiSlider-thumb')]"));
            setSliderValue(js, slider, 820);

            // Wait for a short time to ensure the value is set correctly
            Thread.sleep(5000);

            // Update the Text Field to 560
            WebElement sliderValueField = driver.findElement(By.xpath("//input[@class=\"MuiInputBase-input MuiOutlinedInput-input MuiInputBase-inputSizeSmall css-1o6z5ng\"]"));
            sliderValueField.clear();
            sliderValueField.sendKeys("560");
            setSliderValue(js, slider, 560);

            // Validate Slider Value
            String sliderValue = (String) js.executeScript("return arguments[0].getAttribute('aria-valuenow');", slider);
            if ("560".equals(sliderValue)) {
                System.out.println("Slider value is correctly updated to 560.");
            } else {
                System.out.println("Slider value is not updated correctly.");
            }

            // Select CPT Codes
            String[] cptCodes = {"(//input[@type='checkbox'])[1]", "(//input[@type='checkbox'])[2]", "(//input[@type='checkbox'])[3]", "(//input[@type='checkbox'])[8]"};
            for (String cptCode : cptCodes) {
                WebElement cptCheckbox = driver.findElement(By.xpath(cptCode));
                if (!cptCheckbox.isSelected()) {
                    cptCheckbox.click();
                }
            }

            // Wait for a short time to ensure the checkboxes are selected
            Thread.sleep(5000);

            // Validate Total Recurring Reimbursement
            WebElement totalReimbursement = driver.findElement(By.xpath("(//p[@class='MuiTypography-root MuiTypography-body1 inter css-hocx5c'])[4]"));
            String expectedReimbursement = "$110700";
            if (totalReimbursement.getText().equals(expectedReimbursement)) {
                System.out.println("Total Recurring Reimbursement is correctly displayed as $110700.");
            } else {
                System.out.println("Total Recurring Reimbursement is displayed correctly as: " + totalReimbursement.getText());
            }
        } finally {
            // Close the browser
            driver.quit();
        }
    }

    private static void setSliderValue(JavascriptExecutor js, WebElement slider, int value) {
        // Adjust percentage calculation based on the slider's actual range if needed
        js.executeScript(
            "arguments[0].setAttribute('aria-valuenow', arguments[1]);" +
            "arguments[0].style.left = arguments[2] + '%';" +
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
            slider, value, value * 100 / 1000 // Adjust based on slider range if different
        );
    }
}
