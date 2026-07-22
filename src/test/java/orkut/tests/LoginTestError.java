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

public class LoginTestError {

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
    public void deveExibirErroAoLogarComUsuarioInexistente() {
        driver.get("http://localhost:3000");

        WebElement inputUsuario = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[placeholder='Usuário']"))
        );
        inputUsuario.click();
        inputUsuario.clear();
        inputUsuario.sendKeys("usuarioInexistente");

        driver.findElement(By.cssSelector("button[type='submit']")).click();


        /*
        WebElement mensagemErro = wait.until(
        ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p[class$='mensagemErro']"))
        );
        */
        WebElement mensagemErro = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#msgErroUser"))
        );

        assertTrue(mensagemErro.getText().contains("Utilizador não cadastrado no banco local!"),
                "A mensagem de erro exibida está incorreta ou não apareceu.");

        System.out.println("Teste de erro validado com sucesso!");
    }


    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}