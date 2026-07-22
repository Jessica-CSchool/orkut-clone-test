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

public class RedirectFotosTest {

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
    public void deveRedirecionarParaFotosPage() throws InterruptedException {
        driver.get("http://localhost:3000");

        // login
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


        // Localiza e clica no link para pagina de fotos
        WebElement linkFotos = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@href, '/fotos')]"))
        );

        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", linkFotos);

        // Verifica se foi redirecionado para a página de fotos
        boolean redirecionamentoComSucesso = wait.until(ExpectedConditions.urlToBe("http://localhost:3000/fotos"));
        assertTrue(redirecionamentoComSucesso, "Deveria ter redirecionado para a página de fotos");

        // Valida se o título da página de álbuns está visível na tela
        WebElement tituloAlbuns = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.title"))
        );
        assertTrue(tituloAlbuns.isDisplayed(), "O título da página de álbuns não foi exibido.");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}