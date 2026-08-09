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

public class DeleteComunidadeTest {

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
    public void deletarComunidade() {
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

        // --- CLICAR NA ABA GERENCIAR ---
        WebElement abaGerenciar = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'aba-item') and contains(text(), 'Gerenciar')]"))
        );
        abaGerenciar.click();

        // --- CAPTURAR O NOME OU IDENTIFICADOR DA PRIMEIRA COMUNIDADE ANTES DE DELETAR ---
        WebElement primeiroNomeComunidade = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.tabela-gerenciar tbody tr:first-child strong"))
        );
        String nomeComunidadeExcluida = primeiroNomeComunidade.getText();

        // --- CLICAR NO BOTÃO DELETAR DA PRIMEIRA COMUNIDADE ---
        WebElement botaoDeletarPrimeira = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("table.tabela-gerenciar tbody tr:first-child button.btn-del"))
        );

        // Garante que o botão está visível/clicável na tela
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", botaoDeletarPrimeira);
        botaoDeletarPrimeira.click();

        // --- VERIFICAR SE A COMUNIDADE FOI REMOVIDA ---
        // Se houver alerta nativo de confirmação (caso a aplicação use window.confirm):
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            // Caso a aplicação não utilize alert nativo do navegador, o bloco é ignorado com segurança
        }

        // Valida que a comunidade específica não está mais visível na tabela/listagem
        boolean comunidadeRemovida = wait.until(
                ExpectedConditions.invisibilityOfElementLocated(By.xpath("//strong[text()='" + nomeComunidadeExcluida + "']"))
        );

        assertTrue(comunidadeRemovida, "A comunidade '" + nomeComunidadeExcluida + "' deveria ter sido removida da listagem.");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}