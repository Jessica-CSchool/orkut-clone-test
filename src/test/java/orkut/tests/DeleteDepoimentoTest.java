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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteDepoimentoTest {

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
    public void deveDeletarPrimeiroDepoimentoComSucesso() throws InterruptedException {
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

        // Localiza e clica no link para página de depoimentos
        WebElement linkDepoimentos = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@href, '/depoimentos')]"))
        );

        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", linkDepoimentos);

        // Verifica o redirecionamento
        boolean redirecionamentoComSucesso = wait.until(ExpectedConditions.urlToBe("http://localhost:3000/depoimentos"));
        assertTrue(redirecionamentoComSucesso, "Deveria ter redirecionado para a página de depoimentos");

        // Captura a quantidade inicial de depoimentos na lista
        List<WebElement> depoimentosAntes = driver.findElements(By.cssSelector(".depoimento-item"));
        int quantidadeInicial = depoimentosAntes.size();

        // Localiza e clica no botão "Deletar do meu perfil" na primeira linha
        WebElement botaoDeletarPrimeiro = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector(".depoimento-item:first-child .txt-link-recusar"))
        );
        botaoDeletarPrimeiro.click();

        // Lida com o alerta de confirmação, caso apareça
        try {
            Alert alerta = wait.until(ExpectedConditions.alertIsPresent());
            alerta.accept();
        } catch (Exception e) {
            // Segue o fluxo se não houver alerta nativo
        }

        // Valida que a quantidade de depoimentos diminuiu em 1
        boolean quantidadeDiminuiu = wait.until(driver -> {
            int quantidadeAtual = driver.findElements(By.cssSelector(".depoimento-item")).size();
            return quantidadeAtual == quantidadeInicial - 1;
        });

        assertTrue(quantidadeDiminuiu, "A quantidade de depoimentos deveria ter diminuído após a exclusão.");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}