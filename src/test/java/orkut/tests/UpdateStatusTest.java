package orkut.tests;

import com.epam.healenium.SelfHealingDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateStatusTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--incognito");

        WebDriver delegate = new ChromeDriver(options);
        driver = SelfHealingDriver.create(delegate);

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    public void updateStatus() {
        driver.get("http://localhost:3000");

        // Login
        WebElement inputUsuario = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[placeholder='Usuário']"))
        );
        WebElement botaoLogin = driver.findElement(By.cssSelector("button[type='submit']"));
        inputUsuario.click();
        inputUsuario.clear();
        inputUsuario.sendKeys("Jessica-Lira");
        botaoLogin.click();

        boolean loginSucesso = wait.until(ExpectedConditions.urlToBe("http://localhost:3000/"));
        assertTrue(loginSucesso, "A página inicial pós-login não foi exibida (falha no redirecionamento).");

        // --- ALTERAR O STATUS USANDO By.name ---
        WebElement selectElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.name("statusSelect"))
        );

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "arguments[0].value = 'Ausente';" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                selectElement
        );

        // --- NAVEGAR PARA OUTRA PÁGINA (ex: Fotos) ---
        WebElement linkFotos = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@href, '/fotos')]"))
        );
        js.executeScript("arguments[0].click();", linkFotos);

        // --- VERIFICAR QUE O STATUS CONTINUA ALTERADO NA NOVA PÁGINA USANDO By.name ---
        WebElement selectElementNaNovaPagina = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.name("statusSelect"))
        );

        Select dropdownVerificacao = new Select(selectElementNaNovaPagina);
        String statusAtual = dropdownVerificacao.getFirstSelectedOption().getAttribute("value");

        assertEquals("Ausente", statusAtual, "O status não permaneceu 'Ausente' após navegar de página!");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}