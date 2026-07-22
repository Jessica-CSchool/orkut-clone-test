package orkut.tests;

import com.epam.healenium.SelfHealingDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogoutTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--incognito");

        WebDriver delegate = new ChromeDriver(options);
        driver = SelfHealingDriver.create(delegate);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void deveRealizarLogout() {
        driver.get("http://localhost:3000");

        // Realizar login primeiro
        WebElement inputUsuario = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[placeholder='Usuário']"))
        );
        WebElement botaoLogin = driver.findElement(By.cssSelector("button[type='submit']"));
        inputUsuario.click();
        inputUsuario.clear();
        inputUsuario.sendKeys("Jessica-Lira");
        botaoLogin.click();
        boolean loginSucesso = wait.until(ExpectedConditions.urlToBe("http://localhost:3000/"));
        assertTrue(loginSucesso, "O login deveria ter sido realizado com sucesso.");

        // Localiza e clica no botão "Sair"
        WebElement botaoSair = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("logout-link"))
        );
        botaoSair.click();

        // Verifica se foi redirecionado para a página de login após sair
        boolean logoutSucesso = wait.until(ExpectedConditions.urlToBe("http://localhost:3000/login"));
        assertTrue(logoutSucesso, "Deveria ter redirecionado para a página de login.");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}