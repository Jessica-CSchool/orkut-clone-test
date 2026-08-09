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

public class EditComunidadeTest {

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
    public void alterarComunidade() {
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

        // --- NAVEGAÇÃO PARA COMUNIDADES USANDO LINK TEXT ---
        WebElement linkComunidades = wait.until(
                ExpectedConditions.elementToBeClickable(By.linkText("comunidades"))
        );
        linkComunidades.click();

        // Verifica redirecionamento para a página correta
        boolean redirecionamentoComSucesso = wait.until(ExpectedConditions.urlContains("/comunidades"));
        assertTrue(redirecionamentoComSucesso, "Deveria ter redirecionado para a página de comunidades");

        // --- CLICAR NA ABA GERENCIAR ---
        WebElement abaGerenciar = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'aba-item') and contains(text(), 'Gerenciar')]"))
        );
        abaGerenciar.click();

        // --- CLICAR NO BOTÃO EDITAR DA PRIMEIRA COMUNIDADE ---
        WebElement botaoEditarPrimeira = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("table.tabela-gerenciar tbody tr:first-child button.btn-edit"))
        );
        botaoEditarPrimeira.click();

        // --- ALTERAR O NOME DA COMUNIDADE ---
        WebElement inputNomeComunidade = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("title"))
        );
        inputNomeComunidade.clear();
        inputNomeComunidade.sendKeys("teste_Alterarnome");

        // Clica em "Salvar Alterações"
        WebElement botaoSalvar = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and contains(text(), 'Salvar Alterações')]"))
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", botaoSalvar);
        botaoSalvar.click();

        // --- VERIFICAR SE O NOME FOI ALTERADO ---
        boolean nomeAlteradoComSucesso = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'teste_Alterarnome')]"))
        ).isDisplayed();

        assertTrue(nomeAlteradoComSucesso, "O nome da comunidade deveria ter sido alterado para 'teste_Alterarnome'.");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}