package orkut.tests;

// IMPORT OFICIAL DO HEALENIUM (EPAM)
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

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        // Garante que o navegador inicie sem sessões antigas presas em cache
        options.addArguments("--incognito");

        WebDriver delegate = new ChromeDriver(options);
        driver = SelfHealingDriver.create(delegate);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void deveLogarComSucessoNoOrkut() {
        // 1. Acessa a aplicação local
        driver.get("http://localhost:3000");

        // 2. Aguarda o input de Usuário estar visível de verdade na tela de Login
        WebElement inputUsuario = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[placeholder='Usuário']"))
        );

        // 3. Localiza o botão de submit da tela de Login
        WebElement botaoLogin = driver.findElement(By.cssSelector("button[type='submit']"));

        // Interação segura com o elemento
        inputUsuario.click();
        inputUsuario.clear();
        inputUsuario.sendKeys("Jessica-Lira");

        // Dispara o evento de Login
        botaoLogin.click();

        // 4. Validação Definitiva: Aguarda o carregamento do elemento exclusivo da área logada
        // Nota técnica: Se o seletor da sua home for diferente de '.profileArea' (ex: um header ou h2),
        // substitua a string abaixo pelo seletor correto da sua área restrita.
        WebElement elementoHome = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".profileArea"))
        );

        assertTrue(elementoHome.isDisplayed(), "A página inicial pós-login não foi exibida.");
        System.out.println("Login efetuado com sucesso e validado com o Healenium ativo!");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}