package orkut.tests;

import com.epam.healenium.SelfHealingDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddDepoimentoTest {

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
    public void deveAdicionarDepoimentoComSucesso() throws InterruptedException {
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

        // Localiza e clica no link para pagina de depoimentos
        WebElement linkDepoimentos = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@href, '/depoimentos')]"))
        );

        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", linkDepoimentos);

        // Verifica se foi redirecionado para a página de depoimentos
        boolean redirecionamentoComSucesso = wait.until(ExpectedConditions.urlToBe("http://localhost:3000/depoimentos"));
        assertTrue(redirecionamentoComSucesso, "Deveria ter redirecionado para a página de depoimentos");

        // 1. Preenche a caixa de texto do depoimento (textarea)
        WebElement inputDepoimento = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("txt-depoimento-input"))
        );
        inputDepoimento.click();
        inputDepoimento.clear();
        inputDepoimento.sendKeys("teste");

        // 2. Clica no botão de enviar depoimento
        WebElement botaoEnviarDepoimento = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn-enviar-depo"))
        );
        botaoEnviarDepoimento.click();

        // 3. Aguarda o alerta, valida a mensagem e clica em OK
        Alert alerta = wait.until(ExpectedConditions.alertIsPresent());
        String textoAlerta = alerta.getText();

        assertEquals("Depoimento enviado! Ele ficará na lista de Pendentes do usuário até ser aprovado.", textoAlerta);

        alerta.accept();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}