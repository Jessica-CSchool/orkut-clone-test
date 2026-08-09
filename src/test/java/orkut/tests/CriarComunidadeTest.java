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
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CriarComunidadeTest {

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
    public void criarComunidade() {
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

        // --- NAVEGAÇÃO PARA COMUNIDADES ---
        WebElement linkComunidades = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/comunidades')]"))
        );
        linkComunidades.click();

        // Verifica redirecionamento para a página correta
        boolean redirecionamentoComSucesso = wait.until(ExpectedConditions.urlContains("/comunidades"));
        assertTrue(redirecionamentoComSucesso, "Deveria ter redirecionado para a página de comunidades");

        // --- CRIAR NOVA COMUNIDADE ---
        WebElement inputNome = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title")));
        inputNome.sendKeys("Automação com Selenium");

        //WebElement inputCapa = driver.findElement(By.cssSelector("input[placeholder*='Coloque uma URL']"));
        //inputCapa.sendKeys("data:image/jpeg;base64,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");

        // Usando By.tagName para buscar todos os inputs e pegar o segundo (índice 1)
        WebElement inputCapa = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.tagName("input")
        ));
        inputCapa.sendKeys("data:image/jpeg;base64,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");

        // Submete o formulário
        WebElement botaoEnviar = driver.findElement(By.cssSelector("form button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", botaoEnviar);
        botaoEnviar.click();

        // Verifica se a nova comunidade aparece na tela
        boolean comunidadeCriada = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Automação com Selenium')]"))
        ).isDisplayed();

        assertTrue(comunidadeCriada, "A nova comunidade deveria estar visível na tela.");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}